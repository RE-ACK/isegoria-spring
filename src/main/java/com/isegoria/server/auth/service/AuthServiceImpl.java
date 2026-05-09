package com.isegoria.server.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.isegoria.server.user.service.UserService;
import com.isegoria.server.auth.request.AuthRequest;
import com.isegoria.server.auth.response.AuthResponse;
import com.isegoria.server.auth.response.TokenResponse;
import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.global.jwt.JwtProvider;
import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  /**
   * 회원가입
   * 
   * @param UserRequest
   *                    - String email,
   *                    - String username,
   *                    - String password
   * @return UserResponse
   *         - Long id
   *         - String username,
   *         - String email,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @Override
  public UserResponse register(UserRequest request) {
    UserResponse response = userService.register(request);
    return response;
  }

  /**
   * 로그인
   * 
   * @param AuthRequest
   *                    - String email,
   *                    - String password
   * @return AuthResponse
   *         - Long id
   *         - String username
   *         - String email
   *         - String avatarUrl
   *         - LocalDateTime createdAt
   *         - TokenResponse serverTokens
   *         - String accessToken
   *         - String refreshToken
   */
  @Override
  public AuthResponse login(AuthRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();
    User user = userService.findByEmail(email);

    if (user == null) {
      throw new ApiException(ErrorCode.LOGIN_FAILED);
    }
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new ApiException(ErrorCode.LOGIN_FAILED);
    }

    TokenResponse tokens = generateTokens(user.getId());
    AuthResponse response = AuthResponse.fromEntity(user, tokens);
    return response;
  }

  /**
   * 토큰 생성
   * 
   * @param Long userId
   * @return TokenResponse
   *         - String accessToken
   *         - String refreshToken
   */
  private TokenResponse generateTokens(Long userId) {
    String accessToken = jwtProvider.createAccessToken(userId);
    String refreshToken = jwtProvider.createRefreshToken(userId);

    return TokenResponse.from(accessToken, refreshToken);
  }
}
