package org.jsmart.zerocode.parallel;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import sun.jvm.hotspot.utilities.WorkerThread;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.parseInt;
import static org.jsmart.zerocode.parallel.PropertiesProvider.getProperty;

public class LoadTest {

    public static void mainX(String[] args) {
        ExecutorServiceRunner executorServiceRunner = new ExecutorServiceRunner(3, 1, 9);

        List<Callable<Object>> callables = new ArrayList<>();
        callables.add(executorServiceRunner.createCallableFuture("Task1", (new MockHttpExecutor())::invoke));
        //callables.add(executorServiceRunner.createCallableFuture("Task2", (new MockHttpExecutor())::invoke));

        Runnable task_x = () -> (new MockHttpExecutor()).invoke("task X");

        executorServiceRunner.runSimple(callables);
    }

    public static void main(String[] args) {
        ExecutorServiceRunner executorServiceRunner = new ExecutorServiceRunner(5, 1, 10);

        Runnable task_x = () -> (new MockHttpExecutor()).invoke("task X");

        Runnable task_y = () -> {
            System.out.println(Thread.currentThread().getName() + " Task- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " Task- **Finished* Time = " + LocalDateTime.now());

        };

        Runnable taskGitHubApiTest = () -> {
            System.out.println(Thread.currentThread().getName() + " JUnit test- Start. Time = " + LocalDateTime.now());

            Result result = (new JUnitCore()).run(Request.method(TestGitHubApi.class, "testGitHubApi_get"));

            System.out.println(Thread.currentThread().getName() + " JUnit test- *Finished Time, result = " + LocalDateTime.now() + " -" + result.wasSuccessful());

        };

        List<Runnable> runnables = new ArrayList<>();
        runnables.add(executorServiceRunner.createRunnables(taskGitHubApiTest));
        //runnables.add(executorServiceRunner.createRunnables(task_x));
        //runnables.add(executorServiceRunner.createRunnables(task_y));

        executorServiceRunner.runRunnables(runnables);
    }

    public static void main2(String[] args) {
        ScheduledExecutorService execService
                = Executors.newScheduledThreadPool(5);
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " 1st- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 1st- Finished* Time = " + LocalDateTime.now());

        };
        //execService.scheduleAtFixedRate(task, 0, 1000L, TimeUnit.MILLISECONDS);
        execService.scheduleWithFixedDelay(task, 0, 1000L, TimeUnit.MILLISECONDS);

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        execService.shutdown();
    }

    public static void main3(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        ///
        final AtomicInteger count = new AtomicInteger();
        Runnable task = () -> {
            System.out.println(count.getAndIncrement() + ", " + Thread.currentThread().getName() + " 1st- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(count.getAndIncrement() + ", " + Thread.currentThread().getName() + " 1st- Finished* Time = " + LocalDateTime.now());

        };

        Runnable task2 = () -> {
            System.out.println(count.getAndIncrement() + ", " + Thread.currentThread().getName() + " 2nd- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(count.getAndIncrement() + ", " + Thread.currentThread().getName() + " 2nd- Finished* Time = " + LocalDateTime.now());

        };

        Runnable counterCheck = () -> {
            System.out.println("checking counter... now- " + count.get());
            if (count.get() >= 20) {
                System.out.println("reached Max, can exit now, " + count.get());
                scheduledThreadPool.shutdown();
            }
        };

//        scheduledThreadPool.scheduleWithFixedDelay(() -> (new MockHttpExecutor()).invoke("task 3"), 0, 1, TimeUnit.SECONDS);
        //scheduledThreadPool.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);
        //scheduledThreadPool.scheduleWithFixedDelay(task2, 0, 3, TimeUnit.SECONDS);
        //scheduledThreadPool.scheduleWithFixedDelay(task2, 0, 4, TimeUnit.SECONDS);
//        scheduledThreadPool.scheduleWithFixedDelay(counterCheck, 5, 2, TimeUnit.SECONDS);

        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            scheduledThreadPool.scheduleWithFixedDelay(() -> (new MockHttpExecutor()).invoke("task 3"), 0, 1, TimeUnit.SECONDS);
            //scheduledThreadPool.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);
        }

        ///

        ///
        //schedule to run after sometime
//        System.out.println("Current Time = " + LocalDateTime.now());
//        final AtomicInteger count = new AtomicInteger();
//
//        for (int i = 0; i < 3; i++) {
//            Thread.sleep(1000);
//            scheduledThreadPool.scheduleAtFixedRate(() -> {
//                        System.out.println(count.getAndIncrement() + ", " + Thread.currentThread().getName() + " Start. Time = " + LocalDateTime.now());
//                        if (count.get() == 7) {
//                            scheduledThreadPool.shutdown();
//                        }
//                    },
//                    i + 1,
//                    i + 1,
//                    TimeUnit.SECONDS);
//
//        }

        ///

        //add some delay to let some threads spawn by scheduler
        Thread.sleep(20000);

        scheduledThreadPool.shutdown();
        while (!scheduledThreadPool.isTerminated()) {
            //wait for all tasks to finish
            //System.out.println("Still waiting for all threads to complete execution...");
        }
        System.out.println("Finished all threads");
    }

    // Works
    public static void main_works(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        Runnable task1 = () -> {
            System.out.println(Thread.currentThread().getName() + " 1st- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 1st- Finished* Time = " + LocalDateTime.now());

        };

        Runnable task2 = () -> {
            System.out.println(Thread.currentThread().getName() + " 2nd- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " 2nd- Finished* Time = " + LocalDateTime.now());

        };

        Runnable taskUnitTest = () -> {
            System.out.println(Thread.currentThread().getName() + " JUnit test- Start. Time = " + LocalDateTime.now());

            Result result = (new JUnitCore()).run(Request.method(SampleTest.class, "testFirstName"));

            System.out.println(Thread.currentThread().getName() + " JUnit test- *Finished Time = " + LocalDateTime.now() + " -" + result.wasSuccessful());

        };


        int k = 0;
        for(int j = 0; j < 2; j++){
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Loop count- " + k++ );
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                executor.execute(taskUnitTest);
                //executor.execute(task1);
                //executor.execute(task2);
                //executor.execute(() -> (new MockHttpExecutor()).invoke("task 3"));
            }
        }


        executor.shutdown();
        while (!executor.isTerminated()) {
            //wait for all tasks to finish
            //System.out.println("Still waiting for all threads to complete execution...");
        }
        System.out.println("Now Finished all threads");
    }

}

