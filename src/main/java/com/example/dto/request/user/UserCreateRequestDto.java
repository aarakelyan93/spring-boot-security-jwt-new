package com.example.dto.request.user;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserCreateRequestDto {

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String username;

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String firstname;

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String lastname;

  @Size(max = 256)
  private String middleName;

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String passport;

  @NotNull
  private LocalDate dateOfBirth;

  @NotNull
  @NotBlank
  @Size(min = 2, max = 256)
  private String password;

}
