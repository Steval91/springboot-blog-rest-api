package dev.steve.blogrestapi.service.post;

import dev.steve.blogrestapi.dto.post.PostRequest;
import dev.steve.blogrestapi.dto.post.PostResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface PostService {
    Page findAll(Pageable pageable);
    PostResponse findById(Long id);
    PostResponse saveOrUpdate(PostRequest postRequest, Principal connectedUser);

    void delete(Long id);
}
