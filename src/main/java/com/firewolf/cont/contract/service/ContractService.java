package com.firewolf.cont.contract.service;

import com.firewolf.cont.contract.dto.ContractRequest;
import com.firewolf.cont.contract.dto.gpt.GptRequestDto;
import com.firewolf.cont.contract.dto.gpt.GptResponseDto;
import com.firewolf.cont.contract.entity.Contract;
import com.firewolf.cont.contract.entity.ContractType;
import com.firewolf.cont.contract.entity.Legality;
import com.firewolf.cont.contract.repository.ContractRepository;
import com.firewolf.cont.exception.CustomException;
import com.firewolf.cont.user.entity.Member;
import com.firewolf.cont.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.firewolf.cont.exception.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContractService {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiURL;

    @Value("${gpt.api.max_tokens}")
    private int max_tokens;

    private final RestTemplate template;

    private final ContractRepository contractRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String chatAndSave(Long memberId, ContractRequest contractRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(NO_MEMBER_CONFIGURED_500));
        if(contractRequest.getPrompt().length()>1500)
            throw new CustomException(EXCEEDED_CONTENT_LENGTH_400);

        String compPrompt = make_variable_request(contractRequest.getContractType(), contractRequest.getPrompt());
        GptRequestDto request = new GptRequestDto(model, max_tokens, compPrompt);
        GptResponseDto response =  template.postForObject(apiURL, request, GptResponseDto.class);

        String content = response.getChoices().get(0).getMessage().getContent();
        if(content.contains("계약서 형식에 맞는 내용을 첨부해주세요"))
            throw new CustomException(CONTRACT_FORMAT_ERROR_400);
        JSONObject jsonObject = change_to_json(content);
        Contract contract = Contract.builder()
                .contractType(contractRequest.getContractType())
                .legality(Legality.valueOf(
                        ((String) ((JSONObject) jsonObject.get("result")).get("islegal")).toUpperCase()))
                .member(member)
                .build();
        contract.addMember(member);
        contractRepository.save(contract);
        return jsonObject.toJSONString();
    }

    private JSONObject change_to_json(String content) {
        JSONParser jsonParser = new JSONParser();
        try {
            return (JSONObject) jsonParser.parse(content);
        }catch (ParseException e){
            log.error("json parsing error: ",e);
            throw new CustomException(JSON_PARSE_EXCEPTION_500);
        }
    }

    private String make_variable_request(ContractType contractType, String prompt){
        StringBuilder sb = new StringBuilder();
        sb.append("계약서 종류: ").append(contractType)
                .append(", 계약서 내용: [").append(prompt).append("] 이 계약서에 대한 ");
        switch (contractType) {
            case EMPLOYMENT -> sb.append("임금, 소정근로시간, 휴일, 연차유급휴가, 근무장소, 담당업무에 대한 내용 그리고 최종 결론이 ")
                    .append("""
                            {wage":
                                {
                                    "islegal" : "danger",
                                    "description" : "..."
                                },
                                "working_hours":
                                {
                                    "islegal" : "legal",
                                    "description" : "..."
                                },
                                "holiday":{
                                    "islegal" : "illegal",
                                    "description" : "..."
                                },
                                "annual_vacation":{
                                    "islegal" : "legal",
                                    "description" : "..."
                                },
                                "working_place":{
                                    "islegal" : "illegal",
                                    "description" : "..."
                                },
                                "assigned_task":{
                                    "islegal" : "legal",
                                    "description" : "..."
                                },
                                "result":{
                                "islegal" : "danger"
                                }
                            }""").append("와 같은 형식으로 islegal은 합법이면 legal, 의심스러우면 danger," +
                            "위법이면 illegal 그리고 description에 해당 항목의 결과가 왜 합법/의심/위법인지 " +
                            "최대 2줄로 작성해주고 result는 너가 내린 최종 결론(합법/의심/위법)");
            case PROPERTY -> sb.append("구체적인 부동산의 위치, 계약 내용에 기재된 모든 거래대금, 특약사항에 내용");
        }
        sb.append("만약 계약서가 아닌 내용이면, '계약서 형식에 맞는 내용을 첨부해주세요' 라고 응답을 줘");
        return sb.toString();
    }

}
