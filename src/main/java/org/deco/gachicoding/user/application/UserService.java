package org.deco.gachicoding.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.deco.gachicoding.user.dto.request.UserUpdateRequestDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@link Transactional} : 아이디 중복 시 Transaction silently rolled back because it has been marked as rollback-only 발생
     * <br> 원인 : 트랜잭션은 재사용 될수 없다.
     * <br> <br> save하면서 같은 이메일이 있으면 예외를 발생, 예외 발생 시 기본 값으로 들어있는 롤백이 true가 됨. save가 끝나고 나오면서 registerUser로 돌아 왔을때 @Transactional어노테이션이 있으면
     * 커밋을 앞에서 예외를 잡았기 때문에 문제 없다고 판단, 커밋을 실행한다. 하지만 roll-back only**이 마킹되어 있어 **롤백함.
     * <br> <br> 트러블 슈팅으로 넣으면 좋을 듯
     */
    public Long createUser(UserSaveRequestDto dto) {

        if (userRepository.existsByUserEmail(dto.getUserEmail()))
            throw new DataIntegrityViolationException("중복된 이메일 입니다.");

        // https://prohannah.tistory.com/82 참고
        // {dto 객체를 받아서 비밀번호가 인코딩 된 Entity 객체를 반환하는 행동} 이 필요 - 한 객체에?
        String encryptedPassword = passwordEncoder.encode(dto.getUserPassword());
        dto.setUserPassword(encryptedPassword);

        Long userIdx = userRepository.save(dto.toEntity()).getUserIdx();

        log.info("유저 이메일로 바로 인증 메일을 보낼지말지 고민");

        return userIdx;
    }

    // 기능별 분리 필요
    @Transactional
    public Long updateUser(Long idx, UserUpdateRequestDto dto) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        String encoded = passwordEncoder.encode(dto.getUserPassword());
        user.update(dto.getUserNick(), encoded, dto.isUserLocked(), dto.isUserEnabled());

        return idx;
    }

    @Transactional
    public Long changeUserPassword(String password) { // 비밀번호 변경 dto 있으면 좋음

        // 사전 조건 BCryptPasswordEncoder 클래스 참고하기
        Assert.notNull(password, "새로운 비밀번호를 입력하세요.");

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserEmail(userEmail).get();

        if(passwordEncoder.matches(password, user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호가 이전과 동일합니다.");
        }

        String encryptedPassword = passwordEncoder.encode(password);
        user.changeNewPassword(encryptedPassword);
        return user.getUserIdx();
    }

    // 닉네임 변경

    @Transactional
    public Long deleteUser(Long idx) {
        userRepository.deleteById(idx);
        return idx;
    }
}