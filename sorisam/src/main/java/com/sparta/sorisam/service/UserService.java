package com.sparta.sorisam.service;

import com.sparta.sorisam.Dto.RequestDto.LoginRequestDto;
import com.sparta.sorisam.Dto.RequestDto.SignupRequestDto;
import com.sparta.sorisam.Dto.ResponseDto.LoginResponseDto;
import com.sparta.sorisam.Model.User;
import com.sparta.sorisam.Repository.UserRepository;
import com.sparta.sorisam.global.error.exception.EntityNotFoundException;
import com.sparta.sorisam.global.error.exception.ErrorCode;
import com.sparta.sorisam.global.error.exception.InvalidValueException;
import com.sparta.sorisam.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    //현재 SecurityContext에 있는 유저 정보 가져오기기
    @Transactional
    public LoginResponseDto getMyInfo() {
        // SecurityContext 에 유저 정보가 저장되는 시점
        // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장

        //JwtFilter 에서 SecurityContext 에 세팅한 유저 정보를 꺼냅니다.
        //유저네임으로 찾음 -> 코멘트/리코멘트 생성 시에 사제
        //SecurityContext 는 ThreadLocal 에 사용자의 정보를 저장합니다.

        //저장된 유저 정보
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //유저 정보 있는지 확인
        if(authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다");
        }
        //토큰에서 유저 네임으로 찾은 유저 정보 정보 중 Dto에 해당하는 필드 가져오기 (여기선 유저 네임)
        return userRepository.findByUsername(authentication.getName())
                .map(LoginResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
}
