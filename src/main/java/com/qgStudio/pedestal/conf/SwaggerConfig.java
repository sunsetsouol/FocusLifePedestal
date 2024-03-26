package com.qgStudio.pedestal.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/16
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket createRestApi(Environment environment) {
        Profiles of = Profiles.of("dev", "test");
        boolean enable = environment.acceptsProfiles(of);
        return new Docket(DocumentationType.SWAGGER_2).groupName("专注接口").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.qgStudio")).paths(PathSelectors.ant("/focus/**"))
                .build().globalOperationParameters(setHeaderToken()).enable(enable);
    }
    @Bean
    public Docket createRestApi2(Environment environment) {
        Profiles of = Profiles.of("dev", "test");
        boolean enable = environment.acceptsProfiles(of);
        return new Docket(DocumentationType.SWAGGER_2).groupName("用户接口").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.qgStudio")).paths(PathSelectors.ant("/user/**"))
                .build().globalOperationParameters(setHeaderToken()).enable(enable);
    }
    @Bean
    public Docket createRestApi1(Environment environment) {
        Profiles of = Profiles.of("dev", "test");
        boolean enable = environment.acceptsProfiles(of);
        return new Docket(DocumentationType.SWAGGER_2).groupName("喝水接口").apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.qgStudio")).paths(PathSelectors.ant("/water/**"))
                .build().enable(enable);
    }

    /**
     * @param
     * @Description: 设置swagger文档中全局参数
     * @Date: 2020/9/11 10:15
     * @return: java.util.List<springfox.documentation.service.Parameter>
     */

    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder userId = new ParameterBuilder();
        userId.name("Authorization").description("用户令牌").modelRef(new ModelRef("string")).parameterType("header")
                .required(true).build();
        pars.add(userId.build());

        return pars;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("智能底座")
                .termsOfServiceUrl("")
                .description("智能底座")
                .version("1.0")
                .build();
    }
}