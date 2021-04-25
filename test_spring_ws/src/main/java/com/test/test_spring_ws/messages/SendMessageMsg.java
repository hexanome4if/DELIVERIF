package com.test.test_spring_ws.messages;

public class SendMessageMsg {
    private String user_name;
    private String message;

    public SendMessageMsg(String user_name, String message) {
        this.user_name = user_name;
        this.message = message;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
