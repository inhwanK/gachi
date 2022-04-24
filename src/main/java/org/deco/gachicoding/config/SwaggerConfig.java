package org.deco.gachicoding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * 이 클래스가 지금 {@link WebMvcConfigurationSupport} 를 extends 하고,
 * {@link Configuration} 어노테이션이 있어야 함.
 * 근데 이런 {@link WebMvcConfigurationSupport} 를 상속받는 클래스가 다른게 하나 더 있으면 설정이 안먹힘
 *
 * 따라서 이 클래스는 처음에 스웨거 쓸려고 만든 거 였는데, Pageable resolve 하기 위해서도 사용하게 되었고,
 * 이 후에 다른 설정이 또 추가 될 수 있으니까
 * 여러 의미를 담는 클래스 이름으로 바꾸고 메서드마다 역할을 문서로 정리해줘야할 거 같음.
 * WebMvcConfig 관련 문서를 읽어봐야함.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                //    RequestHandlerSelectors.basePackage("com.app.api") -> 지정된 패키지만 API 화
                .paths(PathSelectors.ant("/api/**")) // 그중 /api/** 인 URL들만 필터링
//                     PathSelectors.any() 모든 URL 적용
                .build();
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    // pageable 사용을 위한 resolver 생성 수정 필요.
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }
}
