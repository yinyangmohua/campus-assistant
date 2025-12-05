package top.seven.assistant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.seven.assistant.common.model.Result;
import top.seven.assistant.entity.ChatMessage;
import top.seven.assistant.service.ChatMessageServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatMessageController {
    private final ChatMessageServiceImpl chatMessageService;

    @PostMapping
    public Result<ChatMessage> chat(@RequestBody ChatReq req) {
        ChatMessage msg = chatMessageService.chat(req.userId, req.sessionId, req.message );
        return Result.success(msg);
    }

    @GetMapping("/history/{userId}")
    public Map<String, Object> getHistory(@PathVariable String userId) {
        String threadId = userId;
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
        private Long sessionId;
        private String message;
    }
}