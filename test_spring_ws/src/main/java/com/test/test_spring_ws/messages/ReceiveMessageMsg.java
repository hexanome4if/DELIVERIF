package com.test.test_spring_ws.messages;

public class ReceiveMessageMsg {

    private String message;

    public ReceiveMessageMsg() {
    }

    public ReceiveMessageMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
