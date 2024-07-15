package com.firewolf.cont.user.entity;

import com.firewolf.cont.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private Boolean isKakaoMember; //카카오 로그인 회원용
    private Long kakaoId;  //카카오 서버로부터 발급 받은 카카오 회원의 교유 id

    private String nickname; //공통

    private String password; //카카오로 로그인 하지 않은 회원

    private String accountEmail; //공통 (kakao 로그인 시에는 선택사항)

    private LocalDate birthday; // year - month - day

}
