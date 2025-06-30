package jpolanco.springbootapp.user.application.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "TokenE{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenE tokenE)) return false;
        return token.equals(tokenE.token) && userId.equals(tokenE.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, userId);
    }
}
