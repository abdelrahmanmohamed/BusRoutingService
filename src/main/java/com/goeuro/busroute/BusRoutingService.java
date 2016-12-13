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
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.goeuro.busroute.messages.CheckDataChanges;
import com.goeuro.busroute.workers.DataChangeWorker;
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
            final Http http = Http.get(system);
            ActorRef busRouteFinder = system.actorOf(Props.create(RouteFinderWorker.class, dataFile));
            system.actorOf(Props.create(DataChangeWorker.class, dataFile, busRouteFinder))
                    .tell(new CheckDataChanges(UUID.randomUUID().toString()), ActorRef.noSender());
            final ActorMaterializer materializer = ActorMaterializer.create(system);
            BusRoutingServiceHttpRoutes reservationSystemRoutes =
                    new BusRoutingServiceHttpRoutes(system, busRouteFinder,
                            system.log());
            Route httpRoutes = reservationSystemRoutes.createHttpRoutes();
            final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = httpRoutes
                    .flow(system, materializer);
            final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                    ConnectHttp.toHost("localhost", 8088), materializer);
            reservationSystemRoutes.setBinding(binding);
            binding.whenComplete((serverBinding, throwable) ->
                    log.info("Server online at http://localhost:8088/\nPress stop call http://localhost:8088/stop..."));
        }
    }
}
