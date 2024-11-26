package com.example.tms.security.service.impl;

import com.example.tms.error.RefreshTokenException;
import com.example.tms.security.dto.*;
import com.example.tms.security.jwt.JwtUtils;
import com.example.tms.security.model.RefreshToken;
import com.example.tms.security.model.User;
import com.example.tms.security.repository.UserRepository;
import com.example.tms.security.service.RefreshTokenService;
import com.example.tms.security.service.SecurityService;
import com.example.tms.security.service.RequestFormValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final RequestFormValidationService requestFormValidationService;

    @Override
    public LoginResponse authenticateUser(SigninRequest signinRequest) {
        requestFormValidationService.validateEmptyFormFields(signinRequest);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.email(),
                signinRequest.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new LoginResponse(
                userDetails.getId(),
                jwtUtils.generateToken(userDetails),
                refreshToken.getToken(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public RegisterResponse register(SignupRequest signupRequest) {
        requestFormValidationService.validateEmptyFormFields(signupRequest);
        requestFormValidationService.validateEmail(signupRequest.email());

        var user = User.builder()
                .email(signupRequest.email())
                .username(signupRequest.username())
                .password(passwordEncoder.encode(signupRequest.password()))
                .roles(signupRequest.roles())
                .build();

        User registeredUser = userRepository.save(user);

        return new RegisterResponse(
                registeredUser.getId(),
                registeredUser.getEmail(),
                jwtUtils.generateToken(registeredUser.getEmail())
        );
    }

    @Override
    public RefreshTokenResponse refreshToken(RequestTokenRefresh request) {
        String refreshToken = request.tokenRefresh();
        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId)
                            .orElseThrow(() -> new RefreshTokenException("Ошибка получения токена для userId: " + userId));
                    String token = jwtUtils.generateTokenFromEmail(tokenOwner.getEmail());
                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "Refresh token не найден"));
    }

    @Override
    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof UserDetailsImpl userDetails) {
            Long userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
        }
    }
}
