package com.test.test_spring_ws.messages;

public class SetUserNameMsg {
    private String user_name;

    public SetUserNameMsg() {
    }

    public SetUserNameMsg(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
