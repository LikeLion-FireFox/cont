package com.firewolf.cont.contract.entity;

import jakarta.persistence.*;
import lombok.*;
import org.json.simple.JSONObject;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class ContractDescription {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "contract_description_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private String field;

    private Legality legality;

    private String description;

    public static ContractDescription toEntity(Contract contract, String field, JSONObject jsonObject){
        return ContractDescription.builder()
                .contract(contract)
                .field(field)
                .legality(Legality.valueOf(((String) jsonObject.get("isLegal")).toUpperCase()))
                .description((String) jsonObject.get("description"))
                .build();
    }

}
