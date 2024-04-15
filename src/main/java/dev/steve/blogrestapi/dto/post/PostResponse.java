package dev.steve.blogrestapi.dto.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@AllArgsConstructor
@Data
public class PostResponse {
    @JsonIgnore
    private String id;

    private String title;

    private String body;

    private String author;

    private Date createdAt;
    private Long createdBy;
    private Date updatedAt;
    private Long updatedBy;
}
