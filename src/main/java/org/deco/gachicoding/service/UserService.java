package org.deco.gachicoding.service;

import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.user.LoginRequestDto;
import org.deco.gachicoding.dto.user.UserSaveRequestDto;
import org.deco.gachicoding.dto.user.UserUpdateRequestDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public interface UserService {

    boolean isDuplicateEmail(String email);

    Optional<User> getUserByUserEmail(String email);

    String login(LoginRequestDto requestDto, HttpSession httpSession);

    Long registerUser(UserSaveRequestDto dto);

    void confirmEmail(String token);

    Long updateUser(Long idx, UserUpdateRequestDto dto);

    Long deleteUser(Long idx);

}
