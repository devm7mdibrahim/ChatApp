package com.devmohamedibrahim1997.chatdemo.pojo;

public class Message {

    private String sender;
    private String receiver;
    private String message;

    public Message(String message, String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Message(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
