package dev.steve.blogrestapi.utility;

import dev.steve.blogrestapi.dto.post.PostResponse;
import dev.steve.blogrestapi.dto.user.UserResponse;
import dev.steve.blogrestapi.model.UserRole;
import dev.steve.blogrestapi.model.entity.Post;
import dev.steve.blogrestapi.model.entity.User;

public class RenderResponse {

  public UserResponse renderUserResponse(User user) {
    String userRole = user.getRole() == UserRole.ROLE_ADMIN ? "admin" : "writer";

    return UserResponse
      .builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .role(userRole)
      .createdBy(user.getCreatedBy())
      .updatedBy(user.getUpdatedBy())
      .createdAt(user.getCreatedAt())
      .updatedAt(user.getUpdatedAt())
      .build();
  }

  public PostResponse renderPostResponse(Post post) {
    return PostResponse
      .builder()
      .id(post.getId())
      .title(post.getTitle())
      .body(post.getBody())
      .author(post.getAuthor())
      .createdBy(post.getCreatedBy())
      .updatedBy(post.getUpdatedBy())
      .createdAt(post.getCreatedAt())
      .updatedAt(post.getUpdatedAt())
      .build();
  }
}
