package com.goeuro.busroute.messages;

import java.io.Serializable;

/**
 * Created by hhmx3422 on 12/11/16.
 */
public class Message implements Serializable{
    private String messageId;

    public Message(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
