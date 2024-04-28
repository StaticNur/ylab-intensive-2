package com.ylab.intensive.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.exception.InvalidInputException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class Converter {

    private final ObjectMapper objectMapper;

    public Converter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getRequestBody(HttpServletRequest request, Class<T> valueType) {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while (true) {
                line = reader.readLine();
                if (line != null) {
                    requestBody.append(line);
                } else break;
            }
            return objectMapper.readValue(requestBody.toString(), valueType);
        } catch (IOException e) {
            throw new InvalidInputException("Not correct input! "+e.getMessage());
        }
    }

    public String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
