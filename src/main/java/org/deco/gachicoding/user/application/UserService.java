package org.deco.gachicoding.user.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.exception.user.password.PasswordAlreadyUsedException;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.user.domain.repository.UserRepository;
import org.deco.gachicoding.user.dto.request.PasswordUpdateRequestDto;
import org.deco.gachicoding.user.dto.request.UserSaveRequestDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createUser(UserSaveRequestDto dto) {

        if (userRepository.existsByUserEmail(dto.getUserEmail()))
            throw new DataIntegrityViolationException("중복된 이메일 입니다.");

        String encryptedPassword = passwordEncoder.encode(dto.getUserPassword());
        dto.setUserPassword(encryptedPassword);

        return userRepository.save(dto.toEntity()).getUserIdx();
    }

    @Transactional
    public String modifyNickname(
            String userEmail,
            String newNickname
    ) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        user.updateNick(newNickname);

        return user.getUserNick();
    }

    public boolean confirmUser(
            String confirmPassword,
            String userPassword
    ) {
        if (!passwordEncoder.matches(confirmPassword, userPassword)) {
            return false;
        }
        return true;
    }

    @Transactional
    public Long modifyUserPassword(
            String userEmail,
            PasswordUpdateRequestDto dto
    ) {

        User user = userRepository.findByUserEmail(userEmail).get();

        if (passwordEncoder.matches(dto.getConfirmPassword(), user.getUserPassword())) {
            throw new PasswordAlreadyUsedException();
        }

        String encryptedPassword = passwordEncoder.encode(dto.getConfirmPassword());
        user.changePassword(encryptedPassword);

        return user.getUserIdx();
    }

    @Transactional
    public void deleteUser(String userEmail) {
        userRepository.deleteByUserEmail(userEmail);
    }
}