package com.isegoria.server.server.service;

import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.server.entity.MemberRole;
import com.isegoria.server.server.entity.Server;
import com.isegoria.server.server.entity.ServerMember;
import com.isegoria.server.server.repository.ServerMemberRepository;
import com.isegoria.server.server.repository.ServerRepository;
import com.isegoria.server.server.response.InviteCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;

    // ───────────────────────────────────────────
    // 서버 생성
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public Server createServer(Long ownerId, String name, String iconUrl) {
        Server server = Server.builder()
                .name(name)
                .ownerId(ownerId)
                .inviteCode(generateInviteCode())
                .iconUrl(iconUrl)
                .build();
        serverRepository.save(server);

        ServerMember ownerMember = ServerMember.builder()
                .server(server)
                .userId(ownerId)
                .role(MemberRole.OWNER)
                .build();
        serverMemberRepository.save(ownerMember);

        return server;
    }

    // ───────────────────────────────────────────
    // 초대 코드로 서버 입장
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public Server joinServer(Long userId, String inviteCode) {
        Server server = serverRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ApiException(ErrorCode.INVITE_CODE_NOT_FOUND));

        if (serverMemberRepository.existsByServerAndUserId(server, userId)) {
            throw new ApiException(ErrorCode.ALREADY_JOINED);
        }

        ServerMember newMember = ServerMember.builder()
                .server(server)
                .userId(userId)
                .role(MemberRole.MEMBER)
                .build();
        serverMemberRepository.save(newMember);

        return server;
    }

    // ───────────────────────────────────────────
    // 내 서버 목록 조회
    // ───────────────────────────────────────────
    @Transactional(readOnly = true)
    @Override
    public List<Server> getServerList(Long userId) {
        return serverRepository.findServersByUserId(userId);
    }

    // ───────────────────────────────────────────
    // 서버 나가기 (OWNER 불가)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public void leaveServer(Long userId, Long serverId) {
        Server server = findServerById(serverId);

        ServerMember member = serverMemberRepository.findByServerAndUserId(server, userId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() == MemberRole.OWNER) {
            throw new ApiException(ErrorCode.OWNER_CANNOT_LEAVE);
        }

        serverMemberRepository.deleteByServerAndUserId(server, userId);
    }

    // ───────────────────────────────────────────
    // 멤버 추방 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public void kickMember(Long ownerId, Long serverId, Long targetUserId) {
        Server server = findServerById(serverId);

        ServerMember requester = serverMemberRepository.findByServerAndUserId(server, ownerId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (requester.getRole() != MemberRole.OWNER) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        if (ownerId.equals(targetUserId)) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        serverMemberRepository.findByServerAndUserId(server, targetUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        serverMemberRepository.deleteByServerAndUserId(server, targetUserId);
    }

    // ───────────────────────────────────────────
    // 초대 코드 재발급 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public InviteCodeResponse regenerateInviteCode(Long ownerId, Long serverId) {
        Server server = findServerById(serverId);

        ServerMember requester = serverMemberRepository.findByServerAndUserId(server, ownerId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (requester.getRole() != MemberRole.OWNER) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        String newCode = generateInviteCode();
        server.updateInviteCode(newCode);

        return new InviteCodeResponse(newCode);
    }

    // ───────────────────────────────────────────
    // 서버 삭제 (OWNER만 가능)
    // ───────────────────────────────────────────
    @Transactional
    @Override
    public void deleteServer(Long ownerId, Long serverId) {
        Server server = findServerById(serverId);

        ServerMember requester = serverMemberRepository.findByServerAndUserId(server, ownerId)
                .orElseThrow(() -> new ApiException(ErrorCode.MEMBER_NOT_FOUND));

        if (requester.getRole() != MemberRole.OWNER) {
            throw new ApiException(ErrorCode.NO_PERMISSION);
        }

        serverRepository.delete(server);
    }

    // ───────────────────────────────────────────
    // 헬퍼 메서드
    // ───────────────────────────────────────────
    private Server findServerById(Long serverId) {
        return serverRepository.findById(serverId)
                .orElseThrow(() -> new ApiException(ErrorCode.SERVER_NOT_FOUND));
    }

    private static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String generateInviteCode() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}