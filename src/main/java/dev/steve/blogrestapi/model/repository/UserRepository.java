package dev.steve.blogrestapi.model.repository;

import dev.steve.blogrestapi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Page<User> findByEmailContaining(String searchQuery, Pageable paging);
  Optional<User> findByEmail(String email);
  Boolean existsByEmail(String email);
}
