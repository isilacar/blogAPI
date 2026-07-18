package com.scalefocus.userservice;

import com.scalefocus.userservice.dto.UserDto;
import com.scalefocus.userservice.entity.Token;
import com.scalefocus.userservice.entity.User;
import com.scalefocus.userservice.repository.TokenRepository;
import com.scalefocus.userservice.repository.UserRepository;
import com.scalefocus.userservice.request.AuthenticationRequest;
import com.scalefocus.userservice.request.RegisterRequest;
import com.scalefocus.userservice.response.TokenResponse;
import com.scalefocus.userservice.security.JwtTokenProvider;
import com.scalefocus.userservice.service.impl.UserServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserServiceApplicationTests extends AbstractMysqlContainer {

    @LocalServerPort
    private int portNumber;

    private String baseUrl;
    private HttpHeaders headers;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TestRestTemplate testRestTemplate;
    private User user;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + portNumber + "/api/users";
        user = userRepository.findById(3L).get();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(user);
        Token token = new Token();
        token.setUser(user);
        token.setExpired(false);
        token.setToken(jwtToken);

        tokenRepository.save(token);

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getToken());

    }

    @AfterEach
    public void tearDown() {
        tokenRepository.deleteAll();
    }


    @Test
    public void testAuthenticateUser() {
        String authenticationUserUrl = baseUrl + "/authenticate";

        RegisterRequest registerRequest = new RegisterRequest("test user", "test password", "test display name");
        userServiceImpl.register(registerRequest);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(registerRequest.getUsername(), registerRequest.getPassword());
        ResponseEntity<TokenResponse> returnedToken =
                testRestTemplate.postForEntity(authenticationUserUrl, authenticationRequest, TokenResponse.class);

        String[] tokenPart = returnedToken.getBody().getToken().split("\\.");

        assertThat(returnedToken).isNotNull();
        assertEquals(returnedToken.getStatusCode(), HttpStatus.valueOf(200));
        assertThat(tokenPart.length).isEqualTo(3);

    }

    @Test
    public void testRegisterUser() {
        String registerUserUrl = baseUrl + "/register";
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "displayName");
        ResponseEntity<TokenResponse> token = testRestTemplate
                .postForEntity(registerUserUrl, registerRequest, TokenResponse.class);

        String[] tokenPart = token.getBody().getToken().split("\\.");

        assertNotNull(token);
        assertEquals(token.getStatusCode(), HttpStatus.valueOf(201));
        assertThat(tokenPart.length).isEqualTo(3);
    }

    @Test
    public void testGettingUserDetails() {
        String getUserDetailsUrl = baseUrl + "/getUserDetails";
        ResponseEntity<UserDto> foundedUser = testRestTemplate.exchange(getUserDetailsUrl, HttpMethod.GET, new HttpEntity<>(null, headers), UserDto.class);

        assertNotNull(foundedUser);
        assertEquals(foundedUser.getStatusCode(), HttpStatus.valueOf(200));

    }

    @Test
    public void testFindUserById() {
        String findUserByIdUrl = baseUrl + "/{userId}";
        ResponseEntity<UserDto> foundedUser = testRestTemplate.exchange(findUserByIdUrl, HttpMethod.GET, new HttpEntity<>(null, headers), UserDto.class, user.getId());

        assertNotNull(foundedUser);
        assertEquals(foundedUser.getStatusCode(), HttpStatus.valueOf(200));

    }
}
