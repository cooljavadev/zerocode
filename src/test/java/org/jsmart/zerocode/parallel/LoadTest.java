package org.jsmart.zerocode.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static java.lang.Integer.parseInt;
import static org.jsmart.zerocode.parallel.PropertiesProvider.getProperty;

public class LoadTest {

    public static void main(String[] args) {
        ExecutorServiceRunner executorServiceRunner = new ExecutorServiceRunner(parseInt(getProperty("loop.count")));

        List<Callable<Object>> callables = new ArrayList<>();
        callables.add(executorServiceRunner.createCallableFuture("Helloooooo", (new MockHttpExecutor())::invoke));
        callables.add(executorServiceRunner.createCallableFuture("How r u", (new MockHttpExecutor())::invoke));
        callables.add(executorServiceRunner.createCallableFuture("today?", (new MockHttpExecutor())::invoke));


        final int noOfThreads = executorServiceRunner.getNumberOfThreads();
        for(int i = 0; i < noOfThreads; i++){
            executorServiceRunner.run(callables);

        }
    }
}
