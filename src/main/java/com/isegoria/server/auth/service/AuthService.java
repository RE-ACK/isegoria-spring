package com.isegoria.server.auth.service;

import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

public interface AuthService {

  UserResponse register(UserRequest request);

}
