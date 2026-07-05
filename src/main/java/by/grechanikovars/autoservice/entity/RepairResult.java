package by.grechanikovars.autoservice.entity;

public record RepairResult(
        int reportId,
        int carId,
        int bayId,
        int partsUsed,
        long repairDurationMs,
        boolean success
) {

    @Override
    public String toString() {
        return String.format(
                "RepairResult{reportId=%d, carId=%d, bayId=%d, partsUsed=%d, durationMs=%d, success=%s}",
                reportId, carId, bayId, partsUsed, repairDurationMs, success
        );
    }
}
