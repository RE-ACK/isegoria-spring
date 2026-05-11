package com.isegoria.server.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.repository.UserRepository;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 회원가입
   * 
   * @param UserRequest
   *                    - String email,
   *                    - String username,
   *                    - String password
   * @return UserResponse
   *         - Long id,
   *         - String username,
   *         - String email,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @Override
  public UserResponse register(UserRequest request) {
    String email = request.getEmail();
    User user = this.findByEmail(email);
    if (user != null) {
      throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    User newUser = UserRequest.toEntity(request, encodedPassword);
    userRepository.save(newUser);
    UserResponse response = UserResponse.fromEntity(newUser);
    return response;
  }

  /**
   * 이메일로 사용자 조회
   * 
   * @param String email
   * @return User Entity
   *         - Long id,
   *         - String username,
   *         - String email,
   *         - String password,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @Override
  public User findByEmail(String email) {
    User user = userRepository.findByEmail(email);
    return user;
  }

  @Override
  public User findById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    return user;
  }
}
