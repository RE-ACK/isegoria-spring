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
  ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST.value(), "잘못된 인자가 전달되었습니다."),
  ILLEGAL_STATE(HttpStatus.BAD_REQUEST.value(), "잘못된 상태에서 메서드가 호출되었습니다."),
  DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT.value(), "데이터 무결성 위반 오류입니다."),

  // JWT 관련 에러
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 액세스 토큰입니다."),

  // User 관련 에러
  USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다.");

  private final Integer httpStatusCode;
  private final String message;
}
