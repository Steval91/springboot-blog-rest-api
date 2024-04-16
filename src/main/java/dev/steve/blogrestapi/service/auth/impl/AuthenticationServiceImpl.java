package dev.steve.blogrestapi.service.auth.impl;

import dev.steve.blogrestapi.controller.AuthenticationLog;
import dev.steve.blogrestapi.dto.auth.AuthenticationResponse;
import dev.steve.blogrestapi.dto.auth.SignInRequest;
import dev.steve.blogrestapi.dto.user.UserRequestCreate;
import dev.steve.blogrestapi.exception.CustomException;
import dev.steve.blogrestapi.model.TokenType;
import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.model.repository.AuthenticationLogRepository;
import dev.steve.blogrestapi.service.user.impl.UserServiceImpl;
import dev.steve.blogrestapi.model.repository.UserRepository;
import dev.steve.blogrestapi.security.JwtService;
import dev.steve.blogrestapi.security.UserDetailsServiceImpl;
import dev.steve.blogrestapi.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final AuthenticationLogRepository authenticationLogRepository;
  private final UserServiceImpl userService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsServiceImpl userDetailsService;
  private final JwtService jwtService;

  private static final Logger log = LoggerFactory.getLogger(
    AuthenticationServiceImpl.class
  );
  private static final String BAD_REQUEST = "Bad Request";

  @Override
  public User signUp(
    UserRequestCreate request,
    Principal connectedUserPrincipal
  ) {
    return userService.save(request, connectedUserPrincipal, "sign-up");
  }

  @Override
  public AuthenticationResponse signIn(SignInRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );
    log.info("Finish authenticate user");

    User searchedUser = userRepository
      .findByEmail(request.getEmail())
      .orElseThrow(() ->
        new CustomException(
          "Resource Not Found",
          "User not found",
          HttpStatus.NOT_FOUND
        )
      );
    UserDetails userDetails = userDetailsService.loadUserByUsername(
      request.getEmail()
    );

    String accessToken = jwtService.generateToken(userDetails);
    this.revokeAllUserTokens(searchedUser, false);
    this.saveToken(searchedUser, accessToken, false);

    String refreshToken = jwtService.generateRefreshToken(userDetails);
    this.revokeAllUserTokens(searchedUser, true);
    this.saveToken(searchedUser, refreshToken, true);

    return AuthenticationResponse
      .builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

  @Override
  public void revokeAllUserTokens(User user, Boolean isRefreshToken) {
    List<AuthenticationLog> validUserTokens = authenticationLogRepository.findAllValidTokensByUser(
      user.getId(),
      isRefreshToken
    );

    if (validUserTokens.isEmpty()) return;

    validUserTokens.forEach(t -> {
      t.setExpired(true);
      t.setRevoked(true);
    });

    authenticationLogRepository.saveAll(validUserTokens);
  }

  @Override
  public void saveToken(User user, String jwtToken, Boolean isRefreshToken) {
    AuthenticationLog token = AuthenticationLog
      .builder()
      .token(jwtToken)
      .tokenType(TokenType.BEARER)
      .user(user)
      .isRefreshToken(isRefreshToken)
      .expired(false)
      .revoked(false)
      .build();
    authenticationLogRepository.save(token);
  }

  @Override
  public AuthenticationResponse refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String refreshToken = null;
    String username = null;
    AuthenticationResponse authenticationResponse = null;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.error("Unauthorized request");
      throw new CustomException(
        BAD_REQUEST,
        "Provide refresh token in request header",
        HttpStatus.BAD_REQUEST
      );
    }

    refreshToken = authHeader.substring(7);
    username = jwtService.extractUsername(refreshToken);

    if (username != null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (jwtService.isTokenValid(refreshToken, userDetails)) {
        AuthenticationLog storedRefreshToken = authenticationLogRepository
          .findByToken(refreshToken)
          .orElseThrow(() ->
            new CustomException(
              "Token Not Found",
              "Refresh token not found",
              HttpStatus.NOT_FOUND
            )
          );

        Boolean isStoredRefreshTokenValid =
          !storedRefreshToken.getExpired() && !storedRefreshToken.getRevoked();
        if (!isStoredRefreshTokenValid) throw new CustomException(
          "JWT Expired",
          "Refresh token has expired or revoked",
          HttpStatus.BAD_REQUEST
        );

        User searchedUser = userRepository
          .findByEmail(username)
          .orElseThrow(() ->
            new CustomException(
              "Resource Not Found",
              "User not found",
              HttpStatus.NOT_FOUND
            )
          );

        String accessToken = jwtService.generateToken(userDetails);
        this.revokeAllUserTokens(searchedUser, false);
        this.saveToken(searchedUser, accessToken, false);

        authenticationResponse =
          AuthenticationResponse
            .builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
      }
    }

    return authenticationResponse;
  }
}
