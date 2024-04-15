package dev.steve.blogrestapi;

//USing BDD Mockito
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import dev.steve.blogrestapi.dto.post.PostRequest;
import dev.steve.blogrestapi.model.entity.Post;
import dev.steve.blogrestapi.model.repository.PostRepository;
import dev.steve.blogrestapi.service.post.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.mockito.ArgumentMatchers.any;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
public class PostsTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Test
    void savedPost(){
        Post post = Post.builder()
                .title("Title 1")
                .body("Body 1")
                .author("Author 1")
                .createdBy(1L)
                .createdAt(new Date())
                .updatedAt(new Date())
                .updatedBy(1L)
                .build();

        Post savePost = postRepository.save(post);
        Assertions.assertThat(savePost).isNotNull();
        Assertions.assertThat(savePost.getId()).isGreaterThan(0);
    }

    @Test
    void getAllPost() {
        //given
        Post post = new Post(null, "Test Title 1", "Test Body 1", "author 1", new Date(), 1L, new Date(), 1L);
        Post post2 = new Post(null, "Test Title 2", "Test Body 2", "author 2", new Date(), 1L, new Date(), 1L);

        // Mocking
        Page<Post> page = new PageImpl<>(List.of(post, post2));
        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);

        //When
        Pageable paging = PageRequest.of(0, 10);
        Page<Post> postList = postService.findAll(paging);

        //Then
        assertThat(postList).isNotNull();
        assertThat(postList.getTotalElements()).isEqualTo(2);
    }
}
