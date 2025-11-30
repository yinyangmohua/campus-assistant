package top.seven.assistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatSessionItem {
    private String title;
    private String lastMsgPreview;
    private Integer unreadCount;
    private LocalDateTime updatedTime;
}

