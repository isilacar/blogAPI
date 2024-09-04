package com.scalefocus.blog_api.service.impl;

import com.scalefocus.blog_api.entity.Token;
import com.scalefocus.blog_api.entity.User;
import com.scalefocus.blog_api.exception.UserExistException;
import com.scalefocus.blog_api.repository.TokenRepository;
import com.scalefocus.blog_api.repository.UserRepository;
import com.scalefocus.blog_api.request.AuthenticationRequest;
import com.scalefocus.blog_api.request.RegisterRequest;
import com.scalefocus.blog_api.response.TokenResponse;
import com.scalefocus.blog_api.security.JwtTokenProvider;
import com.scalefocus.blog_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    public TokenResponse register(RegisterRequest registerRequest) {
        Boolean existsByUsername = userRepository.existsByUsername(registerRequest.getUsername());

        if (existsByUsername) {
            throw new UserExistException("Username already exists");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .displayName(registerRequest.getDisplayName())
                .build();

        User savedUser = userRepository.save(user);
        String jwtToken = jwtTokenProvider.generateToken(user);
        saveToken(savedUser, jwtToken);

        return new TokenResponse(jwtToken);
    }


    @Override
    public TokenResponse login(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername()
                        , authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(authenticationRequest.getUsername()).get();
        String token = jwtTokenProvider.generateToken(user);
        setExpiredAllUserTokens(user);
        saveToken(user, token);

        return new TokenResponse(token);
    }


    private void saveToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .isExpired(false)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }

    private void setExpiredAllUserTokens(User user) {
        List<Token> allNonExpiredTokens = tokenRepository.findAllNonExpiredTokens(user.getId());
        if (allNonExpiredTokens.isEmpty()) {
            return;
        }
        allNonExpiredTokens.forEach(token -> token.setExpired(true));
        tokenRepository.saveAll(allNonExpiredTokens);
    }
}