package com.shoppingmall.demo.controller;

import com.shoppingmall.demo.service.IChatHistoryService;
import com.shoppingmall.demo.service.common.LoginInfoService;
import com.shoppingmall.demo.utils.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Api(tags = "系统消息推送接口")
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/poll")
public class MessageController {

    // 存储等待消息的客户端
    private Map<Long, Object> waitingClients = new HashMap<>();
    private final IChatHistoryService chatHistoryService;
    private final LoginInfoService loginInfoService;

    @GetMapping
    public DeferredResult<ResponseEntity<Result>> longPoll() {
        DeferredResult<ResponseEntity<Result>> deferredResult = new DeferredResult<>(1000L, ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        Long userId = loginInfoService.getLoginId();
        // 将客户端添加到等待队列中
        waitingClients.put(userId, deferredResult);

        // 等待新消息
        deferredResult.onTimeout(() -> {
            log.info("time-out");
            // 超时后返回无内容
            if (waitingClients.containsKey(userId)) {
                DeferredResult<ResponseEntity<Result>> client = (DeferredResult<ResponseEntity<Result>>) waitingClients.get(userId);
                client.setResult(ResponseEntity.ok(Result.success("time-out"))); // 将消息返回给客户端
            }
            waitingClients.remove(userId);

        });

        deferredResult.onCompletion(() -> {
            // 响应完成后移除客户端
            waitingClients.remove(userId);
        });

        // 模拟延迟和消息接收
        ForkJoinPool.commonPool().submit(() -> {
            try {

                Long unreadMsgCount = chatHistoryService.countTotalUnreadMessage(userId);
                // 这里模拟收到新消息
                String newMessage = "Unread message count: " + unreadMsgCount;
                log.info("用户{}未读消息数{}", userId, unreadMsgCount);

                // 找到相应的等待客户端并推送消息
                if (waitingClients.containsKey(userId)) {
                    DeferredResult<ResponseEntity<Result>> client = (DeferredResult<ResponseEntity<Result>>) waitingClients.get(userId);
                    client.setResult(ResponseEntity.ok(Result.success(newMessage))); // 将消息返回给客户端
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });

        return deferredResult;
    }

//    @GetMapping()
//    public DeferredResult<ResponseEntity<?>> handleReqDefResult(Model model) {
//        log.info("Received async-deferredresult request");
//        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
//
//        ForkJoinPool.commonPool().submit(() -> {
//            log.info("Processing in separate thread");
//            output.setResult(ResponseEntity.ok("ok"));
//        });
//
//        log.info("servlet thread freed");
//        return output;
//    }


}
