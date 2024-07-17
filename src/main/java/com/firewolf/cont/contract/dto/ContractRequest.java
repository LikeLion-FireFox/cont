package com.firewolf.cont.contract.dto;

import com.firewolf.cont.contract.entity.ContractType;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ContractRequest {
    private String prompt;

    private ContractType contractType;

}
