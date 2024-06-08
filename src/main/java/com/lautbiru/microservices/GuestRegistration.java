package com.lautbiru.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.CommonUtils;
import com.lautbiru.utils.DateUtils;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GuestRegistration extends CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(GuestRegistration.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void copyHandle(HttpExchange httpExchange) throws IOException {
        if("POST".equals(httpExchange.getRequestMethod())) {
            InputStream requestBody = httpExchange.getRequestBody();
            GuestInput dataInput = objectMapper.readValue(requestBody, GuestInput.class);

            logger.info("Received data: name={}, date ={} --> "+dataInput.getName() +"  "+ dataInput.getDate());

            String response = "Received data: name = " +dataInput.getName()+ ", date = " +dataInput.getDate();

            addGuest(dataInput.name, dataInput.date);

            httpExchange.sendResponseHeaders(200, response.length());

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            logger.info("Response sent : {} "+response);
        } else {
            String response = "Method not allowed";
            httpExchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = httpExchange.getResponseBody()){
                os.write(response.getBytes());
            }
        }
    }

    //TODO handle NullPointerException
    private void addGuest(String name, String date) {
        boolean validBookId = false;
        boolean isRoomExist = false;
        String bookingId;
        int roomNo;

        List<String> bookingNumbers = new ArrayList();
        List<Integer> bookedRooms = new ArrayList<>();
        for (Map.Entry<String, GuestModel> entry : GuestController.guestModels.entrySet()) {
            bookingNumbers.add(entry.getValue().getBookingId());
            bookedRooms.add(entry.getValue().getRoomNo());
        }

        do {
            bookingId = RandomStringUtils.random(8, true, true);
            if(!bookingNumbers.contains(bookingId))
                validBookId = true;
        } while (!validBookId);

        do {
            logger.info("Number of total rooms -> "+props.getProperty("total.hotel.rooms"));
            roomNo = new Random().nextInt(Integer.parseInt(props.getProperty("total.hotel.rooms")));
            if(!bookedRooms.contains(roomNo))
                isRoomExist = true;
        } while (!isRoomExist);

        GuestModel guestModel = new GuestModel();
        GuestController guestController = new GuestController(guestModel, new GuestView());
        guestController.setBookingId(bookingId);
        guestController.setGuestName(name);
        guestController.setRoomNumber(roomNo);
        guestController.setBookingDate(DateUtils.createDate(date));
        GuestController.guestModels.put(guestModel.getBookingId(), guestModel);
    }

    static class GuestInput {
        private String name;

        private String date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
