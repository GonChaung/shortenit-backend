package edu.au.life.shortenit.security;

import edu.au.life.shortenit.entity.ApiKey;
import edu.au.life.shortenit.entity.User;
import edu.au.life.shortenit.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApplicationContext applicationContext;
    private ApiKeyRepository apiKeyRepository;

    public ApiKeyAuthenticationFilter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private ApiKeyRepository getApiKeyRepository() {
        if (apiKeyRepository == null) {
            apiKeyRepository = applicationContext.getBean(ApiKeyRepository.class);
        }
        return apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip API key validation for public endpoints
        if (path.startsWith("/s/") ||
                path.startsWith("/api/auth/") ||
                path.startsWith("/oauth2/") ||
                path.startsWith("/login/oauth2/code/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String apiKeyHeader = request.getHeader("X-API-Key");

        if (apiKeyHeader == null || apiKeyHeader.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Get all API keys and check with BCrypt
            List<ApiKey> allKeys = getApiKeyRepository().findAll();
            ApiKey matchedKey = null;

            for (ApiKey key : allKeys) {
                if (BCrypt.checkpw(apiKeyHeader, key.getKeyHash())) {
                    matchedKey = key;
                    break;
                }
            }

            if (matchedKey != null) {
                // Check expiration
                if (matchedKey.getExpiresAt() != null &&
                        matchedKey.getExpiresAt().isBefore(LocalDateTime.now())) {
                    filterChain.doFilter(request, response);
                    return;
                }

                User user = matchedKey.getUser();

                // Create authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                        );

                authToken.setDetails(new JwtAuthenticationFilter.UserPrincipal(
                        user.getId(), user.getEmail(), user.getRole().name()));

                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Update last used (in a separate thread to avoid transaction issues)
                matchedKey.setLastUsedAt(LocalDateTime.now());
                getApiKeyRepository().save(matchedKey);
            }
        } catch (Exception e) {
            logger.error("API Key validation error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}