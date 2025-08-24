package jpolanco.applicationcore.shared.infrastructure.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that captures request metadata and stores it in a ThreadLocal context for the duration of the request.
 * This includes generating a unique transaction ID, capturing the client's IP address, and User-Agent.
 * The context is cleared after the request is processed to prevent memory leaks.
 */
@Component("RequestMetadataFilter")
@RequiredArgsConstructor
public class RequestContextFilter extends OncePerRequestFilter {
    private final ThreadLocalContext threadLocalContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String transactionId = UUID.randomUUID().toString(); // Generate a unique transaction ID
            threadLocalContext.put("transactionId", transactionId);

            threadLocalContext.put("ip", request.getRemoteAddr());
            threadLocalContext.put("userAgent", request.getHeader("User-Agent"));

            filterChain.doFilter(request, response); // Continue the filter chain
        } finally {
            threadLocalContext.clear(); // Clear the context to prevent memory leaks
        }
    }
}
