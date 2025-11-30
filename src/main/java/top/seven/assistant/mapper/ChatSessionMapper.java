package top.seven.assistant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.seven.assistant.model.ChatSession;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
}

