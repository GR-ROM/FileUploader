package su.grinev.FileUploader.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CustomThreadPoolTest {

    private CustomThreadPool threadPool;
    private List<Runnable> testTask;
    private final int max_task = 1000;
    private final int max_timeout_sec = 10;

    @BeforeEach
    public void initialize() {
        testTask = new ArrayList<>();
        for (int i = 0; i != max_task; i++) {
            int finalI = i;
            Random rnd=new Random();
            testTask.add(() -> {
                int a = 10000;
                int b = 0;
                for (int t = 0; t != a; t++) {
                    t=rnd.nextInt(65535);
                    b=+t*t;
                }
                b= (int) Math.sqrt(b/a);
                String.valueOf(b);
            });
        }
    }

    @DisplayName("Should start and complete 10 tasks with different number of threads")
    @ParameterizedTest
    @MethodSource("provideNumberOfThreads")
    public void shouldStartAndComplete10TasksWithDifferentNumberOfThreads(int threads) throws InterruptedException {
        threadPool = new CustomThreadPool(threads);
        Long time = System.currentTimeMillis();
        threadPool.startExecutor();
        testTask.forEach(t -> threadPool.enqueueTask(new TaskWrapper(t)));
        Assertions.assertTimeout(Duration.ofSeconds(max_timeout_sec), () -> threadPool.waitForComplete());
        System.out.println("All done in " + (System.currentTimeMillis() - time) + " ms");
    }

    @ParameterizedTest
    @MethodSource("provideNumberOfThreads")
    public void shouldStartAndTerminateAllWorkers(int threads) {
        threadPool = new CustomThreadPool(threads);
        testTask.forEach(t -> threadPool.enqueueTask(new TaskWrapper(t)));
        Assertions.assertTimeout(Duration.ofSeconds(2), () -> threadPool.terminateAll());
    }

    @Test
    public void shouldFireIllegalArgumentExceptionInCaseOfZeroThreads() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> threadPool = new CustomThreadPool(0));
    }

    private static List<Arguments> provideNumberOfThreads() {
        List<Arguments> args = new ArrayList<>();
        IntStream.iterate(1, i -> i * 2).limit(6).forEach(t -> args.add(Arguments.of(t)));
        return args;
    }
}
