package com.isegoria.server.server.request;

import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.request.UserRequest;
import jakarta.validation.constraints.NotBlank;

public record JoinServerRequest(

        @NotBlank(message = "초대 코드를 입력해주세요.")
        String inviteCode
) {}

