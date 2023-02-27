package org.deco.gachicoding.unit.post.question.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.common.factory.user.UserMock;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.deco.gachicoding.post.question.application.dto.QuestionDto;
import org.deco.gachicoding.post.question.presentation.QuestionController;
import org.deco.gachicoding.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;

@Slf4j
@WebMvcTest(
        controllers = QuestionController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@MockBean(JpaMetamodelMappingContext.class) // jpaAuditingHandler
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QuestionService questionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("질문 등록 성공한다.") // 자기 자신 참조하는 과정 체크해보기
    @Test
    @WithMockUser(username = "1234@1234.com")
    void register_Question_Success() throws Exception {
        QuestionDto.SaveRequestDto dto = QuestionDto.SaveRequestDto.builder()
                .queTitle("새로운 질문")
                .queGeneralContent("새로운 질문의 일반적인 내용")
                .queGeneralContent("새로운 질문의 일반적인 설명들")
                .queCodeContent("```java class Test{}```")
                .queErrorContent("error ~~~")
                .build();

        User questioner = UserMock.builder()
                .userEmail("1234@1234.com")
                .build();
        // given
        given(questionService.registerQuestion(eq(questioner.getUserEmail()), eq(dto)))
                .willReturn(1L);


        String saveRequestDto = objectMapper.writeValueAsString(dto);
        log.info("요청 dto : {}", saveRequestDto);
        // when
        mockMvc.perform(post("/api/question")
                        .content(saveRequestDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
