package com.ylab.intensive.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.exception.InvalidInputException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * This class provides utility methods for converting between JSON and Java objects,
 * as well as extracting integer values from servlet requests.
 * It utilizes the Jackson ObjectMapper for JSON serialization and deserialization.
 */
@Log4j2
public class Converter {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new Converter with the specified ObjectMapper.
     *
     * @param objectMapper The ObjectMapper instance used for JSON serialization and deserialization.
     */
    public Converter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Reads the request body from the given HttpServletRequest and converts it into an instance of the specified class.
     *
     * @param <T>       The type of the object to convert to.
     * @param request   The HttpServletRequest containing the request body.
     * @param valueType The class of the object to convert to.
     * @return An instance of the specified class representing the request body.
     * @throws InvalidInputException if the input is not correctly formatted or cannot be parsed.
     */
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
            throw new InvalidInputException("Not correct input! " + e.getMessage());
        }
    }

    /**
     * Converts the given object to its JSON representation.
     *
     * @param object The object to convert to JSON.
     * @return The JSON representation of the object.
     * @throws RuntimeException if there is an error during JSON serialization.
     */
    public String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Retrieves the integer value of the specified parameter from the given HttpServletRequest.
     * If the parameter value cannot be parsed as an integer, logs a warning and returns 0.
     *
     * @param req       The HttpServletRequest containing the parameter.
     * @param paramName The name of the parameter.
     * @return The integer value of the parameter, or 0 if the parameter value cannot be parsed.
     */
    public int getInteger(HttpServletRequest req, String paramName) {
        return Optional.ofNullable(req.getParameter(paramName)).map(p -> {
            try {
                return Integer.parseInt(p);
            } catch (NumberFormatException e) {
                log.info("wrong value of {}: {}", paramName, p);
            }
            return 0;
        }).orElse(0);
    }
}
