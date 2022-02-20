package com.example.exception;

public class ObjectInvalidStateException extends MultilingualException {

  public ObjectInvalidStateException(final String code) {
    super(code);
  }

  public ObjectInvalidStateException(final String code, final String args) {
    super(code, args);
  }

}
