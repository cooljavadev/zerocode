package org.jsmart.zerocode.parallel;


import org.jboss.resteasy.spi.InternalServerErrorException;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.jsmart.zerocode.parallel.PropertiesProvider.getProperty;

public class ExecutorServiceRunner {

    private int numberOfThreads = parseInt(getProperty("number.of.threads"));

    public ExecutorServiceRunner(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void run(List<Callable<Object>> callables) {

        // Keep pool size very minimum depending upon the machine -
        ExecutorService executorService = newFixedThreadPool(numberOfThreads);

        try {
            executorService.invokeAll(callables).stream().forEach(future -> execute(future));
        } catch (InterruptedException interruptEx) {
            throw new RuntimeException(interruptEx);
        } finally {
            executorService.shutdown();
        }
    }

    public <T extends Object> Callable<Object> createCallableFuture(T objectToConsumer, Consumer<T> consumer) {
        return () -> {
            consumer.accept(objectToConsumer);
            return true;
        };
    }

    private Object execute(Future<Object> future) {
        try {
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
}
