package dev.steve.blogrestapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

  @Schema(description = "User's email")
  @NotBlank(message = "Email is required")
  private String email;

  @Schema(description = "User's password")
  @NotBlank(message = "Password is required")
  private String password;
}
