package jpolanco.applicationcore.user.infrastructure.adapters.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    private String type;

    private String status = TokenStatus.ACTIVE.name();

    private Instant createdAt = Instant.now();

    @Indexed(unique = true)
    private String userUUID;

    public static TokenEntity of(String token) {
        return new TokenEntity(null, token, null, TokenStatus.ACTIVE.name(), Instant.now(), null);
    }

    public TokenEntity withId(String id) {
        return new TokenEntity(id, this.token, this.type, this.status, this.createdAt, this.userUUID);
    }

    public TokenEntity withTypeBearer() {
        return new TokenEntity(this.id, this.token, TokenType.BEARER.name(), this.status, this.createdAt, this.userUUID);
    }

    public TokenEntity withUserUUID(String userUUID) {
        return new TokenEntity(this.id, this.token, this.type, this.status, this.createdAt, userUUID);
    }

    public TokenEntity withRevokeStatus() {
        return new TokenEntity(this.id, this.token, this.type, TokenStatus.REVOKED.name(), this.createdAt, this.userUUID);
    }

    public TokenEntity withExpiredStatus() {
        return new TokenEntity(this.id, this.token, this.type, TokenStatus.EXPIRED.name(), this.createdAt, this.userUUID);
    }


    enum TokenStatus {
        ACTIVE, REVOKED, EXPIRED
    }

    enum TokenType {
        BEARER,
    }
}
