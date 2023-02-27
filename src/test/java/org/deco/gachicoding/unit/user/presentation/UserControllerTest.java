package org.deco.gachicoding.unit.user.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.exception.user.password.PasswordAlreadyUsedException;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.presentation.UserController;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 참고 자료 : https://brunch.co.kr/@springboot/418
//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        })
@MockBean(JpaMetamodelMappingContext.class)     // jpaAuditingHandler
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @DisplayName("이메일 중복 체크 시, 사용가능한 이메일의 경우 true를 반환한다.")
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
                .andExpect(content().string("true"))
                .andDo(print());
    }

    @DisplayName("이메일 중복 체크 시, 사용할 수 없는 이메일의 경우 false를 반환한다.")
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
                .andExpect(content().string("false"))
                .andDo(print());
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


        mockMvc.perform(post("/api/user/create")
                        .content(mapper.writeValueAsBytes(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("로그인한 사용자는 자신의 별명을 수정할 수 있다.")
    @Test
    @WithMockUser(username = "1234@1234.com")
    void updateUser_Success() throws Exception {

        // given
        String newNickname = "nani_inaning";

        given(userService.modifyNickname(eq("1234@1234.com"), eq(newNickname)))
                .willReturn("nani_inaning");

        // when
        ResultActions perform = mockMvc.perform(patch("/api/user/update-nickname")
                .param("newNickname", newNickname)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(content().string("nani_inaning"))
                .andDo(print());
    }

    @DisplayName("올바른 비밀번호를 입력받아 올바른 사용자임을 확인한다.")
    @Test
    @WithMockUser(password = "1234")
    void confirmUser_Success() throws Exception {
        // given
        given(userService.confirmUser(eq("1234"), eq("1234")))
                .willReturn(true);

        // when
        ResultActions perform = mockMvc.perform(get("/api/user/confirm")
                .param("confirmPassword", "1234")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("잘못된 비밀번호를 입력받았을 시 올바른 사용자가 아님을 확인한다.")
    @Test
    @WithMockUser(password = "1234")
    void confirmUser_Fail() throws Exception {

        // given
        given(userService.confirmUser(eq("12345"), eq("1234")))
                .willReturn(false);

        // when
        ResultActions perform = mockMvc.perform(get("/api/user/confirm")
                .param("confirmPassword", "12345")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("사용자 비밀번호 변경이 성공한다.")
    @Test
    @WithMockUser(username = "1234@1234.com", password = "1234")
    void updateUserPassword_Success() throws Exception {
        // given
        PasswordUpdateRequestDto dto =
                new PasswordUpdateRequestDto("12345", "12345");

        given(userService.modifyUserPassword(eq("1234@1234.com"), eq(dto)))
                .willReturn(1L);

        // when
        ResultActions perform = mockMvc.perform(patch("/api/user/change-password")
                .content(new ObjectMapper().writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("기존 비밀번호와 같은 비밀번호가 입력될 경우 비밀번호 변경이 실패한다.")
    @Test
    @WithMockUser(username = "1234@1234.com", password = "1234")
    void updateUserPassword_Fail() throws Exception {
        // given
        PasswordUpdateRequestDto dto =
                new PasswordUpdateRequestDto("1234", "1234");

        given(userService.modifyUserPassword(eq("1234@1234.com"), any(PasswordUpdateRequestDto.class)))
                .willThrow(new PasswordAlreadyUsedException());

        // when
        ResultActions perform = mockMvc.perform(patch("/api/user/change-password")
                .content(new ObjectMapper().writeValueAsBytes(dto))
                .contentType(MediaType.APPLICATION_JSON));

        perform
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("errorMessage").value("비밀번호가 이전과 동일합니다. 새로운 비밀번호를 입력해주세요."))
                .andDo(print());

    }

    @DisplayName("사용자를 삭제한다.")
    @Test
    @WithMockUser(username = "1234@1234.com", roles = "USER")
    void deleteUser_Success() throws Exception {


        // when
        ResultActions perform = mockMvc.perform(delete("/api/user"));

        //then
        verify(userService, times(1))
                .deleteUser("1234@1234.com");

        perform
                .andExpect(status().isOk())
                .andDo(print());

    }

}