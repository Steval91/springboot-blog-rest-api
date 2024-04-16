package dev.steve.blogrestapi.service.user.impl;

import dev.steve.blogrestapi.dto.user.UserRequestChangePassword;
import dev.steve.blogrestapi.dto.user.UserRequestCreate;
import dev.steve.blogrestapi.dto.user.UserRequestUpdate;
import dev.steve.blogrestapi.exception.CustomException;
import dev.steve.blogrestapi.model.UserRole;
import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.model.repository.UserRepository;
import dev.steve.blogrestapi.service.user.UserService;
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
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
  private static final String BAD_REQUEST = "Bad Request";

  @Override
  public Page<User> findAll(Pageable paging) {
    return userRepository.findAll(paging);
  }

  @Override
  public Page<User> findByEmailContaining(String searchQuery, Pageable paging) {
    return userRepository.findByEmailContaining(searchQuery, paging);
  }

  @Override
  public User findById(Long userId) {
    return userRepository
      .findById(userId)
      .orElseThrow(() ->
        new CustomException(
          "User with ID " + userId + " not found",
          RESOURCE_NOT_FOUND,
          HttpStatus.NOT_FOUND
        )
      );
  }

  @Override
  public void existsById(Long userId) {
    if (Boolean.FALSE.equals(userRepository.existsById(userId))) {
      throw new CustomException(
        RESOURCE_NOT_FOUND,
        "User with ID " + userId + " not found",
        HttpStatus.NOT_FOUND
      );
    }
  }

  @Override
  public User save(
    UserRequestCreate request,
    Principal connectedUserPrincipal,
    String action
  ) {
    if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
      throw new CustomException(
        BAD_REQUEST,
        "Email has been registered",
        HttpStatus.BAD_REQUEST
      );
    }

    UserRole userRole = getUserRole(request.getRole());

    User user = User
      .builder()
      .name(request.getName())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .role(userRole)
      .createdAt(new Date())
      .updatedAt(new Date())
      .build();

    if (action.equals("create-user")) {
      Long creatorId = findConnectedUser(connectedUserPrincipal).getId();
      user.setCreatedBy(creatorId);
      user.setUpdatedBy(creatorId);
    }

    return userRepository.save(user);
  }

  @Override
  public User update(
    UserRequestUpdate request,
    Principal connectedUserPrincipal
  ) {
    Long creatorId = findConnectedUser(connectedUserPrincipal).getId();
    UserRole userRole = getUserRole(request.getRole());

    User searchedUser = this.findById(request.getId());
    searchedUser.setName(request.getName());
    searchedUser.setEmail(request.getEmail());
    searchedUser.setRole(userRole);
    searchedUser.setUpdatedAt(new Date());
    searchedUser.setUpdatedBy(creatorId);

    return userRepository.save(searchedUser);
  }

  @Override
  public void delete(Long userId) {
    this.existsById(userId);

    userRepository.deleteById(userId);
  }

  @Override
  public void changePassword(
    UserRequestChangePassword request,
    Principal connectedUserPrincipal
  ) {
    User user = findConnectedUser(connectedUserPrincipal);

    if (
      !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())
    ) {
      throw new CustomException(
        BAD_REQUEST,
        "Wrong Current Password",
        HttpStatus.BAD_REQUEST
      );
    }

    if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
      throw new CustomException(
        BAD_REQUEST,
        "New Password does not match with Confirmation Password",
        HttpStatus.BAD_REQUEST
      );
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
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
