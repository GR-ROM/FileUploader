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
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CustomFixedThreadPoolTest {

    private CustomFixedThreadPool threadPool;
    private List<Runnable> testTask;
    private final int max_task = 2048;
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
        threadPool = new CustomFixedThreadPool(threads);
        Long time = System.currentTimeMillis();
        testTask.forEach(t -> threadPool.submit(t));
        threadPool.shutdown();
        Assertions.assertTimeout(Duration.ofSeconds(max_timeout_sec), () -> threadPool.awaitTermination(120, TimeUnit.SECONDS));
        threadPool.shutdownNow();
        System.out.println("All done in " + (System.currentTimeMillis() - time) + " ms");
    }

    @ParameterizedTest
    @MethodSource("provideNumberOfThreads")
    public void shouldStartAndTerminateAllWorkers(int threads) {
        threadPool = new CustomFixedThreadPool(threads);
        testTask.forEach(t -> threadPool.enqueueTask(new TaskWrapper(t)));
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.MILLISECONDS);
        Assertions.assertTimeout(Duration.ofSeconds(2), () -> threadPool.shutdownNow());
    }

    @Test
    public void shouldStartAndTerminateAllWorkersNewThread() {
        List<Thread> threadList=new ArrayList<>();
        testTask.forEach(t -> {
            Thread pthread=new Thread(t);
            pthread.start();
            threadList.add(pthread);
        });
        threadList.forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void shouldFireIllegalArgumentExceptionInCaseOfZeroThreads() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> threadPool = new CustomFixedThreadPool(0));
    }

    private static List<Arguments> provideNumberOfThreads() {
        List<Arguments> args = new ArrayList<>();
        IntStream.iterate(1, i -> i * 2).limit(12).forEach(t -> args.add(Arguments.of(t)));
        return args;
    }
}
