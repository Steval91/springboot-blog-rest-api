package dev.steve.blogrestapi.model.entity;

import dev.steve.blogrestapi.helper.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"") // Escape the table name
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(columnDefinition = "VARCHAR(100)", nullable = false)
  private String password;

  private Long createdBy;
  private Long updatedBy;
  private Date createdAt;
  private Date updatedAt;
}
