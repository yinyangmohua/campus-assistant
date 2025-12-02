package top.seven.assistant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.seven.assistant.entity.ChatMessage;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatSessionDetail {
    private Long sessionId;
    private String title;
    private List<ChatMessage> messages;
}

