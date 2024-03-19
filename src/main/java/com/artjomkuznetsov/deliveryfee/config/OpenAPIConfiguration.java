package com.artjomkuznetsov.deliveryfee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Artjom Kuznetsov");
        myContact.setEmail("a-kuznetsov@posteo.net");

        Info info = new Info()
                .title("Delivery fee calculator")
                .version("1.0")
                .description("API designed to calculate delivery fees for food couriers based on regional rates, vehicle types, and weather conditions.")
                .contact(myContact);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
