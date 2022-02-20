package com.example.service.security;

import static com.example.util.MessageCodeUtils.USER_NOT_FOUND;

import com.example.entity.User;
import com.example.exception.ObjectNotFoundException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SecurityService {

  private final UserRepository userRepository;

  public User getPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String currentPrincipalName = authentication.getName();

    return userRepository.findByUsernameAndDeletedDateNull(currentPrincipalName)
        .orElseThrow(() -> new ObjectNotFoundException(USER_NOT_FOUND, currentPrincipalName));
  }
}
