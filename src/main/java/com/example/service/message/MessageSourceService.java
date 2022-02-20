package com.example.service.message;

import com.example.exception.MultilingualException;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSourceService {

  private final MessageSource messageSource;

  public String getMessage(String code) {
    return messageSource.getMessage(code, new Object[] {}, StringUtils.EMPTY, Locale.ENGLISH);
  }

  public String getMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, StringUtils.EMPTY, Locale.ENGLISH);
  }

  public String getMessage(String code, List<Object> args) {
    return messageSource.getMessage(code, args.toArray(), StringUtils.EMPTY, Locale.ENGLISH);
  }

  public String getMessage(MultilingualException context) {
    return getMessage(context.getCode(), context.getArgs());
  }
}
