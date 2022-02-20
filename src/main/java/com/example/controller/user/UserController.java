package com.example.controller.user;

import com.example.dto.request.user.AdminUserResetPasswordRequestDto;
import com.example.dto.request.user.UserCreateRequestDto;
import com.example.dto.request.user.UserResetPasswordRequestDto;
import com.example.dto.request.user.UserUpdateRequestDto;
import com.example.dto.response.user.UserResponseDto;
import com.example.service.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@SecurityRequirement(name = "users-api-security")
public class UserController {

  private final UserService userService;

  @GetMapping
  public List<UserResponseDto> getAll() {
    return userService.getAll();
  }

  @GetMapping("/{id}")
  public UserResponseDto getById(@PathVariable Long id) {
    return userService.getById(id);
  }

  @GetMapping("/by-username/{username}")
  public UserResponseDto getById(@PathVariable String username) {
    return userService.getByUsername(username);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto save(@RequestBody @Valid UserCreateRequestDto dto) {
    return userService.save(dto);
  }

  @PutMapping("/{id}")
  public UserResponseDto update(@RequestBody @Valid UserUpdateRequestDto dto,
                                @PathVariable Long id) {
    return userService.update(id, dto);
  }

  @PutMapping("/reset-password")
  public void resetPassword(@RequestBody @Valid UserResetPasswordRequestDto dto) {
    userService.resetPassword(dto);
  }

  @PutMapping("/{id}/admin-reset-password")
  public void adminResetPassword(@RequestBody @Valid AdminUserResetPasswordRequestDto dto,
                                 @PathVariable Long id) {
    userService.adminResetPassword(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    userService.delete(id);
  }
}
