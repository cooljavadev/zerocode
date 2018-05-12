package org.jsmart.zerocode.parallel;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SampleTest {

    @Test
    public void testFirstName() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertThat("Albert", is("AlbertX"));
    }
}
