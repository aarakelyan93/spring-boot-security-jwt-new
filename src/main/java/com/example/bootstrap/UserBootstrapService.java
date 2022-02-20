package com.example.bootstrap;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.model.role.Roles;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.jpa.hibernate.ddl-auto", havingValue = "create-drop")
public class UserBootstrapService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  @PostConstruct
  public void run() {
    log.info("Users and Roles bootstrap service started");

    Role roleAdmin = Role.builder()
        .authority(Roles.ADMIN.getRole())
        .build();

    Role roleUser = Role.builder()
        .authority(Roles.USER.getRole())
        .build();

    roleRepository.saveAll(List.of(roleUser, roleAdmin));

    User admin = User.builder()
        .username("admin")
        .password("$2a$12$uiC2NVLxtYXWUCUWotSx8eCOnHxpmzOOJU.Sfcg0PX6BWakSeE3BS")
        .firstname("Admin")
        .lastname("Admin")
        .middleName("Admin")
        .passport("AK471231")
        .dateOfBirth(LocalDate.now())
        .createdDate(LocalDateTime.now())
        .updatedDate(LocalDateTime.now())
        .roles(Set.of(roleAdmin))
        .build();

    userRepository.save(admin);

    log.info("Admin user by username : 'admin' created");
  }
}
