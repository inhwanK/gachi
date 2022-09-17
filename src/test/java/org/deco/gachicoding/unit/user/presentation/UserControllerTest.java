package org.deco.gachicoding.unit.user.presentation;


import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.repository.UserRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

    @DisplayName("회원가입시 이미 존재하는 이메일일 경우 false를 반환한다.")
    @Test
    void checkEmail_Return_False() throws Exception {

        String email = "1234@1234.com";
        given(userRepository.existsByUserEmail(email))
                .willReturn(false);

        mockMvc.perform(get("/api/user/regist/check-email")
                        .param("email", "1234@1234.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

//                .andReturn().getResponse().equals(false);

    }
}
