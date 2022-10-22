package org.deco.gachicoding.unit.user.presentation;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.deco.gachicoding.config.SecurityConfig;
import org.deco.gachicoding.config.security.RestAuthenticationToken;
import org.deco.gachicoding.user.application.UserService;
import org.deco.gachicoding.user.domain.RoleType;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.dto.request.authentication.UserAuthenticationDto;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

// 참고 자료 : https://brunch.co.kr/@springboot/418
// 컨트롤러 테스트에서 데이터의 유효성, API의 반환값에 대한 검증 테스트를 진행한다.
//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
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

    @DisplayName("존재하지 않는 이메일의 경우 true를 반환한다.")
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

    @DisplayName("존재하는 이메일의 경우 false를 반환한다.")
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
    void updateUser_Success() throws Exception {

        // given
        User user = User.builder()
                .userEmail("1234@1234.com")
                .userName("김인환")
                .userNick("이나닝")
                .userPassword("1234")
                .userRole(RoleType.ROLE_USER)
                .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.toString()));

        UserDetails userDetails = new UserAuthenticationDto(user, roles);
        Authentication token = new RestAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        String newNickname = "nani_inaning";

        given(userService.modifyNickname("1234@1234.com", newNickname))
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
    void confirmUser_Success() throws Exception {
        // given
        User user = User.builder()
                .userEmail("1234@1234.com")
                .userName("김인환")
                .userNick("이나닝")
                .userPassword("1234")
                .userRole(RoleType.ROLE_USER)
                .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.toString()));

        UserDetails userDetails = new UserAuthenticationDto(user, roles);
        Authentication token = new RestAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        given(userService.confirmUser("1234", user.getUserPassword()))
                .willReturn(true);

        // when
        ResultActions perform = mockMvc.perform(get("/api/user/confirm")
                .param("confirmPassword", "1234")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("잘못된 비밀번호를 입력받았을 시 올바른 사용자가 아님을 확인한다.")
    @Test
    void confirmUser_Fail() throws Exception {

        // given
        User user = User.builder()
                .userEmail("1234@1234.com")
                .userName("김인환")
                .userNick("이나닝")
                .userPassword("1234")
                .userRole(RoleType.ROLE_USER)
                .build();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(RoleType.ROLE_USER.toString()));

        UserDetails userDetails = new UserAuthenticationDto(user, roles);
        Authentication token = new RestAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        given(userService.confirmUser("12345", user.getUserPassword()))
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
    void updateUserPassword_Success() {
        fail("미구현");
    }

    @DisplayName("사용자 비밀번호 변경이 실패한다.")
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