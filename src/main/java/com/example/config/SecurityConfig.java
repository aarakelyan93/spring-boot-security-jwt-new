package com.example.config;

import static com.example.model.role.Roles.ADMIN;
import static com.example.model.role.Roles.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import com.example.filters.JwtTokenFilter;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final JwtTokenFilter jwtTokenFilter;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Disable CSRF
    http = http.cors().and().csrf().disable();

    // Set session management to stateless
    http = http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and();

    http = http
        .exceptionHandling()
        .authenticationEntryPoint((request, response, ex) -> {
              log.error("Unauthorized request - {}", ex.getMessage());
              response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            }
        )
        .and();

    http.authorizeRequests()
        //Swagger
        .antMatchers(GET, "/swagger", "/swagger-ui/*",
            "/v3/api-docs/swagger-config", "/v3/api-docs").permitAll()

        //Token
        .antMatchers(POST, "/api/token").permitAll()
        .antMatchers(POST, "/api/token/verify").permitAll()

        //Users
        .antMatchers(GET, "/api/users").hasRole(ADMIN.name())
        .antMatchers(GET, "/api/users/{id}").hasAnyRole(USER.name(), ADMIN.name())
        .antMatchers(GET, "/api/users/by-username/{username}").hasAnyRole(USER.name(), ADMIN.name())
        .antMatchers(POST, "/api/users").hasRole(ADMIN.name())
        .antMatchers(PUT, "/api/users").hasRole(ADMIN.name())
        .antMatchers(DELETE, "/api/users/{id}").hasRole(ADMIN.name())
        .antMatchers(POST, "/api/users/reset-password").hasRole(USER.name())
        .antMatchers(PUT, "/api/users/{id}/admin-reset-password").hasRole(ADMIN.name())

        .anyRequest().authenticated();

    // Add JWT token filter
    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}