package by.grechanikovars.autoservice.factory;

import by.grechanikovars.autoservice.entity.RepairResult;
import by.grechanikovars.autoservice.util.IdGenerator;

public class RepairResultFactory {

  private final IdGenerator idGenerator;

  public RepairResultFactory(IdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  public RepairResult create(int carId, int bayId, int partsUsed,
                             long repairDurationMs, boolean success) {
    int reportId = idGenerator.nextId();
    return new RepairResult(reportId, carId, bayId, partsUsed, repairDurationMs, success);
  }
}