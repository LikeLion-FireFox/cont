package com.firewolf.cont.contract.dto;

import com.firewolf.cont.contract.entity.enumtype.ContractType;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ContractRequest {
    private String prompt;

    private ContractType contractType;

}
