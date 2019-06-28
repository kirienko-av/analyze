package service;

import lombok.AllArgsConstructor;
import lombok.Data;
import model.AvailabilityInterval;
import model.RequestResult;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class AvailabilityIntervalService {
    private Double maxRuntime;
    private Double minAvailabilityLevel;

    private boolean isCorrect(RequestResult requestResult) {
        return !(requestResult.getRuntime() > maxRuntime ||
                (requestResult.getStatusCode() >= 500 && requestResult.getStatusCode() <= 600));
    }

    public Stream<AvailabilityInterval> process(Stream<String> lines) {
        AtomicReference<AvailabilityInterval> previousInterval = new AtomicReference<>(null);
        AtomicReference<AvailabilityInterval> currentInterval = new AtomicReference<>(null);

        BiFunction<Set<AvailabilityInterval>, RequestResult, Set<AvailabilityInterval>> accumulator
                = (availabilityIntervals, requestResult) -> {

            if (currentInterval.get() == null)
                currentInterval.set(add(new AvailabilityInterval(), requestResult));
            else {
                if (currentInterval.get().getEndDate().equals(requestResult.getDateTime())) {
                    currentInterval.set(add(currentInterval.get(), requestResult));
                } else {
                    if (currentInterval.get().getAvailabilityLevel() < minAvailabilityLevel) {
                        if (previousInterval.get() == null)
                            previousInterval.set(currentInterval.getAndSet(add(new AvailabilityInterval(), requestResult)));
                        else {
                            previousInterval.set(add(currentInterval.getAndSet(add(new AvailabilityInterval(), requestResult)), previousInterval.get()));
                        }
                    } else {
                        if (previousInterval.get() != null) {
                            availabilityIntervals.add(previousInterval.getAndSet(null));
                            currentInterval.set(add(new AvailabilityInterval(), requestResult));
                        }
                    }
                }
            }
            return availabilityIntervals;
        };

        return Stream.concat(lines.map(RequestResult::parse)
                        .filter(Objects::nonNull)
                        .reduce(new HashSet<>(), accumulator, (x, y) -> null).stream(),
                Stream.of(add(Optional.ofNullable(previousInterval.get())
                                .filter(i -> i.getAvailabilityLevel() < minAvailabilityLevel)
                                .orElse(new AvailabilityInterval()),
                        Optional.ofNullable(currentInterval.get())
                                .filter(i -> i.getAvailabilityLevel() < minAvailabilityLevel)
                                .orElse(new AvailabilityInterval()))))
                .sorted(Comparator.comparing(AvailabilityInterval::getStartDate));
    }

    public String toMessage(AvailabilityInterval availabilityInterval) {
        return String.format(Locale.US, "%s\t%s\t%.1f",
                availabilityInterval.getStartDate().toLocalTime(),
                availabilityInterval.getEndDate().toLocalTime(),
                availabilityInterval.getAvailabilityLevel());
    }

    private AvailabilityInterval add(AvailabilityInterval availabilityInterval, RequestResult requestResult) {
        availabilityInterval = Optional.ofNullable(availabilityInterval)
                .orElse(new AvailabilityInterval());
        availabilityInterval.setStartDate(Stream.of(availabilityInterval.getStartDate(), requestResult.getDateTime())
                .filter(Objects::nonNull)
                .min(OffsetDateTime::compareTo)
                .orElse(null));

        availabilityInterval.setEndDate(Stream.of(availabilityInterval.getEndDate(), requestResult.getDateTime())
                .filter(Objects::nonNull)
                .max(OffsetDateTime::compareTo)
                .orElse(null));

        availabilityInterval.setTotalCount(Optional.ofNullable(availabilityInterval.getTotalCount())
                .orElse(0) + 1);

        availabilityInterval.setFailureCount(Optional.ofNullable(availabilityInterval.getFailureCount())
                .orElse(0) + (isCorrect(requestResult) ? 0 : 1));

        return availabilityInterval;
    }

    private AvailabilityInterval add(AvailabilityInterval availabilityInterval1, AvailabilityInterval availabilityInterval2) {
        availabilityInterval1 = Optional.ofNullable(availabilityInterval1)
                .orElse(new AvailabilityInterval());
        availabilityInterval1.setStartDate(Stream.of(availabilityInterval1.getStartDate(), availabilityInterval2.getStartDate())
                .filter(Objects::nonNull)
                .min(OffsetDateTime::compareTo)
                .orElse(null));

        availabilityInterval1.setEndDate(Stream.of(availabilityInterval1.getEndDate(), availabilityInterval2.getEndDate())
                .filter(Objects::nonNull)
                .max(OffsetDateTime::compareTo)
                .orElse(null));

        availabilityInterval1.setTotalCount(Optional.ofNullable(availabilityInterval1.getTotalCount())
                .orElse(0) +
                Optional.ofNullable(availabilityInterval2.getTotalCount())
                        .orElse(0));

        availabilityInterval1.setFailureCount(Optional.ofNullable(availabilityInterval1.getFailureCount())
                .orElse(0) +
                Optional.ofNullable(availabilityInterval2.getFailureCount())
                        .orElse(0));

        return availabilityInterval1;
    }
}
