package com.ylab.intensive.util.converter;

import com.ylab.intensive.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Converter {
    /**
     * Converts a string to a specific type using the provided parser function.
     *
     * @param str          The string to convert.
     * @param parser       The function that parses the string into the desired type.
     * @param errorMessage The error message to be used if the conversion fails.
     * @param <T>          The type to which the string should be converted.
     * @return The converted value of type T.
     * @throws InvalidInputException If the conversion fails, indicating invalid input.
     */
    public  <T> T convert(String str, Function<String, T> parser, String errorMessage) {
        try {
            return parser.apply(str);
        } catch (Exception e) {
            throw new InvalidInputException(errorMessage);
        }
    }
}
