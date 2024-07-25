package com.firewolf.cont.contract.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RealEstate {

    REAL_ESTATE_DETAIL("부동산 위치"),
    CONTRACT_CONTENT("거래 대금"),
    SPECIAL_DATA("특약 사항"),
    PERSONAL_DATA("인적 사항");

    private final String description;

}
