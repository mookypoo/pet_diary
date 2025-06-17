package com.mooky.pet_diary.domain.user.auth;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.mooky.pet_diary.domain.user.User;
import com.mooky.pet_diary.domain.user.User.SignUpType;
import com.mooky.pet_diary.domain.user.auth.dto.EmailLoginReq;
import com.mooky.pet_diary.domain.user.auth.dto.GoogleLoginReq;
import com.mooky.pet_diary.domain.user.auth.dto.UserDto;
import com.mooky.pet_diary.domain.user.auth.dto.UserSignUpReq;
import com.mooky.pet_diary.domain.user.repository.UserRepository;
import com.mooky.pet_diary.global.config.AppConfig;
import com.mooky.pet_diary.global.exception.AuthException;
import com.mooky.pet_diary.global.exception.InUseException;
import com.mooky.pet_diary.global.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final GoogleTokenService googleTokenService;
    private final AppConfig config;

    /**
     * check if email has already been used to sign up
     * @return true if available (= not in use)
     */
    public boolean isEmailAvailable(String email) {
        return this.userRepository.existsByEmail(email);
    }

    /**
     * check if username is in use
     * @return true if available (= not in use)
     */
    public boolean isUsernameAvailable(String username) {
        return !this.userRepository.existsByUsername(username);
    }

    private UserDto generateUserDto(Long userId) {
        String accessToken = this.jwtService.generateAccessToken(userId);
        String refreshToken = this.jwtService.generateRefreshToken(userId);
        return new UserDto(userId, accessToken, refreshToken);
    }
    
    public UserDto signUpByEmail(UserSignUpReq req) {
        if (this.userRepository.existsByEmail(req.getEmail())) {
            throw InUseException.resource("email", req.getEmail());
        }

        if (this.userRepository.existsByUsername(req.getUsername())) {
            throw InUseException.resource("username", req.getUsername());
        }

        User userReq = new User.Builder().agreedMarketingTerms(req.isAgreeToMarketing())
                .email(req.getEmail())
                .username(req.getUsername())
                .password(this.encryptPW(req.getPassword()))
                .signupType(SignUpType.EMAIL)
                .build();

        User savedUser = this.userRepository.save(userReq);
        return this.generateUserDto(savedUser.getId());
    }

    public UserDto signUpByGoogle(UserSignUpReq req) {
        // google already checks existsByEmail before sign up page
        if (this.userRepository.existsByUsername(req.getUsername())) {
            throw InUseException.resource("username", req.getUsername());
        }

        Payload payload = this.googleTokenService.verifyGoogleIdToken(req.getGoogleIdToken());

        User userReq = new User.Builder().agreedMarketingTerms(req.isAgreeToMarketing())
                .email(payload.getEmail())
                .username(req.getUsername())
                .ssoId(payload.getSubject())
                .signupType(SignUpType.GOOGLE)
                .build();

        User savedUser = this.userRepository.save(userReq);
        return this.generateUserDto(savedUser.getId());
    }

    
    public UserDto emailLogin(EmailLoginReq req) {
        User user = this.userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> {
                    throw AuthException.invalidLogin("이메일을 찾을 수 없습니다", req.getEmail(), "로그인 실패");
                });
        boolean hasCorrectPw = this.doesEncryptedMatchValue(req.getPassword(), user.getPassword());
        if (!hasCorrectPw) {
            throw AuthException.invalidLogin("잘못된 비밀번호입니다", req.getEmail(), "로그인 실패");
        }
        return this.generateUserDto(user.getId());
    }

    private String encryptPW(String pw) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(this.config.getBCryptStrength());
        String result = encoder.encode(pw);
        assert (encoder.matches(pw, result));
        return result;
    }

    private boolean doesEncryptedMatchValue(String value, String encrypted) {
        return new BCryptPasswordEncoder(this.config.getBCryptStrength()).matches(value, encrypted);
    }

    /**
     * returns null if the user has not signed up yet --> requires sign up process
     */
    public UserDto googleLogin(GoogleLoginReq req) {
        // verify user exists
        Optional<User> userOpt = this.userRepository.findByEmail(req.getEmail());
        // new user - sav
        if (!userOpt.isPresent()) {
            return null;
        } else {
            return this.generateUserDto(userOpt.get().getId());
        }
    }

    public UserDto refreshAccessToken(String refreshToken) {
        Long userId = this.jwtService.getUserIdFromToken(refreshToken, false);
        return this.generateUserDto(userId);
    }

}
