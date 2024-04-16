package dev.steve.blogrestapi.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Post's Response")
public class PostRequestUpdate {

  @Schema(description = "Post's ID")
  @NotNull(message = "ID is required")
  private Long id;

  @Schema(description = "Post's title")
  @NotBlank(message = "Title is required")
  private String title;

  @Schema(description = "Post's body")
  @NotBlank(message = "Body is required")
  private String body;

  @Schema(description = "Post's author")
  @NotBlank(message = "Author is required")
  private String author;
}
