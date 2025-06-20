package jpolanco.springbootapp.user.infrastructure.adapters.output.persistence;

import jakarta.persistence.*;
import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.shared.utils.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(name = "tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 1000)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.BEARER;

    @Enumerated(EnumType.STRING)
    private TokenStatus status = TokenStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
