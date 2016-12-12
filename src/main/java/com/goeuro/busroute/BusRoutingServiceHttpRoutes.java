package com.goeuro.busroute;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.LoggingAdapter;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.goeuro.busroute.messages.FindRoute;
import com.goeuro.busroute.messages.FindRouteResponse;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.util.UUID;

import static akka.pattern.PatternsCS.ask;

import java.util.concurrent.CompletionStage;


/**
 * Created by hhmx3422 on 12/11/16.
 */
public class BusRoutingServiceHttpRoutes extends AllDirectives {
    private final Timeout timeout;
    private final ActorRef busRouteFinder;
    private final LoggingAdapter log;
    private final ActorSystem system;

    public BusRoutingServiceHttpRoutes(ActorSystem system, ActorRef busRouteFinder, LoggingAdapter log) {
        this.log = log;
        this.system = system;
        this.busRouteFinder = busRouteFinder;
        this.timeout = new Timeout(Duration.create(5, "seconds"));
    }

    public Route createHttpRoutes() {
        return route(get(() -> pathPrefix("api", () -> path("direct", () -> parameter(StringUnmarshallers.INTEGER, "dep_sid",
                departure -> parameter(StringUnmarshallers.INTEGER, "arr_sid", arrival ->
                        getBusRoute(arrival, departure)))))));
    }

    private Route getBusRoute(int arrival, int departure) {
        log.info("Request Received:departure=[{}],arrival=[{}]", departure, arrival);
        try {
            CompletionStage findRouteResponseFutrue = ask(busRouteFinder,
                    new FindRoute(UUID.randomUUID().toString(), departure, arrival),
                    timeout).thenApply((FindRouteResponse.class::cast));
            return onSuccess(() -> findRouteResponseFutrue, response -> {
                        log.info(response.toString());
                        return completeOK(response, Jackson.marshaller());
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            return complete("System Failed");
        }
    }
}
