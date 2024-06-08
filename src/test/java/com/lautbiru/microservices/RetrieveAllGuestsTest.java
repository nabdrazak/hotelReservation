package com.lautbiru.microservices;

import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.DateUtils;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetrieveAllGuestsTest {

    @Test
    public void testHandle() throws IOException {
        RetrieveAllGuests microservice = new RetrieveAllGuests();
        HttpExchange httpExchange = mock(HttpExchange.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(httpExchange.getRequestMethod()).thenReturn("GET");

        addHotelGuest();
        microservice.copyHandle(httpExchange);

        String response = "[bookingId='BIrT4e0K', name='Najib Assadok', roomNo='2', date='Thu Jul 25 00:00:00 MYT 2024']\n" +
                "[bookingId='nbUPeYbd', name='Wong Mei Chew', roomNo='4', date='Thu Jul 25 00:00:00 MYT 2024']\n" +
                "[bookingId='78wjthNu', name='Christopher Hutchinson', roomNo='16', date='Thu Jul 25 00:00:00 MYT 2024']\n";

        assertEquals(outputStream.toString(), response);
    }

    @Test
    public void testHandleUnsupportedMethod() throws IOException {
        RetrieveAllGuests retrieveAllGuests = new RetrieveAllGuests();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange exchange = mock(HttpExchange.class);

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getResponseBody()).thenReturn(outputStream);

        retrieveAllGuests.copyHandle(exchange);

        String expectedResponse = "Method not allowed";

        assertEquals(expectedResponse, outputStream.toString());
    }

    public void addHotelGuest() {
        GuestController.guestModels = new HashMap<>();
        createMockGuestModels(GuestController.guestModels, "BIrT4e0K", "Najib Assadok", 2, "2024-07-25");
        createMockGuestModels(GuestController.guestModels, "nbUPeYbd", "Wong Mei Chew", 4, "2024-07-25");
        createMockGuestModels(GuestController.guestModels, "78wjthNu", "Christopher Hutchinson", 16, "2024-07-25");

    }

    public void createMockGuestModels(Map<String, GuestModel> guestModels, String bookingId, String name, int roomNo, String date) {
        GuestModel guestModel = new GuestModel();
        GuestController guestController = new GuestController(guestModel, new GuestView());
        guestController.setBookingId(bookingId);
        guestController.setGuestName(name);
        guestController.setRoomNumber(roomNo);
        guestController.setBookingDate(DateUtils.createDate(date));
        guestModels.put(guestModel.getBookingId(), guestModel);
    }
}