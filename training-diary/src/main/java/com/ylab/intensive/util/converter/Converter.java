package com.ylab.intensive.util.converter;

import com.ylab.intensive.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class Converter {
    public  <T> T convert(String str, Function<String, T> parser, String errorMessage) {
        try {
            return parser.apply(str);
        } catch (Exception e) {
            throw new InvalidInputException(errorMessage);
        }
    }
}
