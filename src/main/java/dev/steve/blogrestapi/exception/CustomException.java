package dev.steve.blogrestapi.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CustomException extends RuntimeException {

  private String title;
  private HttpStatus status;

  public CustomException(String message) {
    super(message);
  }

  public CustomException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public CustomException(String title, String message, HttpStatus status) {
    super(message);
    this.title = title;
    this.status = status;
  }
}
