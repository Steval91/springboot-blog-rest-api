package dev.steve.blogrestapi.dto.user;

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
public class UserRequestChangePassword {

  @Schema(description = "Current password")
  @NotBlank(message = "Current Password is required")
  private String currentPassword;

  @Schema(description = "New password")
  @NotBlank(message = "New Password is required")
  private String newPassword;

  @Schema(description = "Confirmation password")
  @NotBlank(message = "Confirmation Password is required")
  private String confirmationPassword;
}
