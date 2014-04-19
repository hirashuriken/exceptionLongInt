package com.example.LongInt;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import java.io.IOException;
import java.util.logging.*;
/**
 * Created by melomanka on 19.04.14.
 */
public class TestRunner {
    public static void main(String[] args) {
        try{
            LogManager.getLogManager().readConfiguration(
                    TestJUnit.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }


        Result result = JUnitCore.runClasses(TestJUnit.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
