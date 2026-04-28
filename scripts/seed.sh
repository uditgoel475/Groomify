#!/usr/bin/env bash
# Loads sample data into a running Groomify instance and verifies the load by
# signing in as a seeded user and fetching their profile.
#
# Default target: http://localhost:8080 (the docker-compose stack on this host).
# Override with $GROOMIFY_BASE_URL.
#
#   ./scripts/seed.sh                                    # seed against localhost:8080
#   GROOMIFY_BASE_URL=https://staging.example ./scripts/seed.sh
#   ./scripts/seed.sh --skip-wait                        # don't poll for readiness
#   ./scripts/seed.sh --customers-only                   # skip the verification step
#
# Idempotent: rows that already exist (409 Conflict) are reported as "exists"
# rather than treated as failures.

set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
base_url="${GROOMIFY_BASE_URL:-http://localhost:8080}"
seed_dir="$repo_root/scripts/seed"
customers_file="$seed_dir/customers.json"

skip_wait=false
verify=true
ready_timeout="${GROOMIFY_READY_TIMEOUT:-120}"   # seconds to wait for /v3/api-docs

while [ $# -gt 0 ]; do
  case "$1" in
    --skip-wait)        skip_wait=true ;;
    --customers-only)   verify=false ;;
    -h|--help)
      grep '^#' "$0" | sed 's/^# \{0,1\}//' ; exit 0 ;;
    *) echo "unknown flag: $1" >&2 ; exit 2 ;;
  esac
  shift
done

if ! command -v jq >/dev/null 2>&1; then
  echo "jq is required (brew install jq)" >&2 ; exit 1
fi
if ! command -v curl >/dev/null 2>&1; then
  echo "curl is required" >&2 ; exit 1
fi
if [ ! -f "$customers_file" ]; then
  echo "missing $customers_file" >&2 ; exit 1
fi

GREEN=$'\033[32m'; RED=$'\033[31m'; DIM=$'\033[2m'; OFF=$'\033[0m'
log_ok()   { printf "  %s✓%s %s\n"  "$GREEN" "$OFF" "$*"; }
log_skip() { printf "  %s⋯%s %s\n"  "$DIM"   "$OFF" "$*"; }
log_err()  { printf "  %s✗%s %s\n"  "$RED"   "$OFF" "$*"; }

wait_for_app() {
  if $skip_wait; then return; fi
  echo "Waiting for $base_url to be ready (timeout ${ready_timeout}s)..."
  local deadline=$(( $(date +%s) + ready_timeout ))
  while [ "$(date +%s)" -lt "$deadline" ]; do
    if curl -fsS --max-time 3 "$base_url/v3/api-docs" >/dev/null 2>&1; then
      echo "Ready."
      return 0
    fi
    sleep 2
  done
  echo "Timed out waiting for $base_url" >&2
  exit 1
}

post_signup() {
  local body="$1"
  local username
  username="$(jq -r '.username' <<<"$body")"

  local resp_file
  resp_file="$(mktemp)"
  local status
  status="$(curl -sS -o "$resp_file" -w "%{http_code}" \
              -H 'Content-Type: application/json' \
              -d "$body" \
              "$base_url/api/auth/customer/signup")"

  case "$status" in
    201)  log_ok   "customer $username created" ;;
    409)  log_skip "customer $username already exists (skipping)" ;;
    *)    log_err  "customer $username FAILED ($status): $(head -c 300 "$resp_file")" ;;
  esac
  rm -f "$resp_file"

  [ "$status" = "201" ] || [ "$status" = "409" ]
}

verify_one_user() {
  local first_user_body
  first_user_body="$(jq -c '.[0]' "$customers_file")"
  local username password
  username="$(jq -r '.username' <<<"$first_user_body")"
  password="$(jq -r '.password' <<<"$first_user_body")"

  echo
  echo "Verifying seed by signing in as $username and fetching profile..."

  local signin_resp signin_status
  signin_resp="$(mktemp)"
  signin_status="$(curl -sS -o "$signin_resp" -w "%{http_code}" \
                    -H 'Content-Type: application/json' \
                    -d "{\"username\":\"$username\",\"password\":\"$password\"}" \
                    "$base_url/api/auth/customer/signin")"
  if [ "$signin_status" != "200" ]; then
    log_err "signin failed: HTTP $signin_status — $(head -c 300 "$signin_resp")"
    rm -f "$signin_resp"
    return 1
  fi
  log_ok "signin returned 200"

  local token
  token="$(jq -r '.accessToken' "$signin_resp")"
  rm -f "$signin_resp"
  if [ -z "$token" ] || [ "$token" = "null" ]; then
    log_err "signin response had no accessToken"
    return 1
  fi

  local profile_resp profile_status
  profile_resp="$(mktemp)"
  profile_status="$(curl -sS -o "$profile_resp" -w "%{http_code}" \
                     -H "Authorization: Bearer $token" \
                     "$base_url/api/customer/$username")"
  if [ "$profile_status" != "200" ]; then
    log_err "GET /api/customer/$username failed: HTTP $profile_status — $(head -c 300 "$profile_resp")"
    rm -f "$profile_resp"
    return 1
  fi

  local profile_user profile_email profile_city
  profile_user="$(jq -r '.username' "$profile_resp")"
  profile_email="$(jq -r '.email' "$profile_resp")"
  profile_city="$(jq -r '.billingAddress.city' "$profile_resp")"
  rm -f "$profile_resp"

  log_ok "profile: $profile_user | $profile_email | $profile_city"
}

# --- main ---
wait_for_app

echo
echo "Seeding customers from $customers_file..."
total=0; ok=0; failed=0
while IFS= read -r row; do
  total=$((total+1))
  if post_signup "$row"; then ok=$((ok+1)); else failed=$((failed+1)); fi
done < <(jq -c '.[]' "$customers_file")

echo
printf "Customers: %d total, %d ok/exists, %d failed\n" "$total" "$ok" "$failed"

if $verify; then
  if ! verify_one_user; then
    exit 1
  fi
fi

echo
log_ok "seed complete"
