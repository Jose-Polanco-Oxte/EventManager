package jpolanco.springbootapp.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.components.utils.JwtManager;
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

    private final JwtManager jwtManager;
    private final UserDetailsService userDetailsService;
    private final JwtRepository jpaTokenRepository;
    private final UserRepository userRepository;

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
        final String userEmail = jwtManager.extractUsername(jwtToken);
        if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        final TokenE tokenEntity = jpaTokenRepository.findByToken(jwtToken).orElse(null);
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
        final boolean isTokenValid = jwtManager.isTokenValid(jwtToken, maybeUser.get().getEmail());
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
