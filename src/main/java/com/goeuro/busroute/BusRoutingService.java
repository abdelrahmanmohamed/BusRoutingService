package com.goeuro.busroute;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.LoggingAdapter;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.routing.FromConfig;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.goeuro.busroute.messages.CheckDataChanges;
import com.goeuro.busroute.workers.RouteFinderWorker;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Created by Abdelrahman Mohamed Sayed on 12/11/16.
 */
public class BusRoutingService {
    public static void main(String[] args) throws IOException, InterruptedException {
        ActorSystem system = ActorSystem.create("system");
        LoggingAdapter log = system.log();

        if (args.length < 1) {
            log.error("missing file param");
            system.terminate();
            return;
        }
        String fileName = args[0];
        File dataFile = new File(fileName);
        if (!dataFile.exists()) {
            log.error("data file path is invalid");
            system.terminate();
        } else {
//            ActorRef busRouteFinder = system.actorOf(Props.create(RouteFinderWorker.class, dataFile));
            ActorRef router1 =
                    system.actorOf(FromConfig.getInstance().props(Props.create(RouteFinderWorker.class,dataFile)),
                            "router1");
            final Http http = Http.get(system);
            final ActorMaterializer materializer = ActorMaterializer.create(system);
            BusRoutingServiceHttpRoutes reservationSystemRoutes =
                    new BusRoutingServiceHttpRoutes(system, router1,
                            system.log());
            Route httpRoutes = reservationSystemRoutes.createHttpRoutes();
            final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = httpRoutes
                    .flow(system, materializer);
            final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                    ConnectHttp.toHost("localhost", 80), materializer);
            reservationSystemRoutes.setBinding(binding);
            binding.whenComplete((serverBinding, throwable) ->
                    log.info("Server online at http://localhost:80/\nPress stop call http://localhost:80/stop..."));
        }
    }
}
