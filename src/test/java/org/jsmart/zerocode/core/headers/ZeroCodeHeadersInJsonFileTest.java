package org.jsmart.zerocode.core.headers;

import org.jsmart.zerocode.core.domain.HostProperties;
import org.jsmart.zerocode.core.domain.JsonTestCase;
import org.jsmart.zerocode.core.tests.customrunner.TestOnlyZeroCodeUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@HostProperties(host="http://localhost", port=9998, context = "")
@RunWith(TestOnlyZeroCodeUnitRunner.class)
public class ZeroCodeHeadersInJsonFileTest {
    
    /**
     * Mock end points are in test/resources: simulators/test_purpose_end_points.json
     */

    @Test
    @JsonTestCase("13_headers/20_request_with_custom_headers.json")
    public void testRequestWithStringHeaders() throws Exception {
    
    }


    @Test
    @JsonTestCase("13_headers/30_request_with_headers_in_json_file.json")
    public void testRequestWithHeadersIn_jsonFile() throws Exception {

    }

}



