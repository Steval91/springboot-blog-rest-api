package dev.steve.blogrestapi.security;

import dev.steve.blogrestapi.model.entity.User;
import dev.steve.blogrestapi.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    Optional<User> searchedUser = userRepository.findByEmail(username);
    return searchedUser
      .map(UserDetailsImpl::new)
      .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
