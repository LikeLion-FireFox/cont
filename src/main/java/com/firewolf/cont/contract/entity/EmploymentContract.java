package com.firewolf.cont.contract.entity;

import com.firewolf.cont.contract.entity.dto.ChatForm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "E")
public class EmploymentContract extends Contract{

    @Embedded
    private ChatForm wage;
    @Embedded
    private ChatForm working_hours;
    @Embedded
    private ChatForm holiday;
    @Embedded
    private ChatForm annual_vacation;
    @Embedded
    private ChatForm working_place;
    @Embedded
    private ChatForm assigned_task;

}
