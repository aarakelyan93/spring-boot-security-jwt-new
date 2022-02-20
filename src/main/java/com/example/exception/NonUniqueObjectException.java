package com.example.exception;

public class NonUniqueObjectException extends MultilingualException {

  public NonUniqueObjectException(final String code, final Object... args) {
    super(code, args);
  }

  public NonUniqueObjectException(final String code) {
    super(code);
  }

}
