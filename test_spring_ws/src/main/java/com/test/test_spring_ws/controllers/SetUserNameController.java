package com.test.test_spring_ws.controllers;

import com.test.test_spring_ws.messages.SetUserNameMsg;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class SetUserNameController {

    @MessageMapping("set_username")
    @SendToUser("/queue/set_username_response")
    public SetUserNameMsg setUserName(SetUserNameMsg message, @Header("simpSessionId") String sessionId, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        System.out.println("Received set username");
        headerAccessor.setSessionId(sessionId);
        headerAccessor.getSessionAttributes().put("user_name", message.getUser_name());
        return message;
    }
}
