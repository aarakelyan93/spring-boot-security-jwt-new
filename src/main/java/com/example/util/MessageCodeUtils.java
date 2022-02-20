package com.example.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageCodeUtils {

  //non unique error
  public static final String USERNAME_NON_UNIQUE = "username.non.unique";

  // object not found error
  public static final String USER_NOT_FOUND = "user.not.found";
  public static final String ROLE_NOT_FOUND = "role.not.found";

  //invalid state
  public static final String USER_OLD_PASSWORD_WRONG = "user.old.password.wrong";

  //unsupported operations
  public static final String ADMIN_DELETE_ERROR = "admin.delete.error";
}
