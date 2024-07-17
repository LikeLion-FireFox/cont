package com.firewolf.cont.contract.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Legality {

    LEGAL("합법"),
    DANGER("의심"),
    ILLEGAL("위법");

    private final String description;
}
