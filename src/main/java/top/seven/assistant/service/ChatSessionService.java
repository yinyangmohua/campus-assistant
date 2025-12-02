package top.seven.assistant.service;

import top.seven.assistant.vo.ChatSessionDetail;
import top.seven.assistant.vo.ChatSessionItem;

import java.util.List;

public interface ChatSessionService {
    Long createSession(Long userId, String title);

    void deleteSession(Long sessionId);

    void pinSession(Long userId, Long sessionId);

    void renameSession(Long sessionId, String title);

    List<ChatSessionItem> getListSessions(Long userId);

    ChatSessionDetail getSessionDetail(Long sessionId);
}
