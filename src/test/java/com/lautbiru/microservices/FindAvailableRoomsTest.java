package com.lautbiru.microservices;

import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.DateUtils;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FindAvailableRoomsTest {

    @Test
    public void testHandleMethod() throws IOException {
        FindAvailableRooms findAvailableRooms = new FindAvailableRooms();
//        String requestBody = "{\n"+" \"selectedDate\":\"2024-07-25\"}";
        String requestBody = "{\n" +
                "    \"selectedDate\":\"2024-07-25\"\n" +
                "}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange httpExchange = mock(HttpExchange.class);

        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(httpExchange.getRequestMethod()).thenReturn("GET");

        addHotelGuest();

        URI testUri = URI.create("http://localhost:9192/GetAvailableRooms");
        when(httpExchange.getRequestURI()).thenReturn(testUri);

        findAvailableRooms.copyHandle(httpExchange);

        String expected = "\nRoom Number 0\n" +
                "Room Number 1\n" +
                "Room Number 3\n" +
                "Room Number 5\n" +
                "Room Number 6\n" +
                "Room Number 7\n" +
                "Room Number 8\n" +
                "Room Number 9\n" +
                "Room Number 10\n" +
                "Room Number 11\n" +
                "Room Number 12\n" +
                "Room Number 13\n" +
                "Room Number 14\n" +
                "Room Number 15\n" +
                "Room Number 17\n" +
                "Room Number 18\n" +
                "Room Number 19\n" +
                "Room Number 20\n" +
                "Room Number 21\n" +
                "Room Number 22\n" +
                "Room Number 23\n" +
                "Room Number 24\n";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testHandleUnsupportedMethod() throws IOException {
        FindAvailableRooms microservice = new FindAvailableRooms();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getResponseBody()).thenReturn(outputStream);

        microservice.copyHandle(exchange);

        String expectedResponse = "Method not allowed";

        assertEquals(expectedResponse, outputStream.toString());
    }

    @Test
    public void newTest() throws IOException {
        FindAvailableRooms microservice = new FindAvailableRooms();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange httpExchange = mock(HttpExchange.class);

        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(httpExchange.getRequestMethod()).thenReturn("GET");

        URI testUri = URI.create("http://localhost:9192/GetAvailableRooms?selectedDate=2024-07-25");
        when(httpExchange.getRequestURI()).thenReturn(testUri);
        addHotelGuest();

        microservice.copyHandle(httpExchange);

        String expected = "\nRoom Number 0\n" +
                "Room Number 1\n" +
                "Room Number 3\n" +
                "Room Number 5\n" +
                "Room Number 6\n" +
                "Room Number 7\n" +
                "Room Number 8\n" +
                "Room Number 9\n" +
                "Room Number 10\n" +
                "Room Number 11\n" +
                "Room Number 12\n" +
                "Room Number 13\n" +
                "Room Number 14\n" +
                "Room Number 15\n" +
                "Room Number 17\n" +
                "Room Number 18\n" +
                "Room Number 19\n" +
                "Room Number 20\n" +
                "Room Number 21\n" +
                "Room Number 22\n" +
                "Room Number 23\n" +
                "Room Number 24\n";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testDateEmpty() throws IOException {
        FindAvailableRooms availableRooms = new FindAvailableRooms();
        String requestBody = "{\"selectedDate\":}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange exchange = mock(HttpExchange.class);
        when(exchange.getRequestMethod()).thenReturn("GET");

        URI testUri = URI.create("http://localhost:9192/GetAvailableRooms");
        when(exchange.getRequestURI()).thenReturn(testUri);
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(exchange.getResponseBody()).thenReturn(outputStream);

        availableRooms.copyHandle(exchange);

        String expectedResponse = "Invalid Json format. Please review format pattern";
        assertEquals(expectedResponse, outputStream.toString());
    }

    public void addHotelGuest() {
        GuestController.guestModels = new HashMap<>();
        createMockGuestModels(GuestController.guestModels, "BIrT4e0K", "Najib Assadok", 2, "2024-07-25");
        createMockGuestModels(GuestController.guestModels, "nbUPeYbd", "Wong Mew Chew", 4, "2024-07-25");
        createMockGuestModels(GuestController.guestModels, "78wjthNu", "Wong Mew Chew", 16, "2024-07-25");

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