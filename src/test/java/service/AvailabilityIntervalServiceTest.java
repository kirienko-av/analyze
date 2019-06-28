package service;

import model.AvailabilityInterval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class AvailabilityIntervalServiceTest {
    AvailabilityIntervalService availabilityIntervalService = new AvailabilityIntervalService(45D, 99D);

    @Test
    @DisplayName("process test")
    void processTest() {
        Stream<String> lines = Stream.of("192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1\" 500 2 23.251219 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=e356713 HTTP/1.1\" 200 2 56.164372 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6c21c8f6 HTTP/1.1\" 200 2 56.02583 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 35.249855 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=4b84a53c HTTP/1.1\" 200 2 56.783072 \"-\" \"@list-item-updater\" prio:0",

                "192.168.32.181 - - [14/06/2017:16:48:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:48:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1\" 200 2 23.251219 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:48:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=e356713 HTTP/1.1\" 200 2 23.164372 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:48:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6c21c8f6 HTTP/1.1\" 200 2 23.02583 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:48:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 35.249855 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:48:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=4b84a53c HTTP/1.1\" 200 2 23.783072 \"-\" \"@list-item-updater\" prio:0",

                "192.168.32.181 - - [14/06/2017:16:49:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:49:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6c21c8f6 HTTP/1.1\" 500 2 23.02583 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:49:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 35.249855 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:49:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=4b84a53c HTTP/1.1\" 200 2 23.783072 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:49:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:50:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=7ae28555 HTTP/1.1\" 200 2 23.251219 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:50:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=e356713 HTTP/1.1\" 200 2 23.164372 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:50:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6c21c8f6 HTTP/1.1\" 500 2 23.02583 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:50:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=cceed874 HTTP/1.1\" 200 2 35.249855 \"-\" \"@list-item-updater\" prio:0",
                "192.168.32.181 - - [14/06/2017:16:50:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=4b84a53c HTTP/1.1\" 200 2 23.783072 \"-\" \"@list-item-updater\" prio:0");

        AvailabilityInterval availabilityInterval1 = new AvailabilityInterval();
        availabilityInterval1.setStartDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 47, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval1.setEndDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 47, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval1.setTotalCount(6);
        availabilityInterval1.setFailureCount(4);

        AvailabilityInterval availabilityInterval2 = new AvailabilityInterval();
        availabilityInterval2.setStartDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 49, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval2.setEndDate(OffsetDateTime.of(LocalDateTime.of(2017, 06, 14, 16, 50, 02),
                ZoneOffset.ofHours(10)));
        availabilityInterval2.setTotalCount(10);
        availabilityInterval2.setFailureCount(2);

        List<AvailabilityInterval> expected = Arrays.asList(availabilityInterval1, availabilityInterval2);
        List<AvailabilityInterval> actual = availabilityIntervalService.process(lines).collect(Collectors.toList());
        assertIterableEquals(expected, actual);


    }

    @Test
    void toMessage() {

    }
}