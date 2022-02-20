package com.example.validators.user;

import static com.example.util.MessageCodeUtils.ADMIN_DELETE_ERROR;
import static com.example.util.MessageCodeUtils.USERNAME_NON_UNIQUE;
import static com.example.util.MessageCodeUtils.USER_NOT_FOUND;
import static com.example.util.MessageCodeUtils.USER_OLD_PASSWORD_WRONG;
import static com.example.util.StringUtils.ADMIN_USERNAME;

import com.example.dto.request.user.UserCreateRequestDto;
import com.example.dto.request.user.UserResetPasswordRequestDto;
import com.example.entity.User;
import com.example.exception.NonUniqueObjectException;
import com.example.exception.ObjectInvalidStateException;
import com.example.exception.ObjectNotFoundException;
import com.example.exception.UnsupportedOperationException;
import com.example.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidatorService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void validateForCreate(UserCreateRequestDto dto) {
    Optional<User> optional = userRepository.findByUsernameAndDeletedDateNull(dto.getUsername());

    if (optional.isPresent()) {
      throw new NonUniqueObjectException(USERNAME_NON_UNIQUE, dto.getUsername());
    }
  }

  public void validateForUpdate(Long id, User user) {
    checkUserExists(id, user);
    isUserAdmin(user.getUsername());
  }

  public void validateForResetPassword(UserResetPasswordRequestDto dto, User user) {
    checkUserExists(dto.getUsername(), user);

    isUserAdmin(user.getUsername());

    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new ObjectInvalidStateException(USER_OLD_PASSWORD_WRONG);
    }
  }

  public void validateForDelete(Long id, User user) {
    checkUserExists(id, user);
    isUserAdmin(user.getUsername());
  }

  public void checkUserExists(final Long id, User user) {
    if (Objects.isNull(user)) {
      throw new ObjectNotFoundException(USER_NOT_FOUND, id);
    }
  }

  public void checkUserExists(final String username, User user) {
    if (Objects.isNull(user)) {
      throw new ObjectNotFoundException(USER_NOT_FOUND, username);
    }
  }

  public void isUserAdmin(String username) {
    if (username.equals(ADMIN_USERNAME)) {
      throw new UnsupportedOperationException(ADMIN_DELETE_ERROR);
    }
  }
}
