package com.goeuro.busroute.messages;

/**
 * Created by Abdelrahman Mohamed Sayed on 12/11/16.
 */
public class FindRoute extends Message {
    private final int arrival;
    private final int departure;

    public FindRoute(String messageId, int departure, int arrival) {
        super(messageId);
        this.departure = departure;
        this.arrival = arrival;
    }

    public int getArrival() {
        return arrival;
    }

    public int getDeparture() {
        return departure;
    }
}
