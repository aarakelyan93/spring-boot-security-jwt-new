package com.example.exception;

import lombok.Getter;

@Getter
public class MultilingualException extends RuntimeException {

  private final String code;
  private final transient Object[] args;

  public MultilingualException(final String code, final Object... args) {
    this.code = code;
    this.args = args;
  }

  public MultilingualException(final String code) {
    this.code = code;
    args = new Object[] {};
  }

}
