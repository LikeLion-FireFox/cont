package com.firewolf.cont.contract.service;

import com.firewolf.cont.contract.dto.ContractRequest;
import com.firewolf.cont.contract.dto.gpt.GptRequestDto;
import com.firewolf.cont.contract.dto.gpt.GptResponseDto;
import com.firewolf.cont.contract.entity.Contract;
import com.firewolf.cont.contract.entity.Legality;
import com.firewolf.cont.contract.entity.enumtype.ContractType;
import com.firewolf.cont.contract.entity.enumtype.Employment;
import com.firewolf.cont.contract.entity.enumtype.RealEstate;
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

import java.util.ArrayList;
import java.util.List;

import static com.firewolf.cont.contract.entity.ContractDescription.toEntity;
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
        saveContractByContractType(contractRequest,jsonObject, member);
        return jsonObject.toJSONString();
    }

    private void saveContractByContractType(ContractRequest request, JSONObject jsonObject, Member member) {
        Legality legality = Legality.valueOf(((String)jsonObject.get("result")).toUpperCase());
        Contract contract = Contract.builder()
                .contractType(request.getContractType())
                .member(member)
                .legality(legality)
                .contractDescriptions(new ArrayList<>())
                .build();
        contract.addMember(member);
        switch (request.getContractType()){
            case EMPLOYMENT -> {
                for (Object object : jsonObject.keySet()) {
                    String key = (String) object;
                    for (Employment employment : Employment.values()) {
                        if(key.equals(employment.name().toLowerCase())) {
                            JSONObject field = (JSONObject) jsonObject.get(key);
                            contract.addContractDescription(toEntity(contract, key, field));
                        }
                    }
                }
            }
            case REAL_ESTATE -> {
                for (Object object : jsonObject.keySet()) {
                    String key = (String) object;
                    for (RealEstate realEstate : RealEstate.values()) {
                        if(key.equals(realEstate.name().toLowerCase())) {
                            JSONObject field = (JSONObject) jsonObject.get(key);
                            contract.addContractDescription(toEntity(contract, key, field));
                        }
                    }
                }
            }
            default -> {
                throw new CustomException(CONTRACT_TYPE_ERROR_400);
            }
        }
        contractRepository.save(contract);
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
            case EMPLOYMENT -> sb.append("임금, 소정 근로 시간, 휴일, 연차 유급 휴가, 근무 장소, 담당 업무에 대한 내용 그리고 최종 결론이 ")
                    .append("""
                            {wage":
                                {
                                    "isLegal" : "danger",
                                    "description" : "..."
                                },
                                "working_hours":
                                {
                                    "isLegal" : "legal",
                                    "description" : "..."
                                },
                                "holiday":{
                                    "isLegal" : "illegal",
                                    "description" : "..."
                                },
                                "annual_vacation":{
                                    "isLegal" : "legal",
                                    "description" : "..."
                                },
                                "working_place":{
                                    "isLegal" : "illegal",
                                    "description" : "..."
                                },
                                "assigned_task":{
                                    "isLegal" : "legal",
                                    "description" : "..."
                                },
                                "result": "danger"
                            }""");
            case REAL_ESTATE -> sb.append("""
                    구체적인 부동산 위치, 계약 내용에 기재된 모든 거래 대금, 특약 사항, 인적 사항에 내용이 순서대로 각각 {
                        "real_estate_detail": {
                            "isLegal" : "danger",
                            "description" : "..."
                         },
                        "contract_content": {
                             "isLegal" : "legal",
                             "description" : "..."
                         },
                         "special_data":{
                              "isLegal" : "illegal",
                              "description" : "..."
                         },
                         "personal_data":{
                            "isLegal" : "legal",
                            "description" : "..."
                         },
                        "result":"danger"
                    }""");
        }
        sb.append("에 대응되는 json 형식으로 isLegal은 합법이면 legal, 의심스러우면 danger," +
                "위법이면 illegal 그리고 description에 해당 항목의 결과가 왜 합법/의심/위법인지 " +
                "최대 2줄로 작성해주고 result는 너가 내린 종합적인 결론(legal/danger/illegal), " +
                "만약 해당 계약서에 부합하지 않는 내용이면, '계약서 형식에 맞는 내용을 첨부해주세요' 라고 응답을 줘");
        return sb.toString();
    }

}
