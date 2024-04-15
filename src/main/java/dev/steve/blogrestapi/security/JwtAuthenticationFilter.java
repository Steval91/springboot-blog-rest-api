package dev.steve.blogrestapi.security;

import dev.steve.blogrestapi.controller.AuthenticationLog;
import dev.steve.blogrestapi.exception.CustomException;
import dev.steve.blogrestapi.model.repository.AuthenticationLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(
    JwtAuthenticationFilter.class
  );

  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationLogRepository authenticationLogRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.error("Unauthorized request");
      filterChain.doFilter(request, response);
      return;
    }

    token = authHeader.substring(7);
    username = jwtService.extractUsername(token);

    if (
      username != null &&
      SecurityContextHolder.getContext().getAuthentication() == null
    ) {
      AuthenticationLog searchedToken = authenticationLogRepository
        .findByToken(token)
        .orElseThrow(() ->
          new CustomException(
            "Resource not found",
            "Token Not Found",
            HttpStatus.NOT_FOUND
          )
        );

      Boolean isSearchedTokenValid =
        !searchedToken.getExpired() && !searchedToken.getRevoked();
      if (!isSearchedTokenValid) throw new CustomException(
        "JWT Expired",
        "Access token has expired or revoked",
        HttpStatus.BAD_REQUEST
      );

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (jwtService.isTokenValid(token, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        authenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder
          .getContext()
          .setAuthentication(authenticationToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
