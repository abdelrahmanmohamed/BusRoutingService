package com.gouro.busroute;

import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.testkit.TestRouteResult;
import com.goeuro.busroute.messages.FindRouteResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by Abdelrahman Mohamed Sayed on 12/12/16.
 */
public class TestSample3 extends Common {

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create("system");
        system.log().info("Generate Test Data");
        int maxRoutesNumber = 100000;
        int stationsNumberInRoute = 10;
        system.log().info("Test Data Generation Finished");
        super.setup("sample3.txt", maxRoutesNumber, stationsNumberInRoute);
    }

    @Test
    public void testDeparture0Arrival1Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=0&arr_sid=1"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(0, 1, true));
    }

    @Test
    public void testDeparture1011Arrival1010Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=1011&arr_sid=1010"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(1011, 1010, true));
    }

    @Test
    public void testDeparture989Arrival1000NotConnected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=989&arr_sid=1000"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(989, 1000, false));
    }

    @After
    public void teardown() {
        super.teardown(dataFile);
    }
}
