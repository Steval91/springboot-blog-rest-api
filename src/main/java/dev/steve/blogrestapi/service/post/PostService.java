package dev.steve.blogrestapi.service.post;

import dev.steve.blogrestapi.dto.post.PostRequest;
import dev.steve.blogrestapi.dto.post.PostRequestCreate;
import dev.steve.blogrestapi.dto.post.PostRequestUpdate;
import dev.steve.blogrestapi.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface PostService {
  Page<Post> findAll(Pageable pageable);
  Page<Post> findByTitleContaining(String searchQuery, Pageable pageable);
  Post findById(Long postId);
  void existsById(Long postId);
  Post save(
    PostRequestCreate request,
    Principal connectedUserPrincipal
  );
  Post update(PostRequestUpdate request, Principal connectedUserPrincipal);
  Post saveOrUpdate(PostRequest postRequest, Principal connectedUser);
  void delete(Long postId);
}
