package com.goeuro.busroute.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by hhmx3422 on 12/11/16.
 */
public class FindRouteResponse implements Serializable {
    private final int departure;
    private final int arrival;
    private final boolean directBusRoute;

    @JsonCreator
    public FindRouteResponse(@JsonProperty("dep_sid") int departure,
                             @JsonProperty("arr_sid") int arrival,
                             @JsonProperty("direct_bus_route") boolean directBusRoute) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FindRouteResponse)) return false;

        FindRouteResponse that = (FindRouteResponse) o;

        if (getDeparture() != that.getDeparture()) return false;
        if (getArrival() != that.getArrival()) return false;
        return isDirectBusRoute() == that.isDirectBusRoute();
    }

    @Override
    public int hashCode() {
        int result = getDeparture();
        result = 31 * result + getArrival();
        result = 31 * result + (isDirectBusRoute() ? 1 : 0);
        return result;
    }
}
