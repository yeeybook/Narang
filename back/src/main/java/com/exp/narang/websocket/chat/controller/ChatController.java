package com.exp.narang.websocket.chat.controller;

import com.exp.narang.websocket.chat.model.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    // @EnableWebSocketMessageBroker를 통해서 등록되는 bean
    private final SimpMessagingTemplate template;
    // /server 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/chat")
    // 1) /server에서 메시지를 받고, /client로 메시지를 보내줍니다.
    public void sendMessage(ChatModel chatModel) {
        Long roomId = chatModel.getRoomId();
        log.debug(chatModel.toString());
        // /client/roomId 로 메시지를 반환합니다. 프론트에서 구독한 endpoint 경로이름
        template.convertAndSend("/from/chat/" + roomId, chatModel);
    }
}
