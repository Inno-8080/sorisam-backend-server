package com.sparta.sorisam.controller;


import com.sparta.sorisam.Dto.RequestDto.LoginRequestDto;
import com.sparta.sorisam.Dto.RequestDto.SignupRequestDto;
import com.sparta.sorisam.global.common.response.ApiUtils;
import com.sparta.sorisam.global.common.response.CommonResponse;
import com.sparta.sorisam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/signup")
    public CommonResponse<?> registerUser(@Valid @RequestBody SignupRequestDto requestDto, MultipartFile img) {
        userService.registerUser(requestDto,img);
        return ApiUtils.success(201, "회원가입에 성공하였습니다.");
    }

    @PostMapping("/api/login")
    public CommonResponse<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.login(loginRequestDto);
        return ApiUtils.success(200, token);
    }

}

