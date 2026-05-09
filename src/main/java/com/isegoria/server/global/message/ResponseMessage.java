package com.isegoria.server.global.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage implements ResponseMessageInterface {
  REGISTER_SUCCESS("회원가입이 성공적으로 완료되었습니다.");

  private final String message;
}
