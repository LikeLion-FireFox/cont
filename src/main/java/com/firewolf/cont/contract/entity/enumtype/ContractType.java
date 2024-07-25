package com.firewolf.cont.contract.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContractType {

    EMPLOYMENT("근로 계약"),
    REAL_ESTATE("부동산 계약");

    private final String contract_name;

}
