package dev.steve.blogrestapi.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User's Response")
public class UserResponse {

  @Schema(description = "User's ID")
  private Long id;

  @Schema(description = "User's Name")
  private String name;

  @Schema(description = "User's Email")
  private String email;

  @Schema(description = "User's Role")
  private String role;

  @Schema(description = "Created By")
  private Long createdBy;

  @Schema(description = "Updated By")
  private Long updatedBy;

  @Schema(description = "Created Date")
  private Date createdAt;

  @Schema(description = "Updated Date")
  private Date updatedAt;
}
