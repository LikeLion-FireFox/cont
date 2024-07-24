package com.firewolf.cont.contract.entity;

import com.firewolf.cont.contract.entity.dto.ChatForm;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue(value = "P")
public class PropertyContract extends Contract{

    @Embedded
    private ChatForm property_detail;
    @Embedded
    private ChatForm contract_content;
    @Embedded
    private ChatForm special_data;
    @Embedded
    private ChatForm personal_data;

}
