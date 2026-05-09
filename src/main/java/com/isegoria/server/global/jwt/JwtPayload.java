package com.isegoria.server.global.jwt;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPayload {
  private UUID id;
}
