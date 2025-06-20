package jpolanco.springbootapp.user.application.utils;

import jpolanco.springbootapp.shared.utils.TokenStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class TokenE {
    private String token;
    private String userId;
    private TokenStatus status;
    private Instant createdAt;

    public TokenE(String token, String userId, TokenStatus status, Instant createdAt) {
        this.token = token;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public TokenE() {
        // Default constructor for serialization/deserialization
    }

}
