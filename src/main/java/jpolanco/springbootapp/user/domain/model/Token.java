package jpolanco.springbootapp.user.domain.model;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.shared.utils.TokenType;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserId;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Token {
    private Long id;
    private UserId userId;
    private String token;
    private TokenType tokenType;
    private int uses;
    private Instant createdAt;
    private Instant lastUsed;
    private Instant expires;
    private TokenStatus status;

    private Token(Long id, UserId userId, String token, TokenType tokenType, int uses, Instant createdAt, Instant lastUsed, Instant expires, TokenStatus status) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.tokenType = tokenType;
        this.uses = uses;
        this.createdAt = createdAt;
        this.lastUsed = lastUsed;
        this.expires = expires;
        this.status = status;
    }

    public static Result<Token> create(String userId, String token, TokenType tokenType, long expirationInMillis) {
        var validUserId = UserId.create(userId);
        if (validUserId.isFailure()) {
            return Result.failure(validUserId.getError());
        }
        return Result.success( new Token(
                null,
                validUserId.getValue(),
                token,
                tokenType,
                0,
                Instant.now(),
                null,
                Instant.now().plusMillis(expirationInMillis),
                TokenStatus.ACTIVE
        ));
    }

    public static Result<Token> load(Long id, String userId, String token, TokenType tokenType, int uses, Instant createdAt, Instant lastUsed, Instant expires, TokenStatus status) {
        var validUserId = UserId.create(userId);
        if (validUserId.isFailure()) {
            return Result.failure(validUserId.getError());
        }
        return Result.success(new Token(
                id,
                validUserId.getValue(),
                token,
                tokenType,
                uses,
                createdAt,
                lastUsed,
                expires,
                status
        ));
    }

    public void changeStatus(TokenStatus status) {
        this.status = status;
    }

    public boolean isRevoked() {
        return this.status == TokenStatus.REVOKED;
    }

    public boolean isExpired() {
        return this.status == TokenStatus.EXPIRED;
    }

    public boolean isActive() {
        return this.status == TokenStatus.ACTIVE;
    }

    public boolean isValid() {
        return isActive() && !isExpired();
    }

    public void incrementUses() {
        this.uses++;
    }
}
