package com.example.chatui;

import org.litepal.crud.LitePalSupport;

public class Record extends LitePalSupport {

    private String content;
    private String time;
    private String ip;
    private int type;

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getIp() {
        return ip;
    }

    public int getType() {
        return type;
    }
}
