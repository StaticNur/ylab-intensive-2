package com.ylab.intensive.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ylab.intensive.exception.InvalidInputException;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Pattern;

public class DurationDeserializer extends JsonDeserializer<Duration> {

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
