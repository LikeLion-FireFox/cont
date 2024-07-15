package com.firewolf.cont.user.repository;

import com.firewolf.cont.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByPassword(String password);
    Optional<Member> findByKakaoId(Long kakaoId);
    Optional<Member> findByAccountEmail(String email);
}
