package org.deco.gachicoding.unit.post.notice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.common.factory.post.notice.NoticeFactory;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.post.notice.application.NoticeService;
import org.deco.gachicoding.post.notice.application.dto.request.NoticeSaveRequestDto;
import org.deco.gachicoding.post.notice.presentation.RestNoticeController;
import org.deco.gachicoding.post.notice.presentation.dto.request.NoticeSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 이슈 정리
 * <br> 1. SecurityConfig 빈 생성 불가 문제
 * <br> 해결 : excludeFilters 를 통해 객체 주입 제외
 * <br> 참고 : https://velog.io/@cieroyou/WebMvcTest%EC%99%80-Spring-Security-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 *
 * <br><br> 2. jpaAuditing 빈 생성 불가 문제
 * <br> 문제 : SpringBootApplication 클래스에 EnableJpaAuditing 어노테이션을 함께 둔 것이 문제,
 * <br> EnableJpaAuditing이 SpringBootApplication에 위치하므로써 모든 테스트가 jpa 관련 빈을 필요로 하게 되었음
 * <br> 해결1 : 따로 config 클래스를 생성해 필요한 곳에만 주입하는 방법
 * <br> 참고 : https://stackoverflow.com/questions/41250177/getting-at-least-one-jpa-metamodel-must-be-present-with-webmvctest, https://xlffm3.github.io/spring%20&%20spring%20boot/JPAError/
 * <br> 해결 2 : 테스트마다 MockBean으로 jpa 의존성을 주입하는 방법
 * <br> 참고 : https://stackoverflow.com/questions/41250177/getting-at-least-one-jpa-metamodel-must-be-present-with-webmvctest,
 *
 * <br><br> 3. csrf 토큰 널로 에러 - 403
 * <br> 해결 : spring-security-test 의존성 추가 후 SecurityMockMvcRequestPostProcessors.csrf()를 통해 요청에 csrf 토큰 주입
 * <br> 참고 : https://velog.io/@cieroyou/WebMvcTest%EC%99%80-Spring-Security-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 * <br><br> 4. 시큐리티 권한 에러 - 401
 * <br> 해결 : WithMockUser 어노테이션으로 유저 권한 부여
 * <br> 참고 : https://velog.io/@cieroyou/WebMvcTest%EC%99%80-Spring-Security-%ED%95%A8%EA%BB%98-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0
 * <br><br> 5. 잘못된 요청 에러 - 400
 * <br> 해결 : param이 아닌 content에 objectMapper로 dto 클래스를 파라미터로 주입, objectMapper에 대한 추가적인 이해 필요
 * <br> 참고 : 없음
**/

// 특정 컨트롤러 클래스만 지정하여 스캔
@WebMvcTest(controllers = RestNoticeController.class,
            excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
            })
@MockBean(JpaMetamodelMappingContext.class)     // jpaAuditingHandler
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoticeService noticeService;

    @Test
    @WithMockUser
    @DisplayName("사용자는 공지사항을 작성할 수 있다.")
    void write_writeNoticeWithUser_Success() throws Exception {
        // given
        String userEmail = "gachicoding@test.com";
        String notTitle = "테스트 공지사항 제목 수정 전";
        String notContents = "테스트 공지사항 내용 수정 전";

        NoticeSaveRequest request = NoticeFactory.mockNoticeSaveRequest(userEmail, notTitle, notContents);

        given(noticeService.registerNotice(any(NoticeSaveRequestDto.class)))
                .willReturn(1L);

        // when
        ResultActions perform = mockMvc.perform(post("/api/notice")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // then
        perform.andExpect(status().isCreated());

        // userRepository의 findByUserEmail이 1번 실행되었는지 검사한다.
        verify(noticeService, times(1))
                .registerNotice(any(NoticeSaveRequestDto.class));
    }

}
