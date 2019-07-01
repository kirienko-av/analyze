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

            Optional<AvailabilityInterval> current = Optional.ofNullable(currentInterval.get());
            Optional<AvailabilityInterval> previous = Optional.ofNullable(previousInterval.get());

            if (current.isPresent()) {
                if (current
                        .filter(i -> i.getEndDate().equals(requestResult.getDateTime()))
                        .isPresent()) {
                    currentInterval.set(add(currentInterval.get(), requestResult));
                } else if (current
                        .filter(i -> i.getAvailabilityLevel() < minAvailabilityLevel)
                        .isPresent()) {
                    previousInterval.set(previous
                            .map(i -> add(currentInterval.getAndSet(add(new AvailabilityInterval(), requestResult)), i))
                            .orElse(currentInterval.getAndSet(add(new AvailabilityInterval(), requestResult))));
                } else {
                    previous
                            .ifPresent(i -> {
                                availabilityIntervals.add(previousInterval.getAndSet(null));
                            });
                    currentInterval.set(add(new AvailabilityInterval(), requestResult));
                }
            } else {
                currentInterval.set(add(new AvailabilityInterval(), requestResult));
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
                .filter(t -> t.getStartDate() != null);
    }

    public String toMessage(AvailabilityInterval availabilityInterval) {
        return String.format(Locale.US, "%s\t%s\t%.1f",
                availabilityInterval.getStartDate().toLocalTime(),
                availabilityInterval.getEndDate().toLocalTime(),
                availabilityInterval.getAvailabilityLevel());
    }

    private AvailabilityInterval add(AvailabilityInterval availabilityInterval, RequestResult requestResult) {
        return add(availabilityInterval,
                requestResult.getDateTime(),
                requestResult.getDateTime(),
                1L,
                (isCorrect(requestResult) ? 0L : 1L));
    }

    private AvailabilityInterval add(AvailabilityInterval availabilityInterval1, AvailabilityInterval availabilityInterval2) {
        return add(availabilityInterval1,
                availabilityInterval2.getStartDate(),
                availabilityInterval2.getEndDate(),
                availabilityInterval2.getTotalCount(),
                availabilityInterval2.getFailureCount());
    }

    private AvailabilityInterval add(AvailabilityInterval availabilityInterval1, OffsetDateTime startDate, OffsetDateTime endDate, Long totalCount, Long failureCount) {
        availabilityInterval1 = Optional.ofNullable(availabilityInterval1)
                .orElse(new AvailabilityInterval());
        availabilityInterval1.setStartDate(Stream.of(availabilityInterval1.getStartDate(), startDate)
                .filter(Objects::nonNull)
                .min(OffsetDateTime::compareTo)
                .orElse(null));

        availabilityInterval1.setEndDate(Stream.of(availabilityInterval1.getEndDate(), endDate)
                .filter(Objects::nonNull)
                .max(OffsetDateTime::compareTo)
                .orElse(null));

        availabilityInterval1.setTotalCount(Optional.ofNullable(availabilityInterval1.getTotalCount())
                .orElse(0L) +
                Optional.ofNullable(totalCount)
                        .orElse(0L));

        availabilityInterval1.setFailureCount(Optional.ofNullable(availabilityInterval1.getFailureCount())
                .orElse(0L) +
                Optional.ofNullable(failureCount)
                        .orElse(0L));

        return availabilityInterval1;
    }
}
