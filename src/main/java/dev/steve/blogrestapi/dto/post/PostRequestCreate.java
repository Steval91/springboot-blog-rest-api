package dev.steve.blogrestapi.dto.post;

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
public class PostRequestCreate {

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
