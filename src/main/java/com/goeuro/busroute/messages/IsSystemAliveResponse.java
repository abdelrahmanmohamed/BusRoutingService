package com.goeuro.busroute.messages;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Created by hhmx3422 on 12/13/16.
 */
public class IsSystemAliveResponse extends Message {
    private long systemTime;

    @JsonCreator
    public IsSystemAliveResponse(String messageId) {
        super(messageId);
        this.systemTime = System.currentTimeMillis();
    }

    public long getSystemTime() {
        return systemTime;
    }
}
