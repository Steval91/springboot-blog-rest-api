package dev.steve.blogrestapi.security;

import dev.steve.blogrestapi.controller.AuthenticationLog;
import dev.steve.blogrestapi.exception.CustomException;
import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.model.repository.AuthenticationLogRepository;
import dev.steve.blogrestapi.model.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {

  private final AuthenticationLogRepository tokenRepository;
  private final UserRepository userRepository;
  private final JwtService jwtService;

  @Override
  public void logout(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  ) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String jwt = null;
    String username = null;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new CustomException(
        "Provide access token in request header",
        "Bad Request",
        HttpStatus.BAD_REQUEST
      );
    }

    jwt = authHeader.substring(7);

    AuthenticationLog storedToken = tokenRepository
      .findByToken(jwt)
      .orElseThrow(() ->
        new CustomException(
          "Token not found",
          "Resource Not Found",
          HttpStatus.NOT_FOUND
        )
      );

    Boolean isStoredTokenValid =
      !storedToken.getExpired() && !storedToken.getRevoked();
    if (!isStoredTokenValid) throw new CustomException(
      "Access token has expired or revoked",
      "JWT Expired",
      HttpStatus.BAD_REQUEST
    );

    username = jwtService.extractUsername(jwt);

    User connectedUser = userRepository
      .findByEmail(username)
      .orElseThrow(() ->
        new CustomException(
          "User not found",
          "Resource Not Found",
          HttpStatus.NOT_FOUND
        )
      );

    List<AuthenticationLog> validUserAccessTokens = tokenRepository.findAllValidTokensByUser(
      connectedUser.getId(),
      false
    );
    validUserAccessTokens.forEach(t -> {
      t.setExpired(true);
      t.setRevoked(true);
    });
    tokenRepository.saveAll(validUserAccessTokens);

    List<AuthenticationLog> validUserRefreshTokens = tokenRepository.findAllValidTokensByUser(
      connectedUser.getId(),
      true
    );
    validUserRefreshTokens.forEach(t -> {
      t.setExpired(true);
      t.setRevoked(true);
    });
    tokenRepository.saveAll(validUserRefreshTokens);
  }
}
