package dev.steve.blogrestapi.controller;

import dev.steve.blogrestapi.dto.post.PostRequest;
import dev.steve.blogrestapi.dto.post.PostResponse;
import dev.steve.blogrestapi.model.entity.Post;
import dev.steve.blogrestapi.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public Object findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Pageable pageable) {
        Pageable paging = PageRequest.of(page, size);

        return postService.findAll(paging);
    }

    @GetMapping("/{id}")
    public PostResponse findId(@PathVariable("id") Long id) {
        return postService.findById(id);
    }

    @PostMapping("/create-update")
    public PostResponse saveOrUpdate(@RequestBody PostRequest postRequest, Principal connecteUsser) {
        return postService.saveOrUpdate(postRequest, connecteUsser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
