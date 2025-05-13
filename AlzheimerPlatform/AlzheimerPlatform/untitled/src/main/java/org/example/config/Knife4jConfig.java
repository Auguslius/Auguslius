package org.example.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableKnife4j
public class Knife4jConfig  {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("knife4j-openapi3入门测试")
                        .version("1.0")
                        .description("knife4j-openapi3项目的接口文档"));
    }

    @Bean
    public GroupedOpenApi userAPI() {
        return GroupedOpenApi.builder().group("用户信息管理").
                pathsToMatch("/user/**").
                build();
    }

    @Bean
    public GroupedOpenApi systemAPI() {
        return GroupedOpenApi.builder().group("水果信息管理").
                pathsToMatch("/fruit/**").
                build();
    }
}
