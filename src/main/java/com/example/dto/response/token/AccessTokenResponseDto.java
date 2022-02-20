package com.example.dto.response.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessTokenResponseDto {
  private String accessToken;
}
