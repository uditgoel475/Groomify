package com.uditgoel.groomify;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.uditgoel.groomify.dto.JwtAuthenticationResponse;
import com.uditgoel.groomify.support.EmbeddedRedisInitializer;

/**
 * End-to-end test of the auth flow. Boots the full Spring context against H2
 * (PostgreSQL compatibility mode) and an in-process Redis server. Drives the system
 * over real HTTP — like Playwright does for UIs, but for the REST surface.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = EmbeddedRedisInitializer.class)
class AuthFlowEndToEndTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate rest;

	@Test
	void customerSignup_thenSignin_issuesAccessAndRefreshTokens() {
		String username = "alice" + System.nanoTime();
		String email = username + "@example.com";
		String password = "Test@1234";

		ResponseEntity<String> signupResp = rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, email, password), String.class);
		assertThat(signupResp.getStatusCode())
				.as("signup body=%s", signupResp.getBody())
				.isEqualTo(HttpStatus.CREATED);

		ResponseEntity<JwtAuthenticationResponse> signinResp = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", username, "password", password),
				JwtAuthenticationResponse.class);
		assertThat(signinResp.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(signinResp.getBody()).isNotNull();
		assertThat(signinResp.getBody().accessToken()).isNotBlank();
		assertThat(signinResp.getBody().refreshToken()).isNotBlank();
		assertThat(signinResp.getBody().expiresAt()).isNotNull();
	}

	@Test
	void signin_withWrongPassword_returns401() {
		String username = "bob" + System.nanoTime();
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "@example.com", password), String.class);

		ResponseEntity<String> resp = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", username, "password", "Wrong@9999"), String.class);

		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void signin_withUnknownUsername_returns401() {
		ResponseEntity<String> resp = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", "ghost-" + System.nanoTime(), "password", "Test@1234"), String.class);
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void refreshToken_returnsNewAccessToken() {
		String username = "carol" + System.nanoTime();
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "@example.com", password), String.class);
		ResponseEntity<JwtAuthenticationResponse> signin = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", username, "password", password), JwtAuthenticationResponse.class);
		String refreshToken = signin.getBody().refreshToken();

		ResponseEntity<JwtAuthenticationResponse> refresh = rest.postForEntity(
				url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", refreshToken),
				JwtAuthenticationResponse.class);

		assertThat(refresh.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(refresh.getBody()).isNotNull();
		assertThat(refresh.getBody().accessToken()).isNotBlank();
		assertThat(refresh.getBody().refreshToken()).isNotBlank();
		assertThat(refresh.getBody().refreshToken()).isNotEqualTo(refreshToken);
	}

	@Test
	void refreshToken_withInvalidToken_returns401() {
		ResponseEntity<String> resp = rest.postForEntity(url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", "definitely-not-a-real-token"), String.class);
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void usernameCollision_onSignup_returns409() {
		String username = "dave" + System.nanoTime();
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "-1@example.com", password), String.class);

		ResponseEntity<String> dup = rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "-2@example.com", password), String.class);

		assertThat(dup.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	void emailCollision_onSignup_returns409() {
		String email = "eve" + System.nanoTime() + "@example.com";
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup("user1-" + System.nanoTime(), email, password), String.class);

		ResponseEntity<String> dup = rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup("user2-" + System.nanoTime(), email, password), String.class);

		assertThat(dup.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	void protectedEndpoint_withoutBearer_returns401() {
		ResponseEntity<String> resp = rest.getForEntity(url("/api/customer/by/username/anyone"), String.class);
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void protectedEndpoint_withBareToken_noBearerPrefix_returns401() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "some-jwt-without-bearer-prefix");
		ResponseEntity<String> resp = rest.exchange(url("/api/customer/by/username/anyone"), HttpMethod.GET,
				new HttpEntity<>(headers), String.class);
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void publicAvailabilityEndpoint_isReachableWithoutAuth() {
		ResponseEntity<String> resp = rest.getForEntity(
				url("/api/customer/checkUsernameAvailability/some-random-" + System.nanoTime()),
				String.class);
		assertThat(resp.getStatusCode().is2xxSuccessful() || resp.getStatusCode().is4xxClientError())
				.as("must not be 5xx — got %s", resp.getStatusCode())
				.isTrue();
	}

	@Test
	void accessToken_unlocks_ownProfile() {
		String username = "frank" + System.nanoTime();
		String password = "Test@1234";
		ResponseEntity<String> signup = rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "@example.com", password), String.class);
		assertThat(signup.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		String accessToken = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", username, "password", password), JwtAuthenticationResponse.class)
				.getBody().accessToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		ResponseEntity<String> probe = rest.exchange(url("/api/customer/" + username), HttpMethod.GET,
				new HttpEntity<>(headers), String.class);

		assertThat(probe.getStatusCode())
				.as("own-profile fetch must be 200, got %s body=%s", probe.getStatusCode(), probe.getBody())
				.isEqualTo(HttpStatus.OK);
		assertThat(probe.getBody()).contains("\"username\":\"" + username + "\"");
	}

	@Test
	void accessToken_cannotFetch_otherUsersProfile_returns403() {
		String aliceName = "alice" + System.nanoTime();
		String bobName = "bob" + System.nanoTime() + "x";
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(aliceName, aliceName + "@example.com", password), String.class);
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(bobName, bobName + "@example.com", password), String.class);
		String aliceToken = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", aliceName, "password", password), JwtAuthenticationResponse.class)
				.getBody().accessToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + aliceToken);
		ResponseEntity<String> probe = rest.exchange(url("/api/customer/" + bobName), HttpMethod.GET,
				new HttpEntity<>(headers), String.class);

		assertThat(probe.getStatusCode())
				.as("alice fetching bob's profile must be 403 Forbidden, got %s body=%s",
						probe.getStatusCode(), probe.getBody())
				.isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	void signupWithMalformedPayload_returns400_withFieldErrors() {
		Map<String, Object> body = new HashMap<>();
		body.put("username", "");                      // @NotBlank violated
		body.put("email", "not-an-email");             // @Email violated
		body.put("password", "weak");                  // @Pattern + @Size violated
		body.put("name", "");
		body.put("regId", "");
		body.put("govtIdType", "");
		body.put("govtId", "");
		body.put("contact", 9876543210L);
		body.put("gender", "MALE");
		body.put("dob", "1990-01-15");
		body.put("billingAddress", Map.of(
				"country", "India", "state", "Karnataka", "city", "Bangalore",
				"postalCode", 560038, "address1", "1 MG Road"));
		body.put("sameShipping", true);

		ResponseEntity<String> resp = rest.postForEntity(url("/api/auth/customer/signup"), body, String.class);

		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(resp.getBody()).contains("\"errors\":");
		assertThat(resp.getBody()).contains("\"field\":\"email\"");
		assertThat(resp.getBody()).contains("\"field\":\"password\"");
	}

	@Test
	void weakPassword_onSignup_isRejected() {
		String username = "grace" + System.nanoTime();
		Map<String, Object> body = validCustomerSignup(username, username + "@example.com", "weakpass");

		ResponseEntity<String> resp = rest.postForEntity(url("/api/auth/customer/signup"), body, String.class);

		assertThat(resp.getStatusCode())
				.as("weak password must be rejected as 400 Bad Request, got %s body=%s",
						resp.getStatusCode(), resp.getBody())
				.isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void refreshToken_rotates_oldOneRejected() {
		String username = "rot1-" + System.nanoTime();
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "@example.com", password), String.class);
		String t1 = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", username, "password", password), JwtAuthenticationResponse.class)
				.getBody().refreshToken();

		ResponseEntity<JwtAuthenticationResponse> firstRefresh = rest.postForEntity(
				url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", t1), JwtAuthenticationResponse.class);
		assertThat(firstRefresh.getStatusCode()).isEqualTo(HttpStatus.OK);
		String t2 = firstRefresh.getBody().refreshToken();
		assertThat(t2).isNotEqualTo(t1);

		ResponseEntity<String> reuseT1 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", t1), String.class);
		assertThat(reuseT1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void refreshTokenReuse_wipesEntireFamily() {
		String username = "rot2-" + System.nanoTime();
		String password = "Test@1234";
		rest.postForEntity(url("/api/auth/customer/signup"),
				validCustomerSignup(username, username + "@example.com", password), String.class);
		String t1 = rest.postForEntity(url("/api/auth/customer/signin"),
				Map.of("username", username, "password", password), JwtAuthenticationResponse.class)
				.getBody().refreshToken();
		String t2 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", t1), JwtAuthenticationResponse.class)
				.getBody().refreshToken();

		// Reuse t1 — theft signal — should wipe the family (t1 + t2).
		ResponseEntity<String> reuseT1 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", t1), String.class);
		assertThat(reuseT1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

		// t2 should also now be invalid — family was wiped.
		ResponseEntity<String> reuseT2 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
				Map.of("refreshToken", t2), String.class);
		assertThat(reuseT2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	private String url(String path) {
		return "http://localhost:" + port + path;
	}

	/**
	 * Returns a plausible CustomerDTO payload. Server may still reject — the iterating
	 * tests will surface what's actually required.
	 */
	private Map<String, Object> validCustomerSignup(String username, String email, String password) {
		Map<String, Object> billing = new HashMap<>();
		billing.put("country", "India");
		billing.put("state", "Karnataka");
		billing.put("city", "Bangalore");
		billing.put("region", "Indiranagar");
		billing.put("postalCode", 560038);
		billing.put("address1", "1 MG Road");

		Map<String, Object> body = new HashMap<>();
		body.put("username", username);
		body.put("name", "First Middle Last");
		body.put("email", email);
		body.put("password", password);
		body.put("dob", "1990-01-15");
		body.put("contact", 9876543210L);
		body.put("alternateContact", 9876543211L);
		body.put("gender", "MALE");
		body.put("regId", "REG-" + System.nanoTime());
		body.put("govtIdType", "AADHAR");
		body.put("govtId", "GID-" + System.nanoTime());
		body.put("billingAddress", billing);
		body.put("shippingAddress", billing);
		body.put("sameShipping", true);
		return body;
	}
}
