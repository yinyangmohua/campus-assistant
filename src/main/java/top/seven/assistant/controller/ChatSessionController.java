package top.seven.assistant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.seven.assistant.vo.ChatSessionDetail;
import top.seven.assistant.vo.ChatSessionItem;
import top.seven.assistant.common.model.Result;
import top.seven.assistant.service.ChatSessionServiceImpl;

@RestController
@RequestMapping("/api/v1/sessions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionServiceImpl chatSessionService;

    @PostMapping
    public Result<Long> createSession(@RequestBody CreateReq req) {
        Long id = chatSessionService.createSession(req.getUserId(), req.getTitle());
        return Result.success(id);
    }

    @GetMapping
    public Result<java.util.List<ChatSessionItem>> listSessions(@RequestParam("userId") Long userId) {
        return Result.success(chatSessionService.getListSessions(userId));
    }

    @GetMapping("/{id}/detail")
    public Result<ChatSessionDetail> getDetail(@PathVariable("id") Long sessionId) {
        return Result.success(chatSessionService.getSessionDetail(sessionId));
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteSession(@PathVariable("id") Long sessionId) {
        chatSessionService.deleteSession(sessionId);
        return Result.success("删除成功");
    }

    @PostMapping("/{id}/pin")
    public Result<String> pinSession(@PathVariable("id") Long sessionId, @RequestBody PinReq req) {
        chatSessionService.pinSession(req.getUserId(), sessionId);
        return Result.success("置顶成功");
    }

    @PostMapping("/{id}/rename")
    public Result<String> renameSession(@PathVariable("id") Long sessionId, @RequestBody RenameReq req) {
        chatSessionService.renameSession(sessionId, req.getTitle());
        return Result.success("会话名称修改成功");
    }

    @Data
    public static class CreateReq {
        private Long userId;
        private String title;
    }

    @Data
    public static class PinReq {
        private Long userId;
    }

    @Data
    public static class RenameReq {
        private String title;
    }
}
