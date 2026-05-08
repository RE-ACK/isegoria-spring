package com.isegoria.server.global.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeInterface {
  OK(HttpStatus.OK.value(), "성공"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 요청입니다."),
  SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러가 발생했습니다."),
  NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), "널 포인트 에러가 발생했습니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증이 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근이 거부되었습니다."),
  ;

  private final Integer httpStatusCode;
  private final String message;
}
