package by.grechanikovars.autoservice.entity;

public record RepairResult(
        int carId,
        int bayId,
        int partsUsed,
        long repairDurationMs,
        boolean success
) {
    @Override
    public String toString() {
        return String.format(
                "RepairResult{carId=%d, bayId=%d, partsUsed=%d, durationMs=%d, success=%s}",
                carId, bayId, partsUsed, repairDurationMs, success
        );
    }
}
