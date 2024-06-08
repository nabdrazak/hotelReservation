package com.lautbiru.microservices;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.CommonUtils;
import com.lautbiru.utils.DateUtils;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FindAvailableRooms extends CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(FindAvailableRooms.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void copyHandle(HttpExchange httpExchange) throws IOException{
        if("GET".equals(httpExchange.getRequestMethod())) {

            String response = "No Data Found.";

            String query = httpExchange.getRequestURI().getQuery();
            if(query == null){
                InputStream requestBody = httpExchange.getRequestBody();
                DateToBook dateToBook = objectMapper.readValue(requestBody, DateToBook.class);

                if(StringUtils.isNotBlank(dateToBook.selectedDate)) {
                    response = getAvailableRooms(dateToBook.selectedDate);
                }
            } else {
                String[] querySplit = query.split("=");

                DateToBook dateToBook = new DateToBook();
                dateToBook.selectedDate = querySplit[1];
                System.out.println(dateToBook.selectedDate);

                if(StringUtils.isNotBlank(dateToBook.selectedDate)) {
                    response = getAvailableRooms(dateToBook.selectedDate);
                }
            }

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            logger.info("Size Guest Models -> "+ GuestController.guestModels.size());
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Method not allowed";
            httpExchange.sendResponseHeaders(405, response.length());
            try (OutputStream os = httpExchange.getResponseBody()){
                os.write(response.getBytes());
            }
        }
    }

    public static boolean isValidJson(byte[] data) {
        try (InputStream byteArrayStream = new ByteArrayInputStream(data)){
            objectMapper.readTree(byteArrayStream);
//            byteArrayStream.reset();
            return true;
        } catch (JsonMappingException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAvailableRooms(String bookingDate) {
        logger.info("Inside Get Available Rooms ");

        StringBuilder listAvailableRooms = new StringBuilder("\n");
        int rooms = Integer.parseInt(props.getProperty("total.hotel.rooms"));
        Map<Integer, Integer> availableRooms = new HashMap<>();

        for (int i=0; i<rooms; i++) {
            availableRooms.put(i, i);
        }
        logger.info("Currently Processing Available Rooms");
        for (Map.Entry<String, GuestModel> entry : GuestController.guestModels.entrySet()) {
            Date bookedDate = entry.getValue().getDate();
            int roomNo = entry.getValue().getRoomNo();

            SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.BOOKED_DATE_PATTERN);
            String formattedDate = dateFormat.format(bookedDate);

            //TODO maybe need to match with regex
            logger.info("Booking Date -> " +bookingDate+ " Formatted Date -> "+formattedDate+ " With room -> "+roomNo);
            if(StringUtils.equals(bookingDate, formattedDate)) {
                logger.info("Inside Remove booked rooms. Room Number to be removed -> "+roomNo);
                availableRooms.remove(roomNo);
            }
        }

        for (Map.Entry room : availableRooms.entrySet()) {
            listAvailableRooms.append("Room Number " +room.getValue()+ "\n");
        }
        logger.info("List of " +availableRooms.size()+ " Available Rooms " +listAvailableRooms);
        return listAvailableRooms.toString();
    }

    static class DateToBook {
        String selectedDate;

        public String getSelectedDate() {
            return selectedDate;
        }

        public void setSelectedDate(String selectedDate) {
            this.selectedDate = selectedDate;
        }
    }
}
