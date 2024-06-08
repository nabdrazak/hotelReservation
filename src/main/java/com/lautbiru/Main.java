package com.lautbiru;

import com.lautbiru.microservices.DataHandler;
import com.lautbiru.microservices.FindAvailableRooms;
import com.lautbiru.microservices.GuestRegistration;
import com.lautbiru.microservices.RetrieveAllGuests;
import com.lautbiru.microservices.RetrieveGuestDetailsById;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(9192), 0);
            server.createContext("/registerGuest", new MainServices());
            server.createContext("/data", new DataHandler());
            server.createContext("/retrieveGuestDetailsById", new MainServices());
            server.createContext("/GetAvailableRooms", new MainServices());
            server.createContext("/retrieveAllGuests", new MainServices());
            server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            logger.info("server started at http://localhost:9192");
            server.start();
        } catch (IOException e) {
            logger.info("An exception was thrown" +e.getMessage());
        }
    }
}