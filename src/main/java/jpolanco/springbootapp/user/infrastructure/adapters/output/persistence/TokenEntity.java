package jpolanco.springbootapp.user.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.application.utils.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "tokens")
@Table(name = "tokens", indexes = {
        @Index(name = "idx_token_value", columnList = "token"),
        @Index(name = "idx_user_id", columnList = "user_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 1000, nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.BEARER;

    @Enumerated(EnumType.STRING)
    private TokenStatus status = TokenStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
