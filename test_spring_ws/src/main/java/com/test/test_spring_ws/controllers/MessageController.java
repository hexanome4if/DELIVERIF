package com.test.test_spring_ws.controllers;

import com.test.test_spring_ws.messages.ReceiveMessageMsg;
import com.test.test_spring_ws.messages.SendMessageMsg;
import com.test.test_spring_ws.messages.SetUserNameMsg;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("message")
    @SendTo("/topic/message")
    public SendMessageMsg receiveMessage(ReceiveMessageMsg message, @Header("simpSessionId") String sessionId, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        System.out.println("Receive message");
        headerAccessor.setSessionId(sessionId);
        System.out.println(headerAccessor.getSessionAttributes().getOrDefault("user_name", "default"));
        if (!headerAccessor.getSessionAttributes().containsKey("user_name")) return null;
        System.out.println("Has user name");
        String userName = headerAccessor.getSessionAttributes().get("user_name").toString();
        return new SendMessageMsg(userName, message.getMessage());
    }
}
