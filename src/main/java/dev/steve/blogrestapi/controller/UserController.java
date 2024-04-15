package dev.steve.blogrestapi.controller;

import dev.steve.blogrestapi.dto.user.UserRequestChangePassword;
import dev.steve.blogrestapi.dto.user.UserRequestCreate;
import dev.steve.blogrestapi.dto.user.UserRequestUpdate;
import dev.steve.blogrestapi.dto.user.UserResponse;
import dev.steve.blogrestapi.model.entity.User;

import dev.steve.blogrestapi.service.user.UserService;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Tag(name = "Users", description = "User API's end points")
@RestController
@RequestMapping("/api/v1/users")
class UserController {

  private final RenderResponse renderResponse;
  private final UserService userService;
  private final WebRequest webRequest;

  @Operation(
    summary = "Retrieve all users",
    description = "Retrieve all users or users containing specific name"
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = UserResponse.class),
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
  @GetMapping
  PaginationResponse<Object> findAll(
    @RequestParam(required = false) String name,
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

    Page<User> userPage;
    if (name != null) userPage =
      userService.findByEmailContaining(name, paging); else userPage =
      userService.findAll(paging);

    List<UserResponse> userResponse = userPage
      .stream()
      .map(renderResponse::renderUserResponse)
      .collect(Collectors.toList());

    return new PaginationResponse<>(
      userPage.getNumber(),
      userPage.getNumberOfElements(),
      userPage.getTotalElements(),
      userPage.getTotalPages(),
      userPage.isFirst(),
      userPage.isLast(),
      userPage.hasNext(),
      userPage.hasPrevious(),
      userResponse
    );
  }

  @Operation(
    summary = "Retrieve a user by ID",
    description = "Get a user object by specifying its ID."
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = UserResponse.class),
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
  @GetMapping("/{userId}")
  UserResponse findById(@PathVariable("userId") Long userId) {
    User searchedUser = userService.findById(userId);
    return renderResponse.renderUserResponse(searchedUser);
  }

  @Operation(summary = "Create User", description = "Create a user.")
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
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  CustomApiResponse<Object> create(
    @Valid @RequestBody UserRequestCreate request,
    Principal connectedUserPrincipal
  ) {
    User createdUser = userService.save(
      request,
      connectedUserPrincipal,
      "create-user"
    );
    UserResponse response = renderResponse.renderUserResponse(createdUser);

    return new CustomApiResponse<>(
      HttpStatus.CREATED,
      "User created successfully",
      response,
      webRequest.getDescription(false)
    );
  }

  @Operation(
    summary = "Update User by ID",
    description = "Update a user by specifying its ID."
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
  @PutMapping
  CustomApiResponse<Object> update(
    @Valid @RequestBody UserRequestUpdate request,
    Principal connectedUserPrincipal
  ) {
    User updatedUser = userService.update(request, connectedUserPrincipal);
    UserResponse response = renderResponse.renderUserResponse(updatedUser);

    return new CustomApiResponse<>(
      HttpStatus.OK,
      "User updated successfully",
      response,
      webRequest.getDescription(false)
    );
  }

  @Operation(
    summary = "Delete User by ID",
    description = "Delete a user by specifying its ID."
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
  @DeleteMapping("/{userId}")
  CustomApiResponse<Object> delete(@PathVariable("userId") Long userId) {
    userService.delete(userId);

    return new CustomApiResponse<>(
      HttpStatus.OK,
      "User deleted successfully",
      "User with ID " + userId + " deleted successfully",
      webRequest.getDescription(false)
    );
  }

  @Operation(
    summary = "Change Password",
    description = "Change user's password."
  )
  @ApiResponse(
    responseCode = "200",
    description = "OK",
    content = {
      @Content(
        schema = @Schema(implementation = CustomApiResponse.class),
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
  @PutMapping("/change-password")
  CustomApiResponse<Object> changePassword(
    @Valid @RequestBody UserRequestChangePassword request,
    Principal connectedUser
  ) {
    userService.changePassword(request, connectedUser);

    return new CustomApiResponse<>(
      HttpStatus.OK,
      "Password updated successfully",
      "Your password updated successfully",
      webRequest.getDescription(false)
    );
  }
}
