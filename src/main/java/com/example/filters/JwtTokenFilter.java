package com.example.filters;

import static java.util.List.of;
import static java.util.Optional.ofNullable;

import com.example.service.jwt.JwtTokenService;
import com.example.service.security.CustomUserDetailsService;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.hasLength(header) && header.startsWith("Bearer ")) {
      final String token = header.split(" ")[1].trim();

      String username = jwtTokenService.validate(token);
      if (username == null) {
        chain.doFilter(request, response);
        return;
      }

      UserDetails userDetails =
          userDetailsService.loadUserByUsername(username);

      Collection<? extends GrantedAuthority> authorities = ofNullable(userDetails)
          .map(UserDetails::getAuthorities)
          .orElse(of());

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, authorities
      );

      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    chain.doFilter(request, response);
  }
}
