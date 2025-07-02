package jpolanco.springbootapp.user.application.utils;

import jpolanco.springbootapp.user.domain.model.value_objects.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class TokenE {
    private String token;
    private Long userId;
    private TokenStatus status;
    private Instant createdAt;

    public TokenE() {
        // Default constructor for serialization/deserialization
    }

    @Override
    public String toString() {
        return "TokenE{" +
                "token='" + token + '\'' +
                ", userId='" + (userId != null ? userId : "null") + '\'' +
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
