package org.jsmart.zerocode.parallel;

import java.time.LocalDateTime;

public class MockHttpExecutor {

    public void invoke(String message){
        System.out.println(Thread.currentThread().getName() + " " + message + " Mock Http call Start. -" + LocalDateTime.now());
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + " " + message + "Mock Http call *Finished. -" + LocalDateTime.now());

    }
}
