package com.firewolf.cont.contract.entity;

import com.firewolf.cont.global.BaseEntity;
import com.firewolf.cont.user.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    private Boolean isLegal;

    @Enumerated(STRING)
    private ContractType contractType;

    @Column(length = 5000)
    private String request_prompt;

    @Column(length = 5000)
    private String response_content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
