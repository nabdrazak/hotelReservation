package com.lautbiru.validator;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JSONValidator {

    public static boolean validate(byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream byteArrayStream = new ByteArrayInputStream(data)){
            objectMapper.readTree(byteArrayStream);
            return true;
        } catch (JsonMappingException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
