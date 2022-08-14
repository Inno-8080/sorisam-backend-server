package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.RequestDto.LoginRequestDto;
import com.sparta.sorisam.Dto.RequestDto.SignupRequestDto;
import com.sparta.sorisam.Model.User;
import com.sparta.sorisam.Repository.UserRepository;
import com.sparta.sorisam.global.error.exception.EntityNotFoundException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import com.sparta.sorisam.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String repassword = requestDto.getRepassword();
        String img = requestDto.getImg();
        String intro = requestDto.getIntro();
        String pattern = "^[a-zA-Z0-9]*$";

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new InvalidValueException(ErrorCode.USERNAME_DUPLICATION);
        }

        if (username.length() < 4) {
            throw new InvalidValueException(ErrorCode.INVALID_INPUT_USERNAME);
        } else if (!Pattern.matches(pattern, username)) {
            throw new InvalidValueException(ErrorCode.INVALID_USERNAME);
        } else if (!password.equals(repassword)) {
            throw new InvalidValueException(ErrorCode.NOTEQUAL_INPUT_PASSWORD);
        } else if (password.length() < 4) {
            throw new InvalidValueException(ErrorCode.INVALID_PASSWORD);
        }

        userRepository.save(new User(username, passwordEncoder.encode(password), img, intro));
    }

    public String login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTFOUND_USER));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidValueException(ErrorCode.LOGIN_INPUT_INVALID);
        }

        return jwtTokenProvider.createToken(username);
    }
}
