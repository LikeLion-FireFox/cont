package com.firewolf.cont.contract.dto.gpt;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GptRequestDto {

    private String model;
    private int max_tokens;
    private List<Message> messages;

    public GptRequestDto(String model, int max_tokens, String prompt) {
        this.max_tokens = max_tokens;
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

}


