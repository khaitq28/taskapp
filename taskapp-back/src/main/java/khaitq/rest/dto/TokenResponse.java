package khaitq.rest.dto;


import lombok.Builder;

@Builder
public record TokenResponse (String token, String refreshToken) {
}