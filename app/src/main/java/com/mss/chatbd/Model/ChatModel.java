package com.mss.chatbd.Model;

public class ChatModel {

    String SenderId,ReceiverId,Message,ChatId;

    public ChatModel() {
    }

    public ChatModel(String senderId, String receiverId, String message, String chatId) {
        SenderId = senderId;
        ReceiverId = receiverId;
        Message = message;
        ChatId = chatId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }
}
