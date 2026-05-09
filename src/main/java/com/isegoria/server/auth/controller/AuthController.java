package com.isegoria.server.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isegoria.server.auth.service.AuthService;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.message.ResponseMessage;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * 회원가입
   * 
   * @param UserRequest (String email, String username, String password)
   * @return UserResponse (Long id, String username, String email, String
   *         avatarUrl, LocalDateTime createdAt)
   */
  @PostMapping("register")
  public Api<UserResponse> register(@Valid @RequestBody UserRequest request) {
    UserResponse response = authService.register(request);
    return Api.OK(response, ResponseMessage.REGISTER_SUCCESS);
  }
}
