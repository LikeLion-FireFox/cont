package com.firewolf.cont.user.dto;

import com.firewolf.cont.contract.entity.Contract;
import com.firewolf.cont.contract.entity.ContractType;
import com.firewolf.cont.contract.entity.Legality;
import com.firewolf.cont.user.entity.Member;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageResponse {

    private String nickname;

    private String email;

    private List<MyPageContract> myPageContracts;

    public static MyPageResponse toDto(Member member, Page<MyPageContract> myPageContracts){
        return MyPageResponse.builder()
                .email(member.getAccountEmail())
                .nickname(member.getNickname())
                .myPageContracts(myPageContracts.stream().toList())
                .build();
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MyPageContract{

        private ContractType contractType;

        private Legality legality;

        private LocalDate createdDate;

        public static MyPageContract toDto(Contract contract){
            return MyPageContract.builder()
                    .contractType(contract.getContractType())
                    .createdDate(contract.getCreatedDate().toLocalDate())
                    .legality(contract.getLegality())
                    .build();
        }

    }

}
