package com.lautbiru.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.CommonUtils;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RetrieveGuestDetailsById extends CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(RetrieveGuestDetailsById.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void copyHandle(HttpExchange httpExchange) throws IOException {
        if("GET".equals(httpExchange.getRequestMethod())) {

            String query = httpExchange.getRequestURI().getQuery();

            String response = "No Data Found.";
            if(query == null ) {
                InputStream requestBody = httpExchange.getRequestBody();
                BookingId bookingId = objectMapper.readValue(requestBody, BookingId.class);

                if(StringUtils.isNotBlank(bookingId.bookedId)) {
                    GuestModel guestModel = GuestController.guestModels.get(bookingId.bookedId);
                    if(guestModel != null)
                        response = guestModel.toString();
                }
            } else {
                String[] requestDetails = query.split("=");
                BookingId bookingId = new BookingId();

                bookingId.bookedId = requestDetails[1];
                System.out.println("requestId -> "+bookingId.bookedId);

                GuestModel guestModel = GuestController.guestModels.get(bookingId.bookedId);
                if(guestModel != null)
                    response = guestModel.toString();
            }

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            logger.info("Size Customer Models -> "+ GuestController.guestModels.size());
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

    static class BookingId {
        private String bookedId;

        public String getBookedId() {
            return bookedId;
        }

        public void setBookedId(String bookedId) {
            this.bookedId = bookedId;
        }
    }
}
