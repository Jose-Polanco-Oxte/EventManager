package jpolanco.springbootapp.user.application.utils;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class TokenE {
    private String token;
    private User user;
    private TokenStatus status;
    private Instant createdAt;

    public TokenE() {
        // Default constructor for serialization/deserialization
    }

    @Override
    public String toString() {
        return "TokenE{" +
                "token='" + token + '\'' +
                ", userId='" + (user != null ? user : "null") + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenE tokenE)) return false;
        return token.equals(tokenE.token) && user.equals(tokenE.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user);
    }
}
