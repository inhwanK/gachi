package org.deco.gachicoding.unit.user.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.presentation.UserController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

// 참고 자료 : https://brunch.co.kr/@springboot/418
// 컨트롤러 테스트에서 데이터의 유효성, API의 반환값에 대한 검증 테스트를 진행한다.
//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)     // jpaAuditingHandler
@WithMockUser
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @DisplayName("회원가입시 존재하지 않는 이메일을 입력받았을 경우 true를 반환한다.")
    @Test
    void checkEmail_Return_True() throws Exception {

        // givne
        String email = "1234@1234.com";
        given(userRepository.existsByUserEmail(email))
                .willReturn(false);

        // when
        mockMvc.perform(get("/api/user/regist/check-email")
                        .param("email", "1234@1234.com")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @DisplayName("회원가입시 이미 존재하는 이메일을 입력받았을 경우 false를 반환한다.")
    @Test
    void checkEmail_Return_False() throws Exception {

        // given
        String email = "1234@1234.com";
        given(userRepository.existsByUserEmail(email))
                .willReturn(true);

        // when
        mockMvc.perform(get("/api/user/regist/check-email")
                        .param("email", "1234@1234.com")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @DisplayName("회원가입을 성공한다.")
    @Test
    void registerUser_Success() throws Exception {

        UserSaveRequestDto dto = new UserSaveRequestDto();
        dto.setUserEmail("1234@1234.com");
        dto.setUserName("김인환");
        dto.setUserNick("이나닝");
        dto.setUserPassword("1234");

        given(userService.createUser(dto))
                .willReturn(1L);

        ObjectMapper mapper = new ObjectMapper();

        /*
        mockMvc.perform(post("/api/user/create")
                        .param(mapper.write,mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
         */
        fail("미완성");
    }

    @DisplayName("회원가입을 실패한다.")
    @Test
    void registerUser_Fail() {
        fail("미구현");
    }

    @DisplayName("사용자 정보를 일괄적으로 수정한다.")
    @Test
    void updateUser_Success() {
        fail("미구현");
    }

    // 이 메소드 이름 변경되어야함.
    @DisplayName("비밀번호를 입력받아 올바른 사용자인지 확인 성공한다.")
    @Test
    void confirmUser_Success() {
        fail("미구현");
    }

    @DisplayName("비밀번호를 입력받아 올바른 사용자인지 확인 실패한다.")
    @Test
    void confirmUser_Fail() {
        fail("미구현");
    }

    @DisplayName("사용자 비밀번호를 변경 성공한다.")
    @Test
    void updateUserPassword_Success() {
        fail("미구현");
    }

    @DisplayName("사용자 비밀번호를 변경 실패한다.")
    @Test
    void updateUserPassword_Fail() {
        fail("미구현");
    }

    @DisplayName("사용자를 삭제한다.")
    @Test
    void deleteUser_Success() {
        fail("미구현");
    }
}