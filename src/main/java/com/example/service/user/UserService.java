package com.example.service.user;

import com.example.dto.request.user.AdminUserResetPasswordRequestDto;
import com.example.dto.request.user.UserCreateRequestDto;
import com.example.dto.request.user.UserResetPasswordRequestDto;
import com.example.dto.request.user.UserUpdateRequestDto;
import com.example.dto.response.user.UserResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  List<UserResponseDto> getAll();

  UserResponseDto getById(Long id);

  UserResponseDto getByUsername(String username);

  UserResponseDto save(UserCreateRequestDto dto);

  UserResponseDto update(Long id, UserUpdateRequestDto dto);

  void resetPassword(UserResetPasswordRequestDto dto);

  void adminResetPassword(Long id, AdminUserResetPasswordRequestDto dto);

  void delete(Long id);

}
