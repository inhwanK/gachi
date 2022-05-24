package org.deco.gachicoding.service.impl;

import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.auth.Auth;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.domain.user.UserRepository;
import org.deco.gachicoding.dto.user.LoginRequestDto;
import org.deco.gachicoding.dto.user.UserResponseDto;
import org.deco.gachicoding.dto.user.UserSaveRequestDto;
import org.deco.gachicoding.dto.user.UserUpdateRequestDto;
import org.deco.gachicoding.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public boolean isDuplicateEmail(String userEmail) {
        return getUserByUserEmail(userEmail).isPresent();
    }

    @Transactional
    @Override
    public Optional<User> getUserByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }


    @Transactional
    @Override
    public UserResponseDto login(LoginRequestDto requestDto, HttpSession httpSession) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

            UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();


            System.out.println("유저 이름 > " + principal.getUsername());

            Optional<User> user = userRepository.findByUserEmail(principal.getUserEmail());
            UserResponseDto userResponseDto = new UserResponseDto(user.get());
            httpSession.setAttribute("user", userResponseDto);

            return userResponseDto;
            // BadCredentialsException - 스프링 시큐리티 에서 아이디 또는 비밀번호가 틀렸을 경우 나오는 예외
        } catch (BadCredentialsException e) {
            e.printStackTrace();
//            return new JwtResponseDto("아이디 또는 비밀번호를 확인해 주세요.");
            return new UserResponseDto(null);
        }
    }


    /**
     * {@link Transactional} : 아이디 중복 시 Transaction silently rolled back because it has been marked as rollback-only 발생
     * <br> 원인 : 트랜잭션은 재사용 될수 없다.
     * <br> <br> save하면서 같은 이메일이 있으면 예외를 발생, 예외 발생 시 기본 값으로 들어있는 롤백이 true가 됨. save가 끝나고 나오면서 registerUser로 돌아 왔을때 @Transactional어노테이션이 있으면
     * 커밋을 앞에서 예외를 잡았기 때문에 문제 없다고 판단, 커밋을 실행한다. 하지만 roll-back only**이 마킹되어 있어 **롤백함.
     * <br> <br> 트러블 슈팅으로 넣으면 좋을 듯
     */
    @Override
    public Long registerUser(UserSaveRequestDto dto) {

        // 이메일 중복 체크
        String registerEmail = dto.getUserEmail();

        if (isDuplicateEmail(registerEmail))
            throw new DataIntegrityViolationException("중복된 이메일 입니다.");

        // 비밀번호 변조
        dto.encryptPassword(passwordEncoder);

        // 유저 저장
        Long userIdx = userRepository.save(dto.toEntity()).getUserIdx();

        // 유저 이메일로 인증 메일 보내기

        return userIdx;
    }

    /**
     * 이메일 인증 로직
     *
     * @param authEmail
     */

//    새 코드로 수정 해야함.
    @Transactional
    @Override
    public void confirmEmail(String authEmail) {
        Auth auth = authService.getTokenByAuthEmail(authEmail);
        Optional<User> findUserInfo = getUserByUserEmail(auth.getAuthEmail());
//        auth.useToken();   // 토큰 만료 로직을 구현해주면 된다. ex) expired 값을 true 로 변경
//        findUserInfo.emailVerifiedSuccess();    // 유저의 이메일 인증 값 변경 로직을 구현해 주면 된다. ex) emailVerified 값을 true로 변경
    }

    @Transactional
    @Override
    public Long updateUser(Long idx, UserUpdateRequestDto dto) {

        User user = userRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. 회원 번호 = " + idx));

        user.update(dto.getUserNick(), dto.getUserPassword(), dto.isUserActivated(), dto.isUserAuth(), dto.getUserRole(), dto.getUserPicture());

        return idx;
    }

    @Transactional
    @Override
    public Long deleteUser(Long idx) {
        userRepository.deleteById(idx);
        return idx;
    }
}