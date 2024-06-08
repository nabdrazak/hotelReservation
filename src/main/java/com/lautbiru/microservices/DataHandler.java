package com.lautbiru.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if("POST".equals(httpExchange.getRequestMethod())) {
            logger.info("Handling Post Request for /data endpoint");

            InputStream requestBody = httpExchange.getRequestBody();
            DataInput dataInput = objectMapper.readValue(requestBody, DataInput.class);

            logger.info("Received data: name={}, age={} --> "+dataInput.getName() +"  "+ dataInput.getAge());

            String response = "Received data: name = " +dataInput.getName()+ ", age = " +dataInput.getAge();
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

    private static class DataInput {
        private String name;

        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
