package service;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.AvailabilityInterval;
import model.RequestResult;
import util.IntervalCounterSingleton;
import util.RequestCountSingleton;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class AvailabilityIntervalService {
    private Double maxRuntime;
    private Double minAccessLevel;

    private static boolean isCorrect(RequestResult requestResult, Double runtimeLimit) {
        return !(requestResult.getRuntime() > runtimeLimit ||
                (requestResult.getStatusCode() >= 500 && requestResult.getStatusCode() <= 600));
    }

    public Stream<AvailabilityInterval> process(Stream<String> lines) {
        IntervalCounterSingleton intervalCounterSingleton = IntervalCounterSingleton.getInstance();
        Set<AvailabilityInterval> availabilityIntervals = new HashSet<>();
        AtomicReference<OffsetDateTime> currentIntervalDateTime = new AtomicReference<>();
        AtomicBoolean isCurrentRequestCorrect = new AtomicBoolean(false);
        lines.map(RequestResult::parse)
                .filter(Objects::nonNull)
                .peek(r -> RequestCountSingleton.getInstance().increment(r.getRuntime()))
                .forEach(r -> {
                    currentIntervalDateTime.set(r.getDateTime());
                    isCurrentRequestCorrect.set(isCorrect(r, maxRuntime));
                    if (!isCurrentRequestCorrect.get()) {
                        intervalCounterSingleton.addRequestResult(r);
                    } else {
                        Optional.ofNullable(intervalCounterSingleton.getAvailabilityIntervalAndClear())
                                .ifPresent(availabilityIntervals::add);
                    }
                });
        Optional.ofNullable(intervalCounterSingleton.getAvailabilityIntervalAndClear())
                .ifPresent(availabilityIntervals::add);
        return availabilityIntervals.stream()
                .filter(i -> i.getAvailabilityLevel() < minAccessLevel)
                .sorted(Comparator.comparing(AvailabilityInterval::getStartDate));
    }

    public String toMessage(AvailabilityInterval availabilityInterval) {
        return String.format(Locale.US, "%s\t%s\t%.1f",
                availabilityInterval.getStartDate().toLocalTime(),
                availabilityInterval.getEndDate().toLocalTime(),
                availabilityInterval.getAvailabilityLevel());
    }
}
