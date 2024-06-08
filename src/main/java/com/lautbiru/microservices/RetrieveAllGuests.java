package com.lautbiru.microservices;

import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.CommonUtils;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class RetrieveAllGuests extends CommonUtils {

    public void copyHandle(HttpExchange httpExchange) throws IOException {
        if("GET".equals(httpExchange.getRequestMethod())) {
            StringBuilder sb = new StringBuilder();

            System.out.println("\n\n");
            for (Map.Entry<String, GuestModel> entry: GuestController.guestModels.entrySet()) {
                GuestModel guestModel = entry.getValue();
                GuestController guestController = new GuestController(guestModel, new GuestView());
                guestController.viewBooking();
                sb.append(guestController);
                sb.append("\n");
            }
            String response = sb.toString();
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        } else {
            String response = "Method not allowed";
            httpExchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = httpExchange.getResponseBody()){
                os.write(response.getBytes());
            }
        }
    }
}
