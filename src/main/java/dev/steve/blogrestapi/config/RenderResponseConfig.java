package dev.steve.blogrestapi.config;

import dev.steve.blogrestapi.utility.RenderResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RenderResponseConfig {

  @Bean
  RenderResponse renderResponse() {
    return new RenderResponse();
  }
}
