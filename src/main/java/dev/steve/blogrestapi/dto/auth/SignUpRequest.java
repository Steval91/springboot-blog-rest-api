package dev.steve.blogrestapi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

  @Schema(description = "User's email")
  @Email
  @NotBlank(message = "Email is required")
  private String email;

  @Schema(description = "User's password")
  @NotBlank(message = "Password is required")
  @Min(value = 8, message = "Password must have at least 8 characters long")
  private String password;
}
