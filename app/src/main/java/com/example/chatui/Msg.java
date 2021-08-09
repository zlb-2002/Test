package com.example.chatui;

public class Msg {

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;

    private String content;
    private String ip;
    private int type;
    private String time;

    public Msg(String content, String ip, int type, String time) {
        this.content = content;
        this.type = type;
        this.ip = ip;
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

}
