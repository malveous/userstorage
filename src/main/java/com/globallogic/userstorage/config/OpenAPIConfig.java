package com.globallogic.userstorage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Value("${spring.profiles.active}")
    private String activeProfile;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${app.version}")
    private String applicationVersion;
    @Value("${app.description}")
    private String applicationDescription;
    @Value("${app.openapi.url}")
    private String openApiUrl;
    @Value("${app.openapi.contact.email}")
    private String contactEmail;
    @Value("${app.openapi.contact.name}")
    private String contactName;
    @Value("${app.openapi.contact.url}")
    private String contactUrl;

    @Bean
    public OpenAPI openAPI() {
        var server = new Server();
        server.setUrl(openApiUrl);
        server.setDescription("""
                Server URL in %s environment""".formatted(activeProfile));

        var contact = new Contact();
        contact.setEmail(contactEmail);
        contact.setName(contactName);
        contact.setUrl(contactUrl);

        var info = new Info().title(applicationName).version(applicationVersion).contact(contact)
                .description(applicationDescription);

        return new OpenAPI().info(info).servers(List.of(server));
    }

}
