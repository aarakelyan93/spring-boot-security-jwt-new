package com.example.dto.request.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserResetPasswordRequestDto {

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String username;

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String oldPassword;

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String password;

}
