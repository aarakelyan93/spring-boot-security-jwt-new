package com.example.service.user;

import static com.example.util.StringUtils.ADMIN_USERNAME;

import com.example.dto.request.user.AdminUserResetPasswordRequestDto;
import com.example.dto.request.user.UserCreateRequestDto;
import com.example.dto.request.user.UserResetPasswordRequestDto;
import com.example.dto.request.user.UserUpdateRequestDto;
import com.example.dto.response.user.UserResponseDto;
import com.example.entity.User;
import com.example.mapper.user.UserMapperService;
import com.example.repository.UserRepository;
import com.example.validators.user.UserValidatorService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapperService userMapperService;
  private final UserRepository userRepository;
  private final UserValidatorService userValidatorService;

  @Override
  public List<UserResponseDto> getAll() {
    List<User> users = userRepository.findAllByDeletedDateNull();

    log.debug("Loading users list total size : '{}'", users.size());

    return users.stream()
        .filter(it -> !it.getUsername().equals(ADMIN_USERNAME))
        .map(userMapperService::mapEntityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserResponseDto getById(final Long id) {
    User entity = userRepository.findByIdAndDeletedDateNull(id).orElse(null);

    userValidatorService.checkUserExists(id, entity);

    return userMapperService.mapEntityToDto(entity);
  }

  @Override
  public UserResponseDto getByUsername(final String username) {
    User entity = userRepository.findByUsernameAndDeletedDateNull(username).orElse(null);

    userValidatorService.checkUserExists(username, entity);

    return userMapperService.mapEntityToDto(entity);
  }

  @Override
  @Transactional
  public UserResponseDto save(final UserCreateRequestDto dto) {
    userValidatorService.validateForCreate(dto);

    User entity = userMapperService.mapDtoToEntity(dto);

    userRepository.save(entity);

    log.debug("User by username : '{}' created and has an id : '{}'",
        entity.getUsername(), entity.getId());

    return userMapperService.mapEntityToDto(entity);
  }

  @Override
  @Transactional
  public UserResponseDto update(final Long id, final UserUpdateRequestDto dto) {
    User entity = userRepository.findByIdAndDeletedDateNull(id).orElse(null);

    userValidatorService.validateForUpdate(id, entity);

    userMapperService.mapDtoToEntity(entity, dto);

    userRepository.save(entity);

    log.debug("User by id : '{}' updated", entity.getId());

    return userMapperService.mapEntityToDto(entity);
  }

  @Override
  @Transactional
  public void resetPassword(final UserResetPasswordRequestDto dto) {
    User entity = userRepository.findByUsernameAndDeletedDateNull(dto.getUsername()).orElse(null);

    userValidatorService.validateForResetPassword(dto, entity);

    userMapperService.mapEntityPassword(entity, dto.getPassword());

    userRepository.save(entity);

    log.debug("Password for user by id : '{}' changed", entity.getId());
  }

  @Override
  @Transactional
  public void adminResetPassword(final Long id, final AdminUserResetPasswordRequestDto dto) {
    User entity = userRepository.findByIdAndDeletedDateNull(id).orElse(null);

    userValidatorService.checkUserExists(id, entity);

    userMapperService.mapEntityPassword(entity, dto.getPassword());

    userRepository.save(entity);

    log.debug("Password for user by id : '{}' changed by admin", entity.getId());
  }

  @Override
  @Transactional
  public void delete(final Long id) {
    User entity = userRepository.findByIdAndDeletedDateNull(id).orElse(null);

    userValidatorService.validateForDelete(id, entity);

    userMapperService.mapEntityAsDeleted(entity);

    userRepository.save(entity);

    log.debug("User by id : '{}' delete", entity.getId());
  }
}
