package dev.steve.blogrestapi.service.post.impl;

import dev.steve.blogrestapi.dto.post.PostRequest;
import dev.steve.blogrestapi.dto.post.PostResponse;
import dev.steve.blogrestapi.exception.CustomException;
import dev.steve.blogrestapi.model.entity.Post;
import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.model.repository.PostRepository;
import dev.steve.blogrestapi.model.repository.UserRepository;
import dev.steve.blogrestapi.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    private static final String BAD_REQUEST = "Bad Request";

    @Override
    public Page findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id).get();
        return entityToDto(post);
    }

    @Override
    public PostResponse saveOrUpdate(PostRequest postRequest, Principal connectedUser) {
        User user = findConnectedUser(connectedUser);
        Long id = postRequest.getId() == null ? 0 : Long.parseLong(postRequest.getId());
        if (id == 0) {
            Post newPost = Post.builder()
                    .title(postRequest.getTitle())
                    .body(postRequest.getBody())
                    .author(postRequest.getAuthor())
                    .createdAt(new Date())
                    .createdBy(user.getId())
                    .updatedAt(new Date())
                    .updatedBy(user.getId())
                    .build();
            postRepository.save(newPost);
            return entityToDto(newPost);
        }

        Optional<Post> post = postRepository.findById(Long.valueOf(postRequest.getId()));
        if (post.isPresent()) {
            Post newPost = Post.builder()
                    .id(post.get().getId())
                    .title(postRequest.getTitle())
                    .body(postRequest.getBody())
                    .author(postRequest.getAuthor())
                    .updatedAt(new Date())
                    .updatedBy(user.getId())
                    .build();
            postRepository.save(newPost);
            return entityToDto(newPost);
        } else {
            throw new CustomException(
                    RESOURCE_NOT_FOUND,
                    "Post with ID " + id + " not found",
                    HttpStatus.NOT_FOUND
            );
        }

    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public PostResponse entityToDto(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .body(post.getBody())
                .author(post.getAuthor())
                .id(String.valueOf(post.getId()))
                .createdAt(post.getCreatedAt())
                .createdBy(post.getCreatedBy())
                .updatedAt(post.getUpdatedAt())
                .updatedBy(post.getUpdatedBy())
                .build();
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
}
