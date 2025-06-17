package com.mooky.pet_diary.domain.user.auth;

import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mooky.pet_diary.domain.user.auth.dto.EmailLoginReq;
import com.mooky.pet_diary.domain.user.auth.dto.GoogleLoginReq;
import com.mooky.pet_diary.domain.user.auth.dto.UserDto;
import com.mooky.pet_diary.domain.user.auth.dto.UserSignUpReq;
import com.mooky.pet_diary.domain.user.constraints.groups.GoogleSignUpInfo;
import com.mooky.pet_diary.domain.user.constraints.groups.Password;
import com.mooky.pet_diary.domain.user.constraints.groups.UserEmail;
import com.mooky.pet_diary.domain.user.constraints.groups.UserSignUpInfo;
import com.mooky.pet_diary.domain.user.constraints.groups.Username;
import com.mooky.pet_diary.global.ApiResponse;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("${mooky.endpoint}/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup/email")
    public ApiResponse signUpByEmail(
            @Validated({ UserSignUpInfo.class, UserEmail.class, Password.class }) @RequestBody UserSignUpReq req) {
        return ApiResponse.ok(this.authService.signUpByEmail(req));
    }
    
    @PostMapping("/signup/google")
    public ApiResponse signUpByGoogle(@Validated({ UserSignUpInfo.class, UserEmail.class, GoogleSignUpInfo.class }) @RequestBody UserSignUpReq req) {
        return ApiResponse.ok(this.authService.signUpByGoogle(req));
    }

    @PostMapping("/check/email")
    public ApiResponse checkEmail(@Validated(UserEmail.class) @RequestBody UserSignUpReq req) {
        return ApiResponse.ok(Map.of(
            "isEmailAvailable", this.authService.isEmailAvailable(req.getEmail()),
            "email", req.getEmail()));
    }
    
    @PostMapping("/check/username")
    public ApiResponse checkUsername(@Validated(Username.class) @RequestBody UserSignUpReq req) {
        return ApiResponse.ok(Map.of(
            "isUsernameAvailable", this.authService.isUsernameAvailable(req.getUsername()),
            "username", req.getUsername()));
    }
    
    @PostMapping("/login/email") 
    public ApiResponse emailLogin(@Valid @RequestBody EmailLoginReq req) {
        return ApiResponse.ok(this.authService.emailLogin(req));
    }

    @PostMapping("/login/google")
    public ApiResponse googleLogin(@Valid @RequestBody GoogleLoginReq req) {
        UserDto user = this.authService.googleLogin(req);
        if (user == null) {
            return ApiResponse.ok("unregistered");
        }
        return ApiResponse.ok(user);
    }

    @PostMapping("/refresh-token")
    public ApiResponse refreshAccessToken(@RequestBody Map<String, String> body) {
        UserDto user = this.authService.refreshAccessToken(body.get("refreshToken"));
        return ApiResponse.ok(user);
    }
}
