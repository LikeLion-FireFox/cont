package com.firewolf.cont.contract.dto;

import com.firewolf.cont.contract.entity.ContractType;
import lombok.*;

public class ContractDto {

    @Getter @Setter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class ContractRequest{

        private String prompt;

        private ContractType contractType;

    }

}
