package jpolanco.springbootapp.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpolanco.springbootapp.shared.domain.TokenStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.mysql.UserRepositoryMySQL;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import jpolanco.springbootapp.user.infrastructure.services.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final JpaTokenRepository jpaTokenRepository;
    private final UserRepositoryMySQL userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwtToken);
        if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        final TokenEntity tokenEntity = jpaTokenRepository.findByToken(jwtToken).orElse(null);
        if (tokenEntity == null || tokenEntity.getStatus() != TokenStatus.ACTIVE) {
            filterChain.doFilter(request, response);
            return;
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        final var maybeUser = userRepository.findByEmail(userDetails.getUsername());
        if (maybeUser.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        final boolean isTokenValid = jwtService.isTokenValid(jwtToken, maybeUser.get().getEmail());
        if (!isTokenValid) {
            return;
        }
        final var authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
