package com.firewolf.cont.contract.entity.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Employment {

    WAGE("임금"),
    WORKING_HOURS("소정 근로 시간"),
    HOLIDAY("휴일"),
    ANNUAL_VACATION("연차 유급 휴가"),
    WORKING_PLACE("근무 장소"),
    ASSIGNED_TASK("담당 업무");

    private final String description;

}
