package model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import util.RequestCountSingleton;

import java.time.OffsetDateTime;

@Data
public class AvailabilityInterval {
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Double runtime;
    @Setter(AccessLevel.NONE)
    private Double availabilityLevel;

    public Double getAvailabilityLevel() {
        if (RequestCountSingleton.getInstance().getRuntime() > 0)
            return (RequestCountSingleton.getInstance().getRuntime() - runtime) / RequestCountSingleton.getInstance().getRuntime() * 100;
        return 0D;
    }
}
