package com.example.controller.token;

import com.example.dto.request.token.AccessTokenRequestDto;
import com.example.dto.request.token.AccessTokenVerifyRequestDto;
import com.example.dto.response.token.AccessTokenResponseDto;
import com.example.entity.User;
import com.example.service.jwt.JwtTokenService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccessTokenController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  @PostMapping("/token")
  public ResponseEntity<AccessTokenResponseDto> login(
      @RequestBody @Valid AccessTokenRequestDto dto) {
    try {
      Authentication authenticate = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              dto.getUsername(),
              dto.getPassword()
          )
      );

      User user = (User) authenticate.getPrincipal();

      String token = jwtTokenService.generateAccessToken(user);

      return ResponseEntity.ok(new AccessTokenResponseDto(token));
    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("/token/verify")
  public ResponseEntity<Boolean> verify(@Valid @RequestBody AccessTokenVerifyRequestDto dto) {
    String username = jwtTokenService.validate(dto.getAccessToken());

    return username != null ? ResponseEntity.ok(true) :
        ResponseEntity.badRequest().body(false);
  }
}
