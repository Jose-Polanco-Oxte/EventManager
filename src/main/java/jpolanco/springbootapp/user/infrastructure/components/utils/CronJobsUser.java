package jpolanco.springbootapp.user.infrastructure.components.utils;

import io.jsonwebtoken.ExpiredJwtException;
import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.infrastructure.components.providers.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CronJobsUser {

    private final JwtQueryRepository jwtQueryRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final JwtManager jwtManager;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void reviewStatusToken() {
        var tokens = jwtQueryRepository.findAll();
        if (tokens.isEmpty()) {
            return;
        }
        for (var token : tokens) {
            try {
                jwtManager.isTokenExpired(token.getToken());
            } catch (ExpiredJwtException e) {
                if (!token.getStatus().equals(TokenStatus.EXPIRED)) {
                    token.setStatus(TokenStatus.EXPIRED);
                    jwtCommandRepository.save(token);
                }
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 */2 * *")
    public void deleteExpiredTokens() {
        System.out.println("Deleting expired and revoked tokens...");
        jwtCommandRepository.deleteAllByStatus(TokenStatus.EXPIRED);
        jwtCommandRepository.deleteAllByStatus(TokenStatus.REVOKED);
    }
}
