package dev.steve.blogrestapi.service.auth;

import dev.steve.blogrestapi.dto.auth.AuthenticationResponse;
import dev.steve.blogrestapi.dto.auth.SignInRequest;
import dev.steve.blogrestapi.dto.user.UserRequestCreate;
import dev.steve.blogrestapi.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Principal;

public interface AuthenticationService {
  User signUp(UserRequestCreate request, Principal connectedUserPrincipal);
  AuthenticationResponse signIn(SignInRequest request);
  void revokeAllUserTokens(User user, Boolean isRefreshToken);
  void saveToken(User user, String token, Boolean isRefreshToken);
  AuthenticationResponse refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  );
}
