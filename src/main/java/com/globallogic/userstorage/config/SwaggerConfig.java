package com.globallogic.userstorage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${app.version}")
    private String applicationVersion;
    @Value("${app.description}")
    private String applicationDescription;
    @Value("${app.openapi.contact.email}")
    private String contactEmail;
    @Value("${app.openapi.contact.name}")
    private String contactName;
    @Value("${app.openapi.contact.url}")
    private String contactUrl;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.globallogic.userstorage")).paths(PathSelectors.any())
                .build().apiInfo(apiInfoMetaData());
    }

    private ApiInfo apiInfoMetaData() {
        return new ApiInfoBuilder().title(applicationName).description(applicationDescription)
                .contact(new Contact(contactName, contactUrl, contactEmail)).version(applicationVersion).build();
    }

}
