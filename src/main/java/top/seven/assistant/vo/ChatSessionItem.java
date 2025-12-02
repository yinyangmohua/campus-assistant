package top.seven.assistant.vo;

import java.time.LocalDateTime;

public record ChatSessionItem (
        String title,
        String lastMsgPreview,
        LocalDateTime lastMsgTime,
        Integer unreadCount,
        Integer isPinned
) {}

