package com.uditgoel.groomify.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record JwtAuthenticationResponse(String accessToken, String refreshToken, Instant expiresAt) {
}
