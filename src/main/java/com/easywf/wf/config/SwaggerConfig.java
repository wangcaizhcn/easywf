package com.easywf.wf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	public static final String BASE_PACKAGE = "com.easywf.wf";
	
    private boolean enable = true;

    @Value("${server.address:localhost}")
    private String address;

    @Value("${server.port:8080}")
    private String port;
    
    @Value("${server.servlet.context-path:}")
    private String context;
	
	@Bean
    public Docket swagger() {
		
		System.out.println("RESTful APIs : http://" + address + ":" + port + context + "/doc.html");
        
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
	
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Easy Workflow RESTful APIs")
                .description("Easy Workflow RESTful APIs")
                //.termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
