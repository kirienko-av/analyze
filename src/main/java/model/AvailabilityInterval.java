package model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.OffsetDateTime;

@Data
public class AvailabilityInterval {
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Long totalCount;
    private Long failureCount;
    @Setter(AccessLevel.NONE)
    private Double availabilityLevel;

    public Double getAvailabilityLevel() {
        if (totalCount > 0)
            return 100D - ((failureCount * 100) / totalCount);
        return null;
    }
}
