package com.lautbiru.microservices;


import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DataHandlerTest {

    @Test
    public void testHandleMethod() throws IOException {
        DataHandler dataHandler = new DataHandler();
        HttpExchange httpExchange = mock(HttpExchange.class);

        String requestBody = "{\"name\":\"John Doe\",\"age\":30}";
        InputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getRequestMethod()).thenReturn("POST");
        when(httpExchange.getRequestBody()).thenReturn(byteArrayInputStream);
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        dataHandler.handle(httpExchange);

        String response = outputStream.toString();

        String expected = "Received data: name = John Doe, age = 30";
        assertEquals(expected, response);
    }

    @Test
    public void testHandleUnsuppoertedMethod() throws IOException{
        DataHandler dataHandler = new DataHandler();

        HttpExchange httpExchange = mock(HttpExchange.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getResponseBody()).thenReturn(outputStream);

        dataHandler.handle(httpExchange);

        String response = outputStream.toString();

        String expected = "Method not allowed";
        assertEquals(expected, response);
    }

}