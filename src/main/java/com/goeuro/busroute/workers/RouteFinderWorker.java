package com.goeuro.busroute.workers;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.goeuro.busroute.datatstructures.DisjointSet;
import com.goeuro.busroute.messages.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by Abdelrahman Mohamed Sayed on 12/11/16.
 */
public class RouteFinderWorker extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final File dataFile;
    private Map<Integer, Integer> actualStationIdVsVirtualStationIndex;
    private DisjointSet set = new DisjointSet();

    public RouteFinderWorker(File dataFile) {
        this.dataFile = dataFile;
        loadData();
    }

    private void loadData() {
        log.info("loading file data");
        try {
            Scanner dateFileReader = new Scanner(new FileInputStream(dataFile));
            int busRoutesCount = Integer.parseInt(dateFileReader.nextLine());
            actualStationIdVsVirtualStationIndex = new HashMap<>(busRoutesCount);
            int virtualId = 0;
            for (int i = 0; i < busRoutesCount; i++) {
                String[] row = dateFileReader.nextLine().split(" ");
                for (int z = 2; z < row.length; z++) {
                    int stationId = Integer.parseInt(row[z]);
                    int previousStationId = Integer.parseInt(row[z - 1]);
                    if (!actualStationIdVsVirtualStationIndex.containsKey(stationId)) {
                        actualStationIdVsVirtualStationIndex.put(stationId, virtualId);
                        virtualId++;
                    }
                    if (!actualStationIdVsVirtualStationIndex.containsKey(previousStationId)) {
                        actualStationIdVsVirtualStationIndex.put(previousStationId, virtualId);
                        virtualId++;
                    }
                    set.union(actualStationIdVsVirtualStationIndex.get(previousStationId),
                            actualStationIdVsVirtualStationIndex.get(stationId));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex, "in data loading");
            getContext().system().terminate();
        }
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof DataChangedNotice) {
            loadData();
        } else if (message instanceof FindRoute) {
            FindRoute command = (FindRoute) message;
            int parent = set.connectedBy(actualStationIdVsVirtualStationIndex.get(command.getArrival())
                    , actualStationIdVsVirtualStationIndex.get(command.getDeparture()));
            FindRouteResponse response = new FindRouteResponse(command.getDeparture(), command.getArrival(), parent != -1);
            log.info(response.toString() + " through " + parent);
            getSender().tell(response, getSelf());
        } else if (message instanceof IsSystemAlive) {
            getSender().tell(new IsSystemAliveResponse(((IsSystemAlive) message).getMessageId()), getSelf());
        }
    }

}
