package dev.steve.blogrestapi.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  @Schema(description = "Post's ID")
  private Long id;

  @Schema(description = "Post's Title")
  private String title;

  @Schema(description = "Post's Body")
  private String body;

  @Schema(description = "Post's Author")
  private String author;

  @Schema(description = "Created By")
  private Long createdBy;

  @Schema(description = "Updated By")
  private Long updatedBy;

  @Schema(description = "Created Date")
  private Date createdAt;

  @Schema(description = "Updated Date")
  private Date updatedAt;
}
