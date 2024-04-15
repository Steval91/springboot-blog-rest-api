package dev.steve.blogrestapi.controller;

import dev.steve.blogrestapi.helper.TokenType;
import dev.steve.blogrestapi.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuthenticationLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  private Boolean isRefreshToken;

  private Boolean expired;

  private Boolean revoked;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
