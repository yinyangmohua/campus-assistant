package top.seven.assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.seven.assistant.entity.ChatMessage;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}

