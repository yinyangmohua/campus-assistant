package top.seven.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("t_chat_message")
public class ChatMessage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("session_id")
    private Long sessionId;

    @TableField("sender")
    private Integer sender;

    @TableField("content")
    private String content;

    @TableField("created_time")
    private LocalDateTime createdTime;
}

