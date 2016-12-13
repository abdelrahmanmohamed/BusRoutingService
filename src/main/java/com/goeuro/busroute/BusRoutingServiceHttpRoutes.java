package com.goeuro.busroute;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.util.Timeout;
import com.goeuro.busroute.messages.FindRoute;
import com.goeuro.busroute.messages.FindRouteResponse;
import com.goeuro.busroute.messages.IsSystemAlive;
import com.goeuro.busroute.messages.IsSystemAliveResponse;
import scala.concurrent.duration.Duration;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

import static akka.pattern.PatternsCS.ask;


/**
 * Created by Abdelrahman Mohamed Sayed on 12/11/16.
 */
public class BusRoutingServiceHttpRoutes extends AllDirectives {
    private final Timeout timeout;
    private final ActorRef busRouteFinder;
    private final LoggingAdapter log;
    private final ActorSystem system;
    private CompletionStage<ServerBinding> binding;

    public BusRoutingServiceHttpRoutes(ActorSystem system, ActorRef busRouteFinder, LoggingAdapter log) {
        this.log = log;
        this.system = system;
        this.busRouteFinder = busRouteFinder;
        this.timeout = new Timeout(Duration.create(10, "seconds"));
    }

    public Route createHttpRoutes() {
        return route(createIsConnectedRoute(), createStopRoute(), createHeartBeatRoute());
    }

    private Route createIsConnectedRoute() {
        return get(() -> pathPrefix("api", () -> path("direct", () -> parameter(StringUnmarshallers.INTEGER, "dep_sid",
                departure -> parameter(StringUnmarshallers.INTEGER, "arr_sid", arrival ->
                        getBusRoute(arrival, departure))))));
    }

    private Route createStopRoute() {
        return get(() -> path("stop", () -> {
            CompletionStage<Void> stopFuture = binding
                    .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                    .thenAccept(unbound -> {
                        log.info("stop server...");
                        system.terminate();
                    });
            return onSuccess(() -> stopFuture, response -> completeOK("{}", Jackson.marshaller()));

        })); // and shutdown when done);
    }

    private Route createHeartBeatRoute() {
        return get(() -> path("heartbeat", () ->
                {
                    CompletionStage<IsSystemAliveResponse> findRouteResponseFuture = ask(busRouteFinder,
                            new IsSystemAlive(UUID.randomUUID().toString()),
                            timeout).thenApply((IsSystemAliveResponse.class::cast));
                    return onSuccess(() -> findRouteResponseFuture, response ->
                            completeOK(response, Jackson.marshaller()));
                }
        ));
    }

    private Route getBusRoute(int arrival, int departure) {
        log.info("Request Received:departure=[{}],arrival=[{}]", departure, arrival);
        try {
            CompletionStage findRouteResponseFuture = ask(busRouteFinder,
                    new FindRoute(UUID.randomUUID().toString(), departure, arrival),
                    timeout).thenApply((FindRouteResponse.class::cast));
            return onSuccess(() -> findRouteResponseFuture, response -> completeOK(response, Jackson.marshaller()));
        } catch (Exception e) {
            e.printStackTrace();
            return complete("System Failed");
        }
    }

    public void setBinding(CompletionStage<ServerBinding> binding) {
        this.binding = binding;
    }
}
