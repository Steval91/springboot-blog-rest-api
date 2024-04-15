package dev.steve.blogrestapi.dto.post;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Builder
@AllArgsConstructor
@Data
public class PostRequest {

    @NotNull
    @Builder.Default
    private String id = "";

    private String title;

    private String body;

    private String author;
}
