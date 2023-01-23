package com.eb.disputemanagement.dispute.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Dispute Management System API",
        version = "0.0.1-SNAPSHOT",
        description = "Dispute Management System",
        contact = @Contact(email = "mikeleemiko@gmail.com", url = "dispute@management.com")

), security = {
        @SecurityRequirement(name = "bearerAuth")
},
        servers = {
                @Server(
                        url = "http://*.*.*.*:*/",
                        description = "DEV Server"
                ),
                @Server(
                        url = "http://*.*.*.*:*/",
                        description = "PROD Server"
                )})

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
