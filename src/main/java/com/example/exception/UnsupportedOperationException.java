package com.example.exception;

public class UnsupportedOperationException extends MultilingualException {

  public UnsupportedOperationException(final String code, final Object... args) {
    super(code, args);
  }

}
