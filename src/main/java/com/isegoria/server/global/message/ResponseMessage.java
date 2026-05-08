package com.isegoria.server.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage implements ResponseMessageInterface {
  // LOGIN_SUCCESS("로그인 하였습니다.") 예시
  ;

  private final String message;
}
