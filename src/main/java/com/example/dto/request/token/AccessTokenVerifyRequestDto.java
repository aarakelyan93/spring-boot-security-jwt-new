package com.example.dto.request.token;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenVerifyRequestDto {
  @NotBlank
  private String accessToken;
}
