package com.firewolf.cont.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springdoc-public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Contract Support Application Everywhere API")
                        .version("1.0.0")
                        .description("swagger-ui에서 request 시에 querystring(ex) memberId)을 요구하는 요청이 많은데, " +
                                "실제로 querystring을 요구하는 요청은 아래와 같습니다.\n" +
                                "/loginPage/save/checkEmail\n" +
                                "이 요청 외에는 서버에서 querystring을 요구하지 않습니다. 세션 속성과 swagger-ui 쪽 호환이 잘 안 되는 것 같습니다.\n"+
                                "pk가 1부터 시작하므로 1이나 그 이상의 값을 입력하시면 될듯 합니다."));
    }
}