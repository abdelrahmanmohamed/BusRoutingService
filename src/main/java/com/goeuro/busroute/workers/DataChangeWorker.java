package com.goeuro.busroute.workers;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.goeuro.busroute.messages.CheckDataChanges;
import com.goeuro.busroute.messages.DataChangedNotice;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Abdelrahman Mohamed Sayed on 12/11/16.
 */
public class DataChangeWorker extends UntypedActor {
    private static ActorRef dataChangeWorkerInstance;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final File dataFile;
    private final ActorRef routeFinderWorker;
    private long dataFileModifiedDate;
    private DataChangeWorker(File dataFile, ActorRef routeFinderWorker) {
        this.dataFile = dataFile;
        this.routeFinderWorker=routeFinderWorker;
        this.dataFileModifiedDate = dataFile.lastModified();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof CheckDataChanges) {
            if (dataFileModifiedDate != dataFile.lastModified()) {
                dataFileModifiedDate = dataFile.lastModified();
                log.info("Data Changed");
                routeFinderWorker.tell(new DataChangedNotice(UUID.randomUUID().toString()), getSelf());
            }
            getContext().system().scheduler().scheduleOnce(Duration.create(2, TimeUnit.SECONDS),
                    getSelf(), new CheckDataChanges(UUID.randomUUID().toString()), getContext().system().dispatcher(), getSelf());
        }
    }
}
