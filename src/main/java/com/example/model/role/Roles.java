package com.example.model.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");

  private final String role;
}
