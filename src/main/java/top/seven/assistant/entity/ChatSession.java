package top.seven.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("t_chat_session")
public class ChatSession {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("title")
    private String title;

    @TableField("last_msg_preview")
    private String lastMsgPreview;

    @TableField("last_msg_time")
    private LocalDateTime lastMsgTime;

    @TableField("unread_count")
    private Integer unreadCount;

    @TableField("is_pinned")
    private Integer isPinned;

    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableLogic(value = "0", delval = "1")
    @TableField("deleted")
    private Boolean deleted;
}

