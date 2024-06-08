package com.lautbiru;

import com.lautbiru.controller.GuestController;
import com.lautbiru.model.GuestModel;
import com.lautbiru.utils.DateUtils;
import com.lautbiru.view.GuestView;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MainServicesTest {

    @Test
    public void testRegisterGuest() throws IOException {
        MainServices mainServices = new MainServices();

        HttpExchange httpExchange = mock(HttpExchange.class);

        String requestBody = "{\"name\":\"Mohsin Abdel\",\"date\":\"2024-07-25\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        URI testUri = URI.create("http://localhost:9192/registerGuest");
        when(httpExchange.getRequestURI()).thenReturn(testUri);

        mainServices.handle(httpExchange);

        String response = "Received data: name = Mohsin Abdel, date = 2024-07-25";
        assertEquals(outputStream.toString(), response);
    }

    @Test
    public void testRetrieveGuestDetailsById() throws IOException {
        MainServices mainServices = new MainServices();

        HttpExchange httpExchange = mock(HttpExchange.class);

        String requestBody = "{\"bookedId\":\"T7sbHjAo\"}";

        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(httpExchange.getRequestMethod()).thenReturn("GET");

        URI testUri = URI.create("http://localhost:9192/retrieveGuestDetailsById");
        when(httpExchange.getRequestURI()).thenReturn(testUri);

        addHotelGuest();
        mainServices.handle(httpExchange);

        String expected = "[bookingId='T7sbHjAo', name='Wong Mei Chew', roomNo='4', date='Thu Jul 25 00:00:00 MYT 2024']";

        assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testGetAvailableRooms() throws IOException {
        MainServices mainServices = new MainServices();

        String requestBody = "{\"selectedDate\":\"2024-07-25\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange httpExchange = mock(HttpExchange.class);

        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(httpExchange.getRequestMethod()).thenReturn("GET");

        addHotelGuest();

        URI testUri = URI.create("http://localhost:9192/GetAvailableRooms");
        when(httpExchange.getRequestURI()).thenReturn(testUri);

        mainServices.handle(httpExchange);

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
    public void testRetrieveAllGuests() throws IOException {
        MainServices mainServices = new MainServices();

        HttpExchange httpExchange = mock(HttpExchange.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getResponseBody()).thenReturn(outputStream);
        when(httpExchange.getRequestMethod()).thenReturn("GET");

        URI testUri = URI.create("http://localhost:9192/retrieveAllGuests");
        when(httpExchange.getRequestURI()).thenReturn(testUri);

        addHotelGuest();

        mainServices.handle(httpExchange);

        ArrayList<String> listExpectedOutputString = mock(ArrayList.class);
        when(listExpectedOutputString.get(0)).thenReturn("[bookingId='BIrT4e0K', name='Najib Assadok', roomNo='2', date='Thu Jul 25 00:00:00 MYT 2024']");
        when(listExpectedOutputString.get(1)).thenReturn("[bookingId='T7sbHjAo', name='Wong Mei Chew', roomNo='4', date='Thu Jul 25 00:00:00 MYT 2024']");
        when(listExpectedOutputString.get(2)).thenReturn("[bookingId='78wjthNu', name='Christopher Hutchinson', roomNo='16', date='Thu Jul 25 00:00:00 MYT 2024']");

        String outputResponse = outputStream.toString();
        assertTrue(outputResponse.contains(listExpectedOutputString.get(0)));
        assertTrue(outputResponse.contains(listExpectedOutputString.get(1)));
        assertTrue(outputResponse.contains(listExpectedOutputString.get(2)));
    }

    public void addHotelGuest() {
        GuestController.guestModels = new HashMap<>();
        createMockGuestModels(GuestController.guestModels, "BIrT4e0K", "Najib Assadok", 2, "2024-07-25");
        createMockGuestModels(GuestController.guestModels, "T7sbHjAo", "Wong Mei Chew", 4, "2024-07-25");
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