package com.example.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "com.example.jwt")
public class JwtProperty {
  private String secret;
  private String issuer;
  private Long expireDays;
}
