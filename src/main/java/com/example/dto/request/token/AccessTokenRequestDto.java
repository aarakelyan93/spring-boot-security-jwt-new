package com.example.dto.request.token;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenRequestDto {
  @NotBlank
  private String username;
  @NotBlank
  private String password;
}
