package dev.steve.blogrestapi.service.post.impl;

import dev.steve.blogrestapi.dto.post.PostRequest;
import dev.steve.blogrestapi.dto.post.PostRequestCreate;
import dev.steve.blogrestapi.dto.post.PostRequestUpdate;
import dev.steve.blogrestapi.exception.CustomException;
import dev.steve.blogrestapi.model.UserRole;
import dev.steve.blogrestapi.model.entity.Post;
import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.model.repository.PostRepository;
import dev.steve.blogrestapi.model.repository.UserRepository;
import dev.steve.blogrestapi.service.post.PostService;
import dev.steve.blogrestapi.utility.ConnectedUser;
import java.security.Principal;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  private static final String RESOURCE_NOT_FOUND = "Resource Not Found";

  @Override
  public Page<Post> findAll(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  @Override
  public Page<Post> findByTitleContaining(
    String searchQuery,
    Pageable pageable
  ) {
    return postRepository.findByTitleContaining(searchQuery, pageable);
  }

  @Override
  public Post findById(Long postId) {
    return postRepository
      .findById(postId)
      .orElseThrow(() ->
        new CustomException(
          "Post with ID " + postId + " not found",
          RESOURCE_NOT_FOUND,
          HttpStatus.NOT_FOUND
        )
      );
  }

  @Override
  public void existsById(Long postId) {
    if (Boolean.FALSE.equals(postRepository.existsById(postId))) {
      throw new CustomException(
        RESOURCE_NOT_FOUND,
        "Post with ID " + postId + " not found",
        HttpStatus.NOT_FOUND
      );
    }
  }

  @Override
  public Post save(
    PostRequestCreate request,
    Principal connectedUserPrincipal
  ) {
    Long creatorId = findConnectedUser(connectedUserPrincipal).getId();

    Post post = Post
      .builder()
      .title(request.getTitle())
      .body(request.getBody())
      .author(request.getAuthor())
      .createdAt(new Date())
      .createdBy(creatorId)
      .updatedAt(new Date())
      .updatedBy(creatorId)
      .build();

    return postRepository.save(post);
  }

  @Override
  public Post update(
    PostRequestUpdate request,
    Principal connectedUserPrincipal
  ) {
    Long creatorId = findConnectedUser(connectedUserPrincipal).getId();

    Post searchedPost = this.findById(request.getId());
    searchedPost.setTitle(request.getTitle());
    searchedPost.setBody(request.getBody());
    searchedPost.setAuthor(request.getAuthor());
    searchedPost.setUpdatedAt(new Date());
    searchedPost.setUpdatedBy(creatorId);

    return postRepository.save(searchedPost);
  }

  @Override
  public Post saveOrUpdate(
    PostRequest postRequest,
    Principal connectedUserPrincipal
  ) {
    User connectedUser = findConnectedUser(connectedUserPrincipal);

    Post post = Post
      .builder()
      .title(postRequest.getTitle())
      .body(postRequest.getBody())
      .author(postRequest.getAuthor())
      .createdAt(new Date())
      .createdBy(connectedUser.getId())
      .build();

    Long id = postRequest.getId();

    if (id == null || id == 0) {
      return postRepository.save(post);
    }

    Post searchedPost = postRepository
      .findById(postRequest.getId())
      .orElseThrow(() ->
        new CustomException(
          RESOURCE_NOT_FOUND,
          "Post with ID " + id + " not found",
          HttpStatus.NOT_FOUND
        )
      );

    post.setId(searchedPost.getId());
    post.setUpdatedAt(new Date());
    post.setUpdatedBy(connectedUser.getId());

    return postRepository.save(post);
  }

  @Override
  public void delete(Long postId) {
    this.existsById(postId);

    postRepository.deleteById(postId);
  }

  public User findConnectedUser(Principal connectedUser) {
    UserDetails userDetails = (UserDetails) (
      (UsernamePasswordAuthenticationToken) connectedUser
    ).getPrincipal();

    return userRepository
      .findByEmail(userDetails.getUsername())
      .orElseThrow(() ->
        new CustomException(
          RESOURCE_NOT_FOUND,
          "User not found",
          HttpStatus.NOT_FOUND
        )
      );
  }

  public UserRole getUserRole(String role) {
    UserRole userRole;
    if (role.equals("admin")) {
      userRole = UserRole.ROLE_ADMIN;
    } else if (role.equals("writer")) {
      userRole = UserRole.ROLE_WRITER;
    } else {
      throw new CustomException(
        RESOURCE_NOT_FOUND,
        "Role not found",
        HttpStatus.BAD_REQUEST
      );
    }

    return userRole;
  }
}
