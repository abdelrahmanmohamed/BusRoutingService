package com.gouro.busroute;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import akka.testkit.JavaTestKit;
import com.goeuro.busroute.BusRoutingServiceHttpRoutes;
import com.goeuro.busroute.messages.CheckDataChanges;
import com.goeuro.busroute.workers.DataChangeWorker;
import com.goeuro.busroute.workers.RouteFinderWorker;
import org.junit.After;
import org.junit.Before;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by hhmx3422 on 12/9/16.
 */
public class Common extends JUnitRouteTest {
    static ActorSystem system;
    static TestRoute appRoute;
    static File dataFile;

    public void setup(String fileName, int routesCount, int stationsNumberInRoute) throws FileNotFoundException {
        dataFile = new File(fileName);
        writeSampleFile(routesCount, stationsNumberInRoute);
        ActorRef busRouteFinder = system.actorOf(Props.create(RouteFinderWorker.class, dataFile));
        system.actorOf(Props.create(DataChangeWorker.class, dataFile, busRouteFinder))
                .tell(new CheckDataChanges(UUID.randomUUID().toString()), ActorRef.noSender());
        appRoute = testRoute(new BusRoutingServiceHttpRoutes(system, busRouteFinder, system.log()).createHttpRoutes());
    }

    public void setup() {
        ActorRef busRouteFinder = system.actorOf(Props.create(RouteFinderWorker.class, dataFile));
        system.actorOf(Props.create(DataChangeWorker.class, dataFile, busRouteFinder))
                .tell(new CheckDataChanges(UUID.randomUUID().toString()), ActorRef.noSender());
        appRoute = testRoute(new BusRoutingServiceHttpRoutes(system, busRouteFinder, system.log()).createHttpRoutes());
    }

    public void teardown(File dataFile) {
        system.terminate();
        dataFile.delete();
    }

    private void writeSampleFile(int routesCount, int maxNumberStationsInRoute) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(dataFile);
        int data = 0;
        int routeId = 0;
        pw.println(routesCount);
        for (int i = 0; i < routesCount; i++) {
            pw.print(routeId);
            pw.print(" ");
            for (int z = 0; z < maxNumberStationsInRoute-1; z++) {
                pw.print(data);
                pw.print(" ");
                data++;
            }
            pw.println(data);
            data++;
            routeId++;
        }
        pw.close();
    }
}
