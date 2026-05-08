package com.isegoria.server.global.error;

public interface ErrorCodeInterface {

  Integer getHttpStatusCode();

  String getMessage();
}