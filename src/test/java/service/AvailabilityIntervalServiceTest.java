package service;

import model.AvailabilityInterval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class AvailabilityIntervalServiceTest {
    AvailabilityIntervalService availabilityIntervalService = new AvailabilityIntervalService(45D, 99D);

    @Test
    @DisplayName("process test")
    void processTest() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/test1.log")));

        AvailabilityInterval availabilityInterval1 = new AvailabilityInterval();
        availabilityInterval1.setStartDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 47, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval1.setEndDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 47, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval1.setTotalCount(6L);
        availabilityInterval1.setFailureCount(4L);

        AvailabilityInterval availabilityInterval2 = new AvailabilityInterval();
        availabilityInterval2.setStartDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 49, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval2.setEndDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 51, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval2.setTotalCount(15L);
        availabilityInterval2.setFailureCount(3L);

        List<AvailabilityInterval> expected = Arrays.asList(availabilityInterval1, availabilityInterval2);
        List<AvailabilityInterval> actual = availabilityIntervalService.process(bufferedReader.lines()).collect(Collectors.toList());
        assertIterableEquals(expected, actual);

        bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/test2.log")));
        actual = availabilityIntervalService.process(bufferedReader.lines()).collect(Collectors.toList());
        assertIterableEquals(expected, actual);

        bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/test3.log")));
        actual = availabilityIntervalService.process(bufferedReader.lines()).collect(Collectors.toList());
        assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("message test")
    void toMessageTest() {
        AvailabilityInterval availabilityInterval = new AvailabilityInterval();
        availabilityInterval.setStartDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 49, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval.setEndDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 51, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval.setTotalCount(15L);
        availabilityInterval.setFailureCount(3L);

        assertEquals(availabilityIntervalService.toMessage(availabilityInterval), "16:49:02\t16:51:02\t80.0");
    }
}