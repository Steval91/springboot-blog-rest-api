package dev.steve.blogrestapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User's Response")
public class UserRequestUpdate {

  @Schema(description = "User's ID")
  @NotNull(message = "ID is required")
  private Long id;

  @Schema(description = "User's name")
  @NotBlank(message = "Name is required")
  private String name;

  @Schema(description = "User's email")
  @Email
  @NotBlank(message = "Email is required")
  private String email;
}
