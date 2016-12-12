package com.goeuro.busroute.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by hhmx3422 on 12/11/16.
 */
public class FindRouteResponse implements Serializable {
    private int departure;
    private int arrival;
    private boolean directBusRoute;

    @JsonCreator
    public FindRouteResponse(int departure,
                             int arrival,
                             boolean directBusRoute) {
        this.departure = departure;
        this.arrival = arrival;
        this.directBusRoute = directBusRoute;
    }

    @JsonProperty("dep_sid")
    public int getDeparture() {
        return departure;
    }

    @JsonProperty("arr_sid")
    public int getArrival() {
        return arrival;
    }

    @JsonProperty("direct_bus_route")
    public boolean isDirectBusRoute() {
        return directBusRoute;
    }

    @Override
    public String toString() {
        return "FindRouteResponse{" +
                "departure=" + departure +
                ", arrival=" + arrival +
                ", directBusRoute=" + directBusRoute +
                '}';
    }
}
