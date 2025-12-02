package top.seven.assistant.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.*;
import top.seven.assistant.common.constant.RequestCode;
import top.seven.assistant.common.exception.BusinessException;
import top.seven.assistant.common.model.AssistantResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatMessageController {

    private final ReactAgent reactAgent;
    private final DashScopeChatModel chatModel;

    // 存储用户的 threadId 映射
    private final Map<String, String> userThreadMap = new HashMap<>();

    @PostMapping
    public AssistantResponse chat(@RequestBody ChatReq req) {
        String userId = req.getUserId();
        String message = req.getMessage();
        // 为每个用户生成或获取 threadId
        String threadId = userThreadMap.computeIfAbsent(userId, k -> UUID.randomUUID().toString());
        // 创建配置
        RunnableConfig config = RunnableConfig.builder()
                .threadId(threadId)
                .addMetadata("user_id", userId)
                .build();
        // 调用 Agent，得到 AssistantMessage
        AssistantMessage response;
        try {
            response = reactAgent.call(message, config);
        } catch (GraphRunnerException e) {
            throw new BusinessException(RequestCode.AGENT_ERROR, e.getMessage());
        }
        // 构建返回结果,实际返回结果应该进行定制，比如回答类型、建议等
        AssistantResponse assistantResponse = new AssistantResponse();
        assistantResponse.setAnswer(response.getText());
        assistantResponse.setType("general");
        assistantResponse.setSuggestion("请根据你的需求，给出一个建议");
        assistantResponse.setNeedsFurtherHelp(false);
        return assistantResponse;
    }

    @GetMapping("/history/{userId}")
    public Map<String, Object> getHistory(@PathVariable String userId) {
        String threadId = userThreadMap.get(userId);
        if (threadId == null) {
            return Map.of("error", "未找到该用户的历史记录");
        }
        // 从MemorySaver中获取历史
        // 这里简化处理，实际应该实现完整的history接口
        return Map.of("userId", userId, "threadId", threadId, "history", "历史记录功能需要进一步实现");
    }

    @Data
    public static class ChatReq {
        private String userId;
        private String message;
    }
}