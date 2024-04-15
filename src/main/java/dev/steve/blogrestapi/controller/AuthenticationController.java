package dev.steve.blogrestapi.controller;

import dev.steve.blogrestapi.dto.auth.AuthenticationResponse;
import dev.steve.blogrestapi.dto.auth.SignInRequest;
import dev.steve.blogrestapi.dto.user.UserRequestCreate;
import dev.steve.blogrestapi.dto.user.UserResponse;
import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.service.auth.AuthenticationService;
import dev.steve.blogrestapi.utility.CustomApiResponse;
import dev.steve.blogrestapi.utility.RenderResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final WebRequest webRequest;
  private final RenderResponse renderResponse;

  private static final String APPLICATION_JSON = "application/json";

  @PostMapping(
    path = "/sign-up",
    consumes = APPLICATION_JSON,
    produces = APPLICATION_JSON
  )
  public CustomApiResponse<Object> signUp(
    @Valid @RequestBody UserRequestCreate request,
    Principal connectedUserPrincipal
  ) {
    User savedUser = authenticationService.signUp(
      request,
      connectedUserPrincipal
    );
    UserResponse response = renderResponse.renderUserResponse(savedUser);

    return new CustomApiResponse<>(
      HttpStatus.CREATED,
      "Sign up success",
      response,
      webRequest.getDescription(false)
    );
  }

  @PostMapping(
    path = "/sign-in",
    consumes = APPLICATION_JSON,
    produces = APPLICATION_JSON
  )
  public AuthenticationResponse signIn(
    @Valid @RequestBody SignInRequest request
  ) {
    return authenticationService.signIn(request);
  }

  @PostMapping(path = "/refresh-token", produces = APPLICATION_JSON)
  public AuthenticationResponse refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    return authenticationService.refreshToken(request, response);
  }
}
