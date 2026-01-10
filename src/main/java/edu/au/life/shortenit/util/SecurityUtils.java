package edu.au.life.shortenit.util;

import edu.au.life.shortenit.entity.User;
import edu.au.life.shortenit.repository.UserRepository;
import edu.au.life.shortenit.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private static UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        SecurityUtils.userRepository = repo;
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }

        Object details = authentication.getDetails();

        if (details instanceof JwtAuthenticationFilter.UserPrincipal) {
            JwtAuthenticationFilter.UserPrincipal principal =
                    (JwtAuthenticationFilter.UserPrincipal) details;

            return userRepository.findById(principal.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        throw new RuntimeException("Invalid authentication");
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }
}
