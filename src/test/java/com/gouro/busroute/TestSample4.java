package com.gouro.busroute;

import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.testkit.TestRouteResult;
import com.goeuro.busroute.messages.FindRouteResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;


/**
 * Created by hhmx3422 on 12/12/16.
 */
public class TestSample4 extends Common {
    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create("system");
        system.log().info("Generate Test Data");
        dataFile = new File("sample4.txt");
        PrintWriter pw = new PrintWriter(dataFile);
        /*
        * 1
          0 11 12*/
        pw.println(1);
        pw.println("0 11 12");
        pw.close();
        system.log().info("Test Data Generatation Finished");
        super.setup();
    }

    @Test
    public void testDeparture11Arrival12Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=11&arr_sid=12"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class),new FindRouteResponse(11,12,true));
    }

    @Test
    public void testDeparture4Arrival3NodesDoesNotExist() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=4&arr_sid=3"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class),new FindRouteResponse(4,3,false));
    }

    @After
    public void teardown() {
        super.teardown(dataFile);
    }
}
