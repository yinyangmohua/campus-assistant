package top.seven.assistant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import top.seven.assistant.common.exception.BusinessException;
import top.seven.assistant.mapper.ChatMessageMapper;
import top.seven.assistant.mapper.ChatSessionMapper;
import top.seven.assistant.entity.ChatSession;
import top.seven.assistant.vo.ChatSessionItem;
import top.seven.assistant.vo.ChatSessionDetail;
import top.seven.assistant.entity.ChatMessage;
import top.seven.assistant.common.constant.RequestCode;
import java.util.List;

@Service
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionMapper chatSessionMapper;
    private final ChatMessageMapper chatMessageMapper;

    public ChatSessionServiceImpl(ChatSessionMapper chatSessionMapper, ChatMessageMapper chatMessageMapper) {
        this.chatSessionMapper = chatSessionMapper;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public Long createSession(Long userId, String title) {
        ChatSession s = ChatSession.builder()
                .userId(userId)
                .title(title)
                .lastMsgPreview("")
                .unreadCount(0)
                .isPinned(0)
                .build();
        chatSessionMapper.insert(s);
        return s.getId();
    }

    @Override
    public void deleteSession(Long sessionId) {
        ChatSession s = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .eq(ChatSession::getDeleted, false)
                .last("limit 1"));
        if (s == null) {
            throw new BusinessException(RequestCode.SESSION_NOT_FOUND);
        }
        chatSessionMapper.update(null, new LambdaUpdateWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .set(ChatSession::getDeleted, true));
    }

    @Override
    public void pinSession(Long userId, Long sessionId) {
        ChatSession s = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .eq(ChatSession::getDeleted, false)
                .last("limit 1"));
        if (s == null) {
            throw new BusinessException(RequestCode.SESSION_NOT_FOUND);
        }
        if (s.getIsPinned() == 1) {
            throw new BusinessException(RequestCode.SESSION_IS_PINNED);
        }
        Long count = chatSessionMapper.selectCount(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getUserId, userId)
                .eq(ChatSession::getDeleted, false)
                .eq(ChatSession::getIsPinned, 1));
        if (count >= 3) {
            throw new BusinessException(RequestCode.PIN_LIMIT_EXCEEDED);
        }
        chatSessionMapper.update(null, new LambdaUpdateWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .set(ChatSession::getIsPinned, 1));
    }

    @Override
    public void renameSession(Long sessionId, String title) {
        ChatSession s = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .eq(ChatSession::getDeleted, false)
                .last("limit 1"));
        if (s == null) {
            throw new BusinessException(RequestCode.SESSION_NOT_FOUND);
        }
        chatSessionMapper.update(null, new LambdaUpdateWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .set(ChatSession::getTitle, title));
    }

    @Override
    public List<ChatSessionItem> getListSessions(Long userId) {
        List<ChatSession> sessions = chatSessionMapper.selectList(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getUserId, userId)
                .eq(ChatSession::getDeleted, false)
                .orderByDesc(ChatSession::getIsPinned)
                .orderByDesc(ChatSession::getLastMsgTime));
        return sessions.stream()
                .map(s -> new ChatSessionItem(
                        s.getTitle(),
                        s.getLastMsgPreview(),
                        s.getLastMsgTime(),
                        s.getUnreadCount(),
                        s.getIsPinned()
                ))
                .toList();
    }

    @Override
    public ChatSessionDetail getSessionDetail(Long sessionId) {
        ChatSession s = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .eq(ChatSession::getDeleted, false)
                .last("limit 1"));
        if (s == null) {
            throw new BusinessException(RequestCode.SESSION_NOT_FOUND);
        }
        List<ChatMessage> messages = chatMessageMapper.selectList(new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getSessionId, sessionId)
                .orderByAsc(ChatMessage::getCreatedTime));
        return new ChatSessionDetail(sessionId, s.getTitle(), messages);
    }
}
