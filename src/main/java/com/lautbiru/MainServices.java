package com.lautbiru;

import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MainServices implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String inputRequest = exchange.getRequestURI().getPath();

        GuestModel guestModel = new GuestModel();
        GuestController guestController = new GuestController(guestModel, new GuestView());

        if("/registerGuest".equals(inputRequest)) {
            guestController.addGuest(exchange);
        } else if("/retrieveGuestDetailsById".equals(inputRequest)) {
            guestController.retrieveGuest(exchange);
        } else if("/GetAvailableRooms".equals(inputRequest)) {
            guestController.viewAvailableRooms(exchange);
        } else if("/retrieveAllGuests".equals(inputRequest)) {
            guestController.viewGuests(exchange);
        }
    }
}
