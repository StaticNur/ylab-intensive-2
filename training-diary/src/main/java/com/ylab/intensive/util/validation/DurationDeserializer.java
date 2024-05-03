package com.ylab.intensive.util.validation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ylab.intensive.exception.InvalidInputException;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Pattern;

/**
 * Deserializes a JSON string representing a duration into a Duration object.
 * The expected format of the JSON string is "H:M:S" where H represents hours, M represents minutes, and S represents seconds.
 * Hours can be any positive integer, while minutes and seconds must be between 0 and 59.
 * If the input JSON string does not match the expected format, an InvalidInputException is thrown.
 */
public class DurationDeserializer extends JsonDeserializer<Duration> {

    /**
     * Deserializes the JSON string representing a duration into a Duration object.
     *
     * @param jsonParser             The JSON parser.
     * @param deserializationContext The deserialization context.
     * @return A Duration object representing the parsed duration.
     * @throws IOException            If an I/O error occurs while reading the JSON string.
     * @throws InvalidInputException If the input JSON string does not match the expected format.
     */
    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String durationStr = jsonParser.getText().trim();
        Pattern durationPattern = Pattern.compile("^([0-9]+):([0-5]?[0-9]):([0-5]?[0-9])$");

        if (durationPattern.matcher(durationStr).matches()) {
            String[] durationHMS = durationStr.split(":");
            return Duration.ofHours(Integer.parseInt(durationHMS[0]))
                    .plusMinutes(Integer.parseInt(durationHMS[1]))
                    .plusSeconds(Integer.parseInt(durationHMS[2]));
        } else {
            throw new InvalidInputException("Формат должен быть H:M:S где часы это любое натуральное число," +
                                            " а минуты и секунды - значения от 0 до 59");
        }
    }
}
