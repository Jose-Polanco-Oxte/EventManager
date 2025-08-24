package jpolanco.applicationcore.user.infrastructure.components.utils;

import io.jsonwebtoken.ExpiredJwtException;
import jpolanco.applicationcore.user.application.ports.input.JwtProvider;
import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.application.ports.output.TokenQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CronJobsUser {

    private final TokenQueryRepository tokenQueryRepository;
    private final TokenCommandRepository tokenCommandRepository;
    private final JwtProvider jwtManager;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void reviewStatusToken() {
        var tokens = tokenQueryRepository.findAll();
        if (tokens.isEmpty()) {
            return;
        }
        for (var token : tokens) {
            try {
                jwtManager.isTokenExpired(token.value());
            } catch (ExpiredJwtException e) {
                if (!token.status().equals("EXPIRED")) {
                    tokenCommandRepository.expireToken(token.value());
                }
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 */2 * *")
    public void deleteExpiredTokens() {
        System.out.println("Deleting expired and revoked tokens...");
        tokenCommandRepository.deleteAllByStatus("EXPIRED");
        tokenCommandRepository.deleteAllByStatus("REVOKED");
    }
}
