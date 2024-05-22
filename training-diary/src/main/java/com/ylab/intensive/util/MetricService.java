package com.ylab.intensive.util;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing metrics related to greetings.
 */
@Service
public class MetricService {

    private final MeterRegistry meterRegistry;
    private final Counter greetingCount;
    private static final String GREETING_COUNT_METRIC = "greeting_count";
    private static final String GREETING_COUNT_BY_USER_METRIC = "greeting_count_by_user";

    /**
     * Constructs a MetricService with the specified MeterRegistry.
     *
     * @param meterRegistry The MeterRegistry to use for metric registration.
     */
    @Autowired
    public MetricService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.greetingCount = Counter.builder(GREETING_COUNT_METRIC)
                .description("Количество обращений")
                .register(meterRegistry);
    }

    /**
     * Increments the overall greeting count by 1.
     */
    public void incrementGreetingCount() {
        greetingCount.increment(1);
    }

    /**
     * Increments the greeting count for a specific user by 1.
     *
     * @param userEmail The email of the user to increment the count for.
     */
    public void incrementGreetingCount(String userEmail) {
        incrementGreetingCount(userEmail, 1);
    }

    /**
     * Increments the greeting count for a specific user by the specified count.
     * Increases both its own metric (GREETING_COUNT_BY_USER_METRIC) and the overall metric (GREETING_COUNT_METRIC)
     *
     * @param userEmail The email of the user to increment the count for.
     * @param count     The count by which to increment the greeting count.
     */
    public void incrementGreetingCount(String userEmail, int count) {
        incrementGreetingCount();
        Counter.builder(GREETING_COUNT_BY_USER_METRIC)
                .description("Количество обращений от пользователя")
                .tag("email", userEmail)
                .register(meterRegistry)
                .increment(count);
    }
}
