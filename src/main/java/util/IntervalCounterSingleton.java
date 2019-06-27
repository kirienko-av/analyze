package util;

import lombok.Data;
import model.AvailabilityInterval;
import model.RequestResult;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
public class IntervalCounterSingleton {
    private static IntervalCounterSingleton instance;
    private AvailabilityInterval availabilityInterval;

    private IntervalCounterSingleton() {
    }

    public static IntervalCounterSingleton getInstance() {
        if (instance == null) {
            instance = new IntervalCounterSingleton();
        }
        return instance;
    }

    public void addRequestResult(RequestResult requestResult) {
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

        availabilityInterval.setRequestCount(Optional.ofNullable(availabilityInterval.getRequestCount())
                .orElse(0) + 1);

        availabilityInterval.setRuntime(Optional.ofNullable(availabilityInterval.getRuntime())
                .orElse(0D) + requestResult.getRuntime());
    }

    public AvailabilityInterval getAvailabilityIntervalAndClear() {
        AvailabilityInterval availabilityInterval = this.availabilityInterval;
        this.availabilityInterval = null;
        return availabilityInterval;
    }
}
