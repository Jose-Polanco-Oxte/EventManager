package jpolanco.springbootapp.user.infrastructure.components;

import io.jsonwebtoken.ExpiredJwtException;
import jpolanco.springbootapp.shared.domain.TokenStatus;
import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CronJobsUser {

    private final JwtRepository jpaTokenRepository;
    private final JwtProvider jwtService;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void reviewStatusToken() {
        var tokens = jpaTokenRepository.findAll();
        if (tokens.isEmpty()) {
            return;
        }
        for (var token : tokens) {
            try {
                jwtService.isTokenExpired(token.getToken());
            } catch (ExpiredJwtException e) {
                if (!token.isExpired()) {
                    token.changeStatus(TokenStatus.EXPIRED);
                    jpaTokenRepository.save(token);
                }
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 */2 * *")
    public void deleteExpiredTokens() {
        System.out.println("Deleting expired and revoked tokens...");
        jpaTokenRepository.deleteAllByStatus(TokenStatus.EXPIRED);
        jpaTokenRepository.deleteAllByStatus(TokenStatus.REVOKED);
    }
}
