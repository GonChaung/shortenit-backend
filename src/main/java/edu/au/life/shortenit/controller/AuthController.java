package edu.au.life.shortenit.controller;

import edu.au.life.shortenit.dto.LoginResponse;
import edu.au.life.shortenit.dto.RefreshTokenRequest;
import edu.au.life.shortenit.entity.User;
import edu.au.life.shortenit.service.AuthService;
import edu.au.life.shortenit.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.LoginExceptionResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // call after successful microsoft login
    @GetMapping("/oauth2/success")
    public ResponseEntity<LoginResponse> oauth2Success (@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("mail");
        if (email == null) {
            email = principal.getAttribute("userPrincipalName");
        }
        String name = principal.getAttribute("displayName");
        String microsoftId = principal.getAttribute("id");

        LoginResponse response = authService.loginWithMicrosoft(email, name, microsoftId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Void> logout() {
        User currentUser = SecurityUtils.getCurrentUser();
        authService.logout(currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse.UserInfo> getCurrentUser() {
        User user = SecurityUtils.getCurrentUser();

        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();

        return ResponseEntity.ok(userInfo);
    }
}
