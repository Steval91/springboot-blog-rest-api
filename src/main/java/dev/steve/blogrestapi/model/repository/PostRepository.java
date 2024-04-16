package dev.steve.blogrestapi.model.repository;

import dev.steve.blogrestapi.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContaining(String searchQuery, Pageable paging);
    Optional<Post> findByTitle(String title);
}
