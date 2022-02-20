package com.example.dto.response.user;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
  private Long id;
  private String username;
  private String firstname;
  private String lastname;
  private String middleName;
  private String passport;
  private LocalDate dateOfBirth;
  private Long createdDate;
  private Long updatedDate;
  private Long deletedDate;
}
