package com.goeuro.busroute.workers;

import akka.actor.UntypedActor;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.goeuro.busroute.messages.DataChangedNotice;
import com.goeuro.busroute.messages.FindRoute;
import com.goeuro.busroute.messages.FindRouteResponse;


/**
 * Created by hhmx3422 on 12/11/16.
 */
public class RouteFinderWorker extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final File dataFile;
    private int[][] routes;

    public RouteFinderWorker(File dataFile) {
        this.dataFile = dataFile;
        loadData();
    }

    private void loadData() {
        log.info("loading file data");
        try {
            Scanner dateFileReader = new Scanner(new FileInputStream(dataFile));
            int busRoutesCount = Integer.parseInt(dateFileReader.nextLine());
            routes = new int[busRoutesCount][];
            for (int i = 0; i < busRoutesCount; i++) {
                String[] row = dateFileReader.nextLine().split(" ");
                int stations[] = new int[row.length - 1];
                for (int z = 1; z < row.length; z++) {
                    stations[z - 1] = Integer.parseInt(row[z]);
                }
                Arrays.sort(stations);
                routes[i]=stations;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex, "in data loading");
        }
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof DataChangedNotice) {
            loadData();
        } else if (message instanceof FindRoute) {
            FindRoute command = (FindRoute) message;
            boolean routeExist = false;
            for (int stations[] : routes) {
                int arrivalStationIndex = Arrays.binarySearch(stations, command.getArrival());
                int departureStationIndex = Arrays.binarySearch(stations, command.getDeparture());
                if (arrivalStationIndex > -1 && departureStationIndex > -1) {
                    routeExist = true;
                    break;
                }
            }
            getSender().tell(new FindRouteResponse(command.getDeparture(), command.getArrival(), routeExist), getSelf());
        }
    }

}
