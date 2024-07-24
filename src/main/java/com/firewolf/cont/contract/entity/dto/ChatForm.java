package com.firewolf.cont.contract.entity.dto;

import com.firewolf.cont.contract.entity.ContractType;
import com.firewolf.cont.contract.entity.Legality;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.json.simple.JSONObject;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ChatForm {

    private String field;

    private Legality legality;

    private String description;

    public static ChatForm toDto(JSONObject jsonObject){
        return ChatForm.builder()
                .field(null)
                .legality(Legality.valueOf((String) jsonObject.get("isLegal")))
                .description((String) jsonObject.get("description"))
                .build();
    }

}
