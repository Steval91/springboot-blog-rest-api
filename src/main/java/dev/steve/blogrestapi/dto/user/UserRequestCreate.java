package dev.steve.blogrestapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User's Create Request")
public class UserRequestCreate {

  @Schema(description = "User's name")
  @NotBlank(message = "Name is required")
  private String name;

  @Schema(description = "User's email")
  @Email(message = "Must be well-formed email address")
  @NotBlank(message = "Email is required")
  private String email;

  @Schema(description = "User's password")
  @NotBlank(message = "Password is required")
  private String password;

  @Schema(description = "User's role")
  @NotBlank(message = "Role is required")
  private String role;
}
