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
 * Created by Abdelrahman Mohamed Sayed on 12/12/16.
 */
public class TestDataChange extends Common {
    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create("system");
        system.log().info("Generate Test Data");
        dataFile = new File("sample4.txt");
        PrintWriter pw = new PrintWriter(dataFile);
        pw.println("1");
        pw.println("0 11 12");
        pw.close();
        system.log().info("Test Data Generation Finished");
        super.setup();
    }

    @Test
    public void testDataChange() throws FileNotFoundException, InterruptedException {
        TestRouteResult run = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=11&arr_sid=12"));
        run.assertStatusCode(200);
        run.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(11, 12, true));
        PrintWriter pw = new PrintWriter(dataFile);
        pw.println("1");
        pw.println("0 10 9");
        pw.close();
        Thread.sleep(2000);
        TestRouteResult run3 = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=11&arr_sid=12"));
        run3.assertStatusCode(200);
        run3.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(11, 12, false));

        TestRouteResult run2 = appRoute.run(HttpRequest.GET("/api/direct?dep_sid=10&arr_sid=9"));
        run2.assertStatusCode(200);
        run2.assertEntityAs(Jackson.unmarshaller(FindRouteResponse.class), new FindRouteResponse(10, 9, true));

    }

    @After
    public void teardown() {
        super.teardown(dataFile);
    }
}
