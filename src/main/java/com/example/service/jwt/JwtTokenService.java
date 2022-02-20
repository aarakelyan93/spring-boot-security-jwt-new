package com.example.service.jwt;

import static com.example.util.StringUtils.ROLES;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Clock;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.property.JwtProperty;
import com.example.util.DateUtils;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtTokenService {

  private final JwtProperty jwtProperty;
  private Algorithm algorithm;

  public String generateAccessToken(User user) {
    LocalDateTime now = DateUtils.currentLocalDateTime();

    return JWT.create()
        .withIssuer(jwtProperty.getIssuer())
        .withIssuedAt(Timestamp.valueOf(now))
        .withExpiresAt(Timestamp.valueOf(now.plusDays(jwtProperty.getExpireDays())))
        .withSubject(user.getUsername())
        .withClaim(ROLES,
            user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toList()))
        .sign(algorithm);
  }

  public String validate(String token) {
    try {
      JWTVerifier.BaseVerification verification =
          (JWTVerifier.BaseVerification) JWT.require(algorithm).withIssuer(jwtProperty.getIssuer());

      Clock clock = () -> Timestamp.valueOf(DateUtils.currentLocalDateTime());

      JWTVerifier verifier = verification.build(clock);

      DecodedJWT jwt = verifier.verify(token);

      return jwt.getSubject();
    } catch (JWTVerificationException exception) {
      log.error(exception);
      return null;
    }
  }

  @PostConstruct
  void postConstruct() {
    algorithm = Algorithm.HMAC256(jwtProperty.getSecret());
  }
}
