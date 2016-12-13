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
 * Created by Abdelrahman Mohamed Sayed on 12/12/16.
 */
public class TestSample1 extends Common {
    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create("system");
        system.log().info("Generate Test Data");
        dataFile = new File("sample1.txt");
        PrintWriter pw = new PrintWriter(dataFile);
        /*
        * 3
          0 0 1 2 3 4
          1 3 1 6 5
          2 0 6 4*/
        pw.println(3);
        pw.println("0 0 1 2 3 4");
        pw.println("1 3 1 6 5");
        pw.println("2 0 6 4");
        pw.close();
        system.log().info("Test Data Generation Finished");
        super.setup();
    }

    @Test
    public void testDeparture0Arrival1Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=0&arr_sid=1"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class),new FindRouteResponse(0,1,true));
    }

    @Test
    public void testDeparture4Arrival3Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=4&arr_sid=3"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class),new FindRouteResponse(4,3,true));
    }

    @Test
    public void testDeparture3Arrival5Connected() {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=3&arr_sid=5"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class),new FindRouteResponse(3,5,true));
    }

    @After
    public void teardown() {
        super.teardown(dataFile);
    }
}
