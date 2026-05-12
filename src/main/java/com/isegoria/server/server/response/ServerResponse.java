package com.isegoria.server.server.response;

import com.isegoria.server.server.entity.Server;

public record ServerResponse(
        Long id,
        String name,
        String iconUrl,
        String inviteCode
) {
    // 그대로 반환하면 불필요한 데이터 노출 및 무한루프 가능성이 있다.
    // 엔티티 → DTO 변환
    public static ServerResponse from(Server server) {
        return new ServerResponse(
                server.getId(),
                server.getName(),
                server.getIconUrl(),
                server.getInviteCode()
        );
    }
}