package pl.kzcwat.localincidentserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pl.kzcwat.localincidentserver"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Local Incident API",
                "REST API for Local Incident application",
                "1.0",
                null,
                new Contact("KZC WAT", "https://github.com/KZC-WAT/", null),
                "GPL-3.0 License",
                "https://github.com/KZC-WAT/local-incident-server/blob/main/LICENSE",
                Collections.emptyList()
        );
    }
}
