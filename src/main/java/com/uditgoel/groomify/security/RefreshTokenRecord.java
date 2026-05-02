package com.uditgoel.groomify.security;

import java.time.Instant;

import com.uditgoel.groomify.dto.JwtJsonSubjectKey;

/**
 * Redis-stored value for a refresh token. Per-token state for the
 * rotation-with-reuse-detection pattern:
 *
 * <ul>
 *   <li>{@code subject} — what the token represents (user identity).</li>
 *   <li>{@code familyId} — every refresh-token chain that started from one signin
 *       shares a family. On reuse-after-rotation we wipe the entire family.</li>
 *   <li>{@code rotatedAt} — null while the token is still valid; set to now() when
 *       rotation happens. A second presentation of a rotated token is a theft signal.</li>
 * </ul>
 */
public record RefreshTokenRecord(JwtJsonSubjectKey subject, String familyId, Instant rotatedAt) {
}
