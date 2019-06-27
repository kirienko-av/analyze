package model;

import lombok.Data;
import util.RequestCountSingleton;

import java.time.OffsetDateTime;

@Data
public class AvailabilityInterval {
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Double runtime;
    private Double availabilityLevel;
    private Integer requestCount;

    public Double getAvailabilityLevel() {
        if (RequestCountSingleton.getInstance().getRuntime() > 0)
            return (RequestCountSingleton.getInstance().getRuntime() - runtime) / RequestCountSingleton.getInstance().getRuntime() * 100;
        return 0D;
    }
}
