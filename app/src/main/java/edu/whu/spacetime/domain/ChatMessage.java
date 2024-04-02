package edu.whu.spacetime.domain;

public class ChatMessage {
    public static final int AI = 0;
    public static final int USER = 1;

    private int role;

    private String content;

    public ChatMessage(int role, String content) {
        this.role = role;
        this.content = content;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
