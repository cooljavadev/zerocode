package org.jsmart.zerocode.parallel;


import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.jsmart.zerocode.parallel.PropertiesProvider.getProperty;

public class ExecutorServiceRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZeroCodeUnitRunner.class);

    public static final int NO_OF_USERS_IN_RAMP_UP_PERIOD = 5;
    private int numberOfThreads = parseInt(getProperty("number.of.threads"));
    private int rampUpPeriod = parseInt(getProperty("ramp.up.period"));
    private int loopCount = parseInt(getProperty("loop.count"));

    private Long delayBetweenTwoThreadsInSecs;


    public ExecutorServiceRunner() {
        delayBetweenTwoThreadsInSecs = (rampUpPeriod / NO_OF_USERS_IN_RAMP_UP_PERIOD) * 1000L;
    }

    public ExecutorServiceRunner(int numberOfThreads, int loopCount, int rampUpPeriod) {
        this.numberOfThreads = numberOfThreads;
        this.loopCount = loopCount;
        this.rampUpPeriod = rampUpPeriod;
        delayBetweenTwoThreadsInSecs = (rampUpPeriod / NO_OF_USERS_IN_RAMP_UP_PERIOD) * 1000L;
    }

    public void runRunnables(List<Runnable> runnables) {
        ExecutorService executorService = newFixedThreadPool(numberOfThreads);

        try {
            for (int i = 0; i < loopCount; i++) {
                runnables.stream().forEach(thisFunction -> {
                    for (int j = 0; j < NO_OF_USERS_IN_RAMP_UP_PERIOD; j++) {
                        System.out.println("j -> " + j);
                        System.out.println(Thread.currentThread().getName() + " JUnit test- Start. Time = " + LocalDateTime.now());
                        try {
                            System.out.println("waiting in the transit for adjusting ramp up, wait time : " + delayBetweenTwoThreadsInSecs);
                            Thread.sleep(delayBetweenTwoThreadsInSecs);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        executorService.execute(thisFunction);
                        System.out.println(Thread.currentThread().getName() + " JUnit test- *Finished Time = " + LocalDateTime.now() );

                    }
                });
            }
        } catch (Exception interruptEx) {
            throw new RuntimeException(interruptEx);
        } finally {
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                //wait for all tasks to finish
                //System.out.println("Still waiting for all threads to complete execution...");
            }
            System.out.println("Finished all threads");
        }
    }

    public void runSimple(List<Callable<Object>> callables) {

        ExecutorService executorService = newFixedThreadPool(numberOfThreads);

        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + " Task- Start. Time = " + LocalDateTime.now());
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " Task- **Finished* Time = " + LocalDateTime.now());

        };

        try {
            for (int i = 0; i < loopCount; i++) {
                executorService.invokeAll(callables).stream().forEach(future -> {
                    for (int j = 0; j < NO_OF_USERS_IN_RAMP_UP_PERIOD; j++) {
                        System.out.println("j -> " + j);
                        System.out.println(Thread.currentThread().getName() + " JUnit test- Start. Time = " + LocalDateTime.now());
                        try {
                            Thread.sleep(delayBetweenTwoThreadsInSecs);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


//                        execute(future);

//                        executorService.execute(task);


                        System.out.println(Thread.currentThread().getName() + " JUnit test- *Finished Time = " + LocalDateTime.now() );

                    }
                });
            }
        } catch (InterruptedException interruptEx) {
            throw new RuntimeException(interruptEx);
        } finally {
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                //wait for all tasks to finish
                //System.out.println("Still waiting for all threads to complete execution...");
            }
            System.out.println("Finished all threads");
        }


    }

    public Runnable createRunnables(Runnable runnable) {
        return runnable;
    }

    public <T extends Object> Callable<Object> createCallableFuture(T objectToConsumer, Consumer<T> consumer) {
        return () -> {
            consumer.accept(objectToConsumer);
            return true;
        };
    }

    private Object execute(Future<Object> future) {
        try {
            System.out.println("executing..........");
            return future.get();
        } catch (Exception futureEx) {
            if (futureEx.getCause() instanceof InternalServerErrorException) {
                throw (InternalServerErrorException) futureEx.getCause();
            }
            throw new RuntimeException(futureEx);
        }
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public int getRampUpPeriod() {
        return rampUpPeriod;
    }


}
