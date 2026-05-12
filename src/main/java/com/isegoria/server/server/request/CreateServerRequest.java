package com.isegoria.server.server.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//데이터만 담는 class임.
//DTO
public record CreateServerRequest(

        @NotBlank(message = "서버 이름을 입력해주세요.")
        @Size(max = 100, message = "서버 이름은 100자 이하로 입력해주세요.")
        String name,

        String iconUrl    // 선택사항, null 가능
) {}