package dev.steve.blogrestapi.controller;

import dev.steve.blogrestapi.dto.post.PostRequestCreate;
import dev.steve.blogrestapi.dto.post.PostRequestUpdate;
import dev.steve.blogrestapi.dto.post.PostResponse;
import dev.steve.blogrestapi.model.entity.Post;
import dev.steve.blogrestapi.service.post.PostService;
import dev.steve.blogrestapi.utility.CustomApiResponse;
import dev.steve.blogrestapi.utility.CustomErrorResponse;
import dev.steve.blogrestapi.utility.PaginationResponse;
import dev.steve.blogrestapi.utility.RenderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post API's end points")
@RestController
@RequestMapping("/api/v1/posts")
class PostController {

  private final RenderResponse renderResponse;
  private final PostService postService;
  private final WebRequest webRequest;

  @Operation(
    summary = "Retrieve all posts",
    description = "Retrieve all posts or posts containing specific name"
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = PostResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "404",
    description = "Not Found",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "415",
    description = "Unsupported Media Type",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @PreAuthorize("hasAuthority('ROLE_WRITER')")
  @GetMapping
  PaginationResponse<Object> findAll(
    @RequestParam(required = false) String title,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestParam(defaultValue = "id,desc") String[] sort
  ) {
    List<Order> orders = new ArrayList<>();
    if (sort[0].contains(",")) {
      for (String sortString : sort) {
        String[] sortArray = sortString.split(",");
        orders.add(
          new Order(Sort.Direction.fromString(sortArray[1]), sortArray[0])
        );
      }
    }
    orders.add(new Order(Sort.Direction.fromString(sort[1]), sort[0]));

    Pageable paging = PageRequest.of(page, size, Sort.by(orders));

    Page<Post> postPage;
    if (title != null) {
      postPage = postService.findByTitleContaining(title, paging);
    } else {
      postPage = postService.findAll(paging);
    }

    List<PostResponse> postResponse = postPage
      .stream()
      .map(renderResponse::renderPostResponse)
      .collect(Collectors.toList());

    return new PaginationResponse<>(
      postPage.getNumber(),
      postPage.getNumberOfElements(),
      postPage.getTotalElements(),
      postPage.getTotalPages(),
      postPage.isFirst(),
      postPage.isLast(),
      postPage.hasNext(),
      postPage.hasPrevious(),
      postResponse
    );
  }

  @Operation(
    summary = "Retrieve a post by ID",
    description = "Get a post object by specifying its ID."
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = PostResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "404",
    description = "Not Found",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "415",
    description = "Unsupported Media Type",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @PreAuthorize("hasAuthority('ROLE_WRITER')")
  @GetMapping("/{postId}")
  PostResponse findById(@PathVariable("postId") Long postId) {
    Post searchedPost = postService.findById(postId);
    return renderResponse.renderPostResponse(searchedPost);
  }

  @Operation(summary = "Create Post", description = "Create a post.")
  @ApiResponse(
    responseCode = "201",
    description = "Created",
    content = {
      @Content(
        schema = @Schema(implementation = ApiResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "404",
    description = "Not Found",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "415",
    description = "Unsupported Media Type",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @PreAuthorize("hasAuthority('ROLE_WRITER')")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  CustomApiResponse<Object> create(
    @Valid @RequestBody PostRequestCreate request,
    Principal connectedPostPrincipal
  ) {
    Post createdPost = postService.save(request, connectedPostPrincipal);
    PostResponse response = renderResponse.renderPostResponse(createdPost);

    return new CustomApiResponse<>(
      HttpStatus.CREATED,
      "Post created successfully",
      response,
      webRequest.getDescription(false)
    );
  }

  @Operation(
    summary = "Update Post by ID",
    description = "Update a post by specifying its ID."
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = ApiResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "404",
    description = "Not Found",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "415",
    description = "Unsupported Media Type",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @PreAuthorize("hasAuthority('ROLE_WRITER')")
  @PutMapping
  CustomApiResponse<Object> update(
    @Valid @RequestBody PostRequestUpdate request,
    Principal connectedUserPrincipal
  ) {
    Post updatedPost = postService.update(request, connectedUserPrincipal);
    PostResponse response = renderResponse.renderPostResponse(updatedPost);

    return new CustomApiResponse<>(
      HttpStatus.OK,
      "Post updated successfully",
      response,
      webRequest.getDescription(false)
    );
  }

  @Operation(
    summary = "Delete Post by ID",
    description = "Delete a post by specifying its ID."
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = ApiResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "400",
    description = "Bad Request",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "404",
    description = "Not Found",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "415",
    description = "Unsupported Media Type",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @ApiResponse(
    responseCode = "500",
    description = "Internal Server Error",
    content = {
      @Content(
        schema = @Schema(implementation = CustomErrorResponse.class),
        mediaType = "application/json"
      ),
    }
  )
  @PreAuthorize("hasAuthority('ROLE_WRITER')")
  @DeleteMapping("/{postId}")
  CustomApiResponse<Object> delete(@PathVariable("postId") Long postId) {
    postService.delete(postId);

    return new CustomApiResponse<>(
      HttpStatus.OK,
      "Post deleted successfully",
      "Post with ID " + postId + " deleted successfully",
      webRequest.getDescription(false)
    );
  }
}
