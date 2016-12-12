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
import java.io.FileNotFoundException;
import java.io.PrintWriter;


/**
 * Created by hhmx3422 on 12/12/16.
 */
public class TestSample2 extends Common {

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create("system");
        system.log().info("Generate Test Data");
        int routesCount = 1000;
        int maxStationsNumberInRoute = 1000;
        system.log().info("Test Data Generatation Finished");
        super.setup("sample2.txt", routesCount, maxStationsNumberInRoute);
    }

    @Test
    public void testDeparture0Arrival1Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=0&arr_sid=1"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(0, 1, true));
    }

    @Test
    public void testDeparture1004Arrival1005Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=1004&arr_sid=1005"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(1004, 1005, true));
    }

    @Test
    public void testDeparture2006Arrival3006NotConnected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=2006&arr_sid=3006"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(2006, 3006, false));
    }

    @After
    public void teardown() {
        super.teardown(dataFile);
    }
}
