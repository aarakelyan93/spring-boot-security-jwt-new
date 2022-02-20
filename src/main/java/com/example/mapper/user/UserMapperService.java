package com.example.mapper.user;

import static com.example.util.MessageCodeUtils.ROLE_NOT_FOUND;
import static com.example.util.StringUtils.UNDERSCORE;

import com.example.dto.request.user.UserCreateRequestDto;
import com.example.dto.request.user.UserUpdateRequestDto;
import com.example.dto.response.user.UserResponseDto;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.exception.ObjectNotFoundException;
import com.example.model.role.Roles;
import com.example.repository.RoleRepository;
import com.example.util.DateUtils;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperService {

  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public User mapDtoToEntity(UserCreateRequestDto dto) {
    Role role = roleRepository.findByAuthority(Roles.USER.getRole())
        .orElseThrow(() -> new ObjectNotFoundException(ROLE_NOT_FOUND, Roles.USER.getRole()));

    return User.builder()
        .username(dto.getUsername())
        .password(passwordEncoder.encode(dto.getPassword()))
        .firstname(dto.getFirstname())
        .lastname(dto.getLastname())
        .middleName(dto.getMiddleName())
        .passport(dto.getPassport())
        .dateOfBirth(dto.getDateOfBirth())
        .roles(new HashSet<>(Set.of(role)))
        .build();
  }

  public void mapDtoToEntity(User entity, UserUpdateRequestDto dto) {
    entity.setUsername(dto.getUsername());
    entity.setFirstname(dto.getFirstname());
    entity.setLastname(dto.getLastname());
    entity.setMiddleName(dto.getMiddleName());
    entity.setPassport(dto.getPassport());
    entity.setDateOfBirth(dto.getDateOfBirth());
  }

  public void mapEntityAsDeleted(User entity) {
    String deletedUsername = entity.getUsername() + UNDERSCORE + DateUtils.getNow();

    entity.setUsername(deletedUsername);
    entity.setDeletedDate(DateUtils.currentLocalDateTime());
  }

  public void mapEntityPassword(User entity, String password) {
    entity.setPassword(passwordEncoder.encode(password));
  }

  public UserResponseDto mapEntityToDto(User entity) {
    return UserResponseDto.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .firstname(entity.getFirstname())
        .lastname(entity.getLastname())
        .middleName(entity.getMiddleName())
        .passport(entity.getPassport())
        .dateOfBirth(entity.getDateOfBirth())
        .createdDate(DateUtils.toEpochMilli(entity.getCreatedDate()))
        .updatedDate(DateUtils.toEpochMilli(entity.getUpdatedDate()))
        .deletedDate(DateUtils.toEpochMilli(entity.getDeletedDate()))
        .build();
  }

}
