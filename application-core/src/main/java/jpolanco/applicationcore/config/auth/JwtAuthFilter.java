package jpolanco.applicationcore.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpolanco.applicationcore.user.application.ports.input.JwtProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtManager;

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

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);

        Optional<String> maybeUserEmail = jwtManager.extractUsername(jwtToken);
        Optional<UUID> maybeUserId = jwtManager.extractId(jwtToken);
        Optional<List<String>> maybeRoles = jwtManager.extractRoles(jwtToken);

        if (maybeUserEmail.isEmpty() || maybeUserId.isEmpty() || maybeRoles.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isTokenValid = jwtManager.isTokenValid(jwtToken, maybeUserEmail.get());

        if (SecurityContextHolder.getContext().getAuthentication() != null || !isTokenValid) {
            filterChain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails = new MyUserDetails(
                maybeUserId.get(),
                maybeUserEmail.get(),
                "",
                true,
                maybeRoles.get()
        );

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
