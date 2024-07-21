package com.firewolf.cont.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveRequestDto {

    @NotBlank
    @Size(min = 2, max = 10)
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @NotBlank
    @Size(min = 5, max = 10)
    private String password;

}
