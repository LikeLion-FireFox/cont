package com.firewolf.cont.user.dto;

import com.firewolf.cont.user.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class LoginDto {

    @Getter @Setter
    public static class LoginRequestDto{

        @NotBlank
        @Size(min = 5, max = 30)
        private String email;

        @NotBlank
        @Size(min = 5, max = 10)
        private String password;
    }


    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private Long id;
        private String nickname;

        public static LoginResponseDto toDto(Member member){
            return LoginResponseDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .build();
        }
    }

}