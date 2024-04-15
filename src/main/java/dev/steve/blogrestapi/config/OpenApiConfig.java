package dev.steve.blogrestapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

  @Value("${application.openapi.dev-url}")
  private String devUrl;

  @Value("${application.openapi.prod-url}")
  private String prodUrl;

  @Bean
  OpenAPI myOpenApi() {
    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in development environment");

    Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Server URL in production environment");

    Contact contact = new Contact();
    contact.setEmail("steve@example.com");
    contact.setName("Steve H");
    contact.setUrl("https://www.steve.dev");

    License mitLicense = new License()
      .name("MIT License")
      .url("https://choosealicense.com/licenses/mit");

    Info info = new Info()
      .title("Blog RESTFull API")
      .version("1.0")
      .contact(contact)
      .description("This API exposes endpoints to manage blogpost")
      .termsOfService("https://www.steve.dev/terms")
      .license(mitLicense);

    List<Server> servers = new ArrayList<>();
    servers.add(devServer);
    servers.add(prodServer);

    return new OpenAPI().info(info).servers(servers);
  }
}
