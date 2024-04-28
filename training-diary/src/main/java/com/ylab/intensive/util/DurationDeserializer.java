package com.ylab.intensive.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String durationStr = jsonParser.getText().trim();
        String[] durationHMS = durationStr.split(":");
        return Duration.ofHours(Integer.parseInt(durationHMS[0]))
                .plusMinutes(Integer.parseInt(durationHMS[1]))
                .plusSeconds(Integer.parseInt(durationHMS[2]));
    }
}
