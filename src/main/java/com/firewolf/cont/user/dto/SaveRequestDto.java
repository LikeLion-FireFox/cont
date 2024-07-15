package com.firewolf.cont.user.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveRequestDto {

    @Size(min = 2, max = 10)
    private String nickname;

    @Size(min = 5, max = 30)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Size(min = 5, max = 10)
    private String password;

}
