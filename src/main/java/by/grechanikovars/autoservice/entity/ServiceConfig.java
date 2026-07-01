package by.grechanikovars.autoservice.entity;

import java.util.List;

public record ServiceConfig(
        int bayCount,
        int warehouseParts,
        List<Integer> carPartsNeeded
) {}
