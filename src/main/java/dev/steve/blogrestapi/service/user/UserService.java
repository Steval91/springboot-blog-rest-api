package dev.steve.blogrestapi.service.user;

import dev.steve.blogrestapi.dto.user.UserRequestChangePassword;
import dev.steve.blogrestapi.dto.user.UserRequestCreate;
import dev.steve.blogrestapi.dto.user.UserRequestUpdate;
import dev.steve.blogrestapi.helper.UserRole;
import dev.steve.blogrestapi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface UserService {
  Page<User> findAll(Pageable paging);
  Page<User> findByEmailContaining(String email, Pageable paging);
  User findById(Long userId);
  void existsById(Long userId);
  UserRole getUserRole(String role);
  User save(
    UserRequestCreate request,
    Principal connectedUserPrincipal,
    String action
  );
  User update(UserRequestUpdate request, Principal connectedUserPrincipal);
  void delete(Long userId);
  User findConnectedUser(Principal connectedUserPrincipal);
  void changePassword(
    UserRequestChangePassword request,
    Principal connectedUserPrincipal
  );
}
