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
@TableName("t_user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_number")
    private String userNumber;

    @TableField("password")
    private String password;

    @TableField("mobile")
    private String mobile;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("sdept")
    private String sdept;

    @TableField("user_type")
    private Integer userType;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("updated_time")
    private LocalDateTime updatedTime;

    @TableLogic(value = "0", delval = "1")
    @TableField("deleted")
    private Boolean deleted;
}
