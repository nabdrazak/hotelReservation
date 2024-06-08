package com.lautbiru.microservices;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GuestRegistrationTest {

    @Test
    public void testHandler() throws IOException {
        GuestRegistration guestRegistration = new GuestRegistration();
        HttpExchange httpExchange = mock(HttpExchange.class);

        String requestBody = "{\"name\":\"Mohsin Abdel\",\"date\":\"2024-07-25\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(httpExchange.getRequestBody()).thenReturn(inputStream);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        guestRegistration.copyHandle(httpExchange);

        String response = "Received data: name = Mohsin Abdel, date = 2024-07-25";
        assertEquals(outputStream.toString(), response);
    }

    @Test
    public void testHandlerUnsupportedMethod() throws IOException {
        GuestRegistration guestRegistration = new GuestRegistration();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        guestRegistration.copyHandle(httpExchange);

        String response = "Method not allowed";
        assertEquals(response, outputStream.toString());
    }
}