package khaitq.domain;


import lombok.Builder;

@Builder
public record Identity(String userId, String email, String role) {}
