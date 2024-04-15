package dev.steve.blogrestapi.utility;

import dev.steve.blogrestapi.dto.user.UserResponse;
import dev.steve.blogrestapi.model.entity.User;

public class RenderResponse {

  public UserResponse renderUserResponse(User user) {
    return UserResponse
      .builder()
      .id(user.getId())
      .name(user.getName())
      .email(user.getEmail())
      .createdBy(user.getCreatedBy())
      .updatedBy(user.getUpdatedBy())
      .createdAt(user.getCreatedAt())
      .updatedAt(user.getUpdatedAt())
      .build();
  }
}
