package dev.steve.blogrestapi.model.repository;

import dev.steve.blogrestapi.controller.AuthenticationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthenticationLogRepository
  extends JpaRepository<AuthenticationLog, Long> {
  @Query(
    """
            SELECT al FROM AuthenticationLog al
            INNER JOIN User u ON al.user.id = u.id
            WHERE (al.expired = FALSE OR al.revoked = FALSE)
            AND al.isRefreshToken = :isRefreshToken
            AND u.id = :userId
        """
  )
  List<AuthenticationLog> findAllValidTokensByUser(
    Long userId,
    Boolean isRefreshToken
  );

  Optional<AuthenticationLog> findByToken(String token);
}
