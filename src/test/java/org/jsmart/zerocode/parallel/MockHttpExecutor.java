package org.jsmart.zerocode.parallel;

import java.time.LocalDateTime;

public class MockHttpExecutor {

    public void invoke(String message){
        System.out.println(LocalDateTime.now() + " - message = " + message);
    }
}
