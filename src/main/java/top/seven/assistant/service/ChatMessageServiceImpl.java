package top.seven.assistant.service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.seven.assistant.common.constant.RedisKey;
import top.seven.assistant.common.constant.RequestCode;
import top.seven.assistant.common.exception.BusinessException;
import top.seven.assistant.entity.ChatMessage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static top.seven.assistant.common.constant.RedisKey.CHAT_THREAD;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl {
    private final ReactAgent reactAgent;
    private final DashScopeChatModel chatModel;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisService redisService;

    public ChatMessage chat(String userId, Long sessionId, String msg) {
        String threadId = createThreadId(userId);
        RunnableConfig config = RunnableConfig.builder()
                .threadId(threadId)
                .addMetadata("user_id", userId)
                .build();
        ChatMessage userMsg = ChatMessage.builder()
                .sessionId(sessionId)
                .sender(0)
                .content(msg)
                .build();
        saveMessage(userMsg);
        AssistantMessage response;
        try {
            response = reactAgent.call(msg, config);
        } catch (GraphRunnerException e) {
            throw new BusinessException(RequestCode.AGENT_ERROR, e.getMessage());
        }
        ChatMessage aiMsg = ChatMessage.builder()
                .sessionId(sessionId)
                .sender(1)
                .content(response.getText())
                .build();
        saveMessage(aiMsg);
        return aiMsg;
    }

    public String createThreadId(String userId) {
        String threadId = redisService.hGet(CHAT_THREAD.key(), userId);
        if (threadId == null) {
            threadId = UUID.randomUUID().toString();
            redisService.hSet(CHAT_THREAD.key(), userId, threadId);
        }
        return threadId;
    }

    public void saveMessage(ChatMessage msg) {
        String day = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String key = RedisKey.CHAT_MSG.format(msg.getSessionId(), day);
        String msgId = String.valueOf(redisService.getId(RedisKey.CHAT_MSG.format("inc", day)));

        redisService.hSet(key, msgId, msg);
        redisService.expire(key, 86400);
    }
}
