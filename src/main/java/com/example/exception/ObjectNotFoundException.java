package com.example.exception;

public class ObjectNotFoundException extends MultilingualException {

  public ObjectNotFoundException(final String code, final Object... args) {
    super(code, args);
  }

}
