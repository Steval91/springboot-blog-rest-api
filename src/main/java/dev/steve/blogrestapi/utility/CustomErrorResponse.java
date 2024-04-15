package dev.steve.blogrestapi.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomErrorResponse<T> {

  private T timestamp;
  private int status;
  private String error;
  private T message;
  private String description;
}
