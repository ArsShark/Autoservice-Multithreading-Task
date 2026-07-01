package by.grechanikovars.autoservice.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {

    @Test
    void nextId_WhenCalledSequentially_ShouldReturnIncreasingValues() {
        // given
        IdGenerator generator = new IdGenerator();
        // when
        int first = generator.nextId();
        int second = generator.nextId();
        int third = generator.nextId();
        // then
        assertEquals(1, first);
        assertEquals(2, second);
        assertEquals(3, third);
    }

    @Test
    void nextId_WhenCalledConcurrently_ShouldProduceUniqueIds() throws Exception {
        IdGenerator generator = new IdGenerator();
        int callCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(callCount);

        List<Callable<Integer>> tasks = new java.util.ArrayList<>();
        for (int i = 0; i < callCount; i++) {
            tasks.add(generator::nextId);
        }
        List<Future<Integer>> futures = executor.invokeAll(tasks);

        List<Integer> ids = futures.stream()
                .map(this::getResult)
                .toList();
        executor.shutdown();

        long distinctCount = ids.stream().distinct().count();
        assertEquals(callCount, distinctCount);
    }

    private Integer getResult(Future<Integer> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to retrieve id from future.", e);
        }
    }
}
