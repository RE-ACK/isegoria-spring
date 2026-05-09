package com.isegoria.server.auth.service;

import org.springframework.stereotype.Service;

import com.isegoria.server.user.service.UserService;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;

  @Override
  public UserResponse register(UserRequest request) {
    UserResponse response = userService.register(request);
    return response;
  }

}
