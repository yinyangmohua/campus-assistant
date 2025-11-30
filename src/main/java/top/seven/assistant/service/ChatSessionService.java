package top.seven.assistant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import top.seven.assistant.exception.BusinessException;
import top.seven.assistant.mapper.ChatSessionMapper;
import top.seven.assistant.model.ChatSession;
import top.seven.assistant.model.ChatSessionItem;
import top.seven.assistant.model.RequestCode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatSessionService {

    private final ChatSessionMapper chatSessionMapper;

    public ChatSessionService(ChatSessionMapper chatSessionMapper) {
        this.chatSessionMapper = chatSessionMapper;
    }

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

    public void deleteSession(Long sessionId) {
        ChatSession s = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .eq(ChatSession::getDeleted, 0)
                .last("limit 1"));
        if (s == null) {
            throw new BusinessException(RequestCode.SESSION_NOT_FOUND);
        }
        chatSessionMapper.update(null, new LambdaUpdateWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .set(ChatSession::getDeleted, 1));
    }

    public void pinSession(Long userId, Long sessionId) {
        ChatSession s = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .eq(ChatSession::getDeleted, 0)
                .last("limit 1"));
        if (s == null) {
            throw new BusinessException(RequestCode.SESSION_NOT_FOUND);
        }
        if (s.getIsPinned() == 1) {
            throw new BusinessException(RequestCode.SESSION_IS_PINNED);
        }
        Long count = chatSessionMapper.selectCount(new LambdaQueryWrapper<ChatSession>()
                .eq(ChatSession::getUserId, userId)
                .eq(ChatSession::getDeleted, 0)
                .eq(ChatSession::getIsPinned, 1));
        if (count >= 3 && s.getIsPinned() != null && s.getIsPinned() == 0) {
            throw new BusinessException(RequestCode.PIN_LIMIT_EXCEEDED);
        }
        chatSessionMapper.update(null, new LambdaUpdateWrapper<ChatSession>()
                .eq(ChatSession::getId, sessionId)
                .set(ChatSession::getIsPinned, 1));
    }

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

//    public List<ChatSessionItem> listSessions(Long userId) {
//        List<ChatSession> sessions = chatSessionMapper.selectList(new LambdaQueryWrapper<ChatSession>()
//                .eq(ChatSession::getUserId, userId)
//                .eq(ChatSession::getDeleted, false)
//                .orderByDesc(ChatSession::getLastMsgTime));
//        return sessions.stream()
//                .map(s -> new ChatSessionItem(
//                        s.getTitle(),
//                        s.getLastMsgPreview(),
//                        s.getUnreadCount(),
//                        s.getUpdatedTime()
//                ))
//                .collect(Collectors.toList());
//    }
}
