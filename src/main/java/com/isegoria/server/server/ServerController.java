package com.isegoria.server.server;

import com.isegoria.server.global.api.Api;
import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.request.CreateServerRequest;
import com.isegoria.server.server.request.JoinServerRequest;
import com.isegoria.server.server.response.InviteCodeResponse;
import com.isegoria.server.server.response.ServerResponse;
import com.isegoria.server.server.service.ServerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //HTTP 요청 받는걸 명시한다
@RequestMapping("/server") //모든 URL 앞에 /server 붙는다
@RequiredArgsConstructor //ServerService를 자동 붙힌다
public class ServerController {

    private final ServerService serverService;

    // POST /servers — 서버 생성
    @PostMapping
    public Api<ServerResponse> createServer(
            @AuthenticationPrincipal Long userId, //userid 추출
            @Valid @RequestBody CreateServerRequest request) {
        //검증, JSON 바디 -> Java 객체로 변환

        Server server = serverService.createServer(userId, request.name(), request.iconUrl());
        return Api.OK(ServerResponse.from(server));
    }

    // POST /servers/join — 초대 코드로 서버 입장
    @PostMapping("/join")
    public Api<ServerResponse> joinServer(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody JoinServerRequest request) {

        Server server = serverService.joinServer(userId, request.inviteCode());
        return Api.OK(ServerResponse.from(server));
    }

    // GET /servers/my — 내 서버 목록
    @GetMapping("/my")
    public Api<List<ServerResponse>> getServerList(
            @AuthenticationPrincipal Long userId) {

        List<ServerResponse> responses = serverService.getServerList(userId)
                .stream()
                .map(ServerResponse::from)
                .toList();
        return Api.OK(responses);
    }

    // DELETE /servers/{serverId} — 서버 삭제
    @DeleteMapping("/{serverId}")
    public Api<Void> deleteServer(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long serverId) {

        serverService.deleteServer(userId, serverId);
        return Api.OK(null);
    }

    // DELETE /servers/{serverId}/leave — 서버 나가기
    @DeleteMapping("/{serverId}/leave")
    public Api<Void> leaveServer(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long serverId) {

        serverService.leaveServer(userId, serverId);
        return Api.OK(null);
    }

    // DELETE /servers/{serverId}/members/{targetUserId} — 멤버 추방
    @DeleteMapping("/{serverId}/members/{targetUserId}")
    public Api<Void> kickMember(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long serverId,
            @PathVariable Long targetUserId) {

        serverService.kickMember(userId, serverId, targetUserId);
        return Api.OK(null);
    }

    // POST /servers/{serverId}/invite — 초대 코드 재발급
    @PostMapping("/{serverId}/invite")
    public Api<InviteCodeResponse> regenerateInviteCode(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long serverId) {

        InviteCodeResponse newCode = serverService.regenerateInviteCode(userId, serverId);
        return Api.OK(newCode);
    }
}