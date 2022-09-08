package org.deco.gachicoding.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;
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

    @Transactional
    public Long updateUser(
            String userEmail,
            UserUpdateRequestDto dto
    ) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.update(dto.getUserNick(), dto.isUserLocked(), dto.isUserEnabled());

        return user.getUserIdx();
    }

    @Transactional
    public Long changeUserPassword(
            String userEmail,
            PasswordUpdateRequestDto dto
    ) {
        User user = userRepository.findByUserEmail(userEmail).get();

        if(passwordEncoder.matches(dto.getConfirmPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호가 이전과 동일합니다.");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getConfirmPassword());
        user.changeNewPassword(encryptedPassword);

        return user.getUserIdx();
    }

    @Transactional
    public void deleteUser(String userEmail) {
        userRepository.deleteByUserEmail(userEmail);
    }
}