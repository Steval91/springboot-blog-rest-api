package dev.steve.blogrestapi.utility;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomApiResponse<T> {

  private T status;
  private String message;
  private T details;
  private String description;
}
