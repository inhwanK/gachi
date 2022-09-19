//package org.deco.gachicoding.unit.emailconfirm.domain;
//
//import org.deco.gachicoding.common.factory.user.MockUser;
//import org.deco.gachicoding.emailconfirm.domain.EmailConfirmToken;
//import org.deco.gachicoding.emailconfirm.domain.repository.EmailConfirmTokenRepository;
//import org.deco.gachicoding.user.domain.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.*;
//
//@DataJpaTest
//public class EmailConfirmTokenRepositoryTest {
//
//    @Autowired
//    EmailConfirmTokenRepository emailConfirmTokenRepository;
//
//    EmailConfirmToken emailConfirmToken;
//
//    User user;
//
//    @BeforeEach
//    void setUp() {
//        user = MockUser.builder()
//                .userEmail("1234@1234.com")
//                .build();
//
//        emailConfirmToken = new EmailConfirmToken("1234@1234.com");
//        emailConfirmTokenRepository.save(emailConfirmToken);
//    }
//
//    @DisplayName("이메일 인증 토큰을 생성한다.")
//    @Test
//    void createEmailConfirmToken_Success() {
//
//        // given
//        EmailConfirmToken createdToken = new EmailConfirmToken("test@test.com");
//        emailConfirmTokenRepository.save(createdToken);
//
//        UUID token = createdToken.getTokenId();
//        Optional<EmailConfirmToken> expected = emailConfirmTokenRepository.findByTokenId(token);
//
//        // then
//        assertThat(createdToken)
//                .isEqualTo(expected.get());
//
//    }
//
//    @DisplayName("유효한 토큰을 조회한다.")
//    @Test
//    void retrieveValidToken_Success() {
//
//        // given
//        boolean valid = emailConfirmTokenRepository.existsByTargetEmail("1234@1234.com");
//
//        // then
//        assertThat(valid).isTrue();
//    }
//
//    @DisplayName("5분 이상이 지난 토큰을 조회할수 없다.")
//    @Test
//    void retrieveValidToken_Exception() {
//
//        EmailConfirmToken validToken =
//                emailConfirmTokenRepository.findByTokenId(emailConfirmToken.getTokenId())
//                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 인증 코드입니다."));
//
//        assertThat(validToken)
//                .isEqualTo(emailConfirmToken);
//        fail("미구현");
//    }
//}
