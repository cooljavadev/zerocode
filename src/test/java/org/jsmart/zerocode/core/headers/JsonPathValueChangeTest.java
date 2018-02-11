package org.jsmart.zerocode.core.headers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

import java.io.IOException;

import static org.jsmart.zerocode.core.utils.SmartUtils.readJsonAsString;

public class JsonPathValueChangeTest {

    private static final Configuration configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();

    @Test
    public void a_value_can_be_updated() throws IOException {

        String originalJson = "{\n"
                + "\"session\":\n"
                + "    {\n"
                + "        \"name\":\"JSESSIONID\",\n"
                + "        \"value\":\"5864FD56A1F84D5B0233E641B5D63B52\"\n"
                + "    },\n"
                + "\"loginInfo\":\n"
                + "    {\n"
                + "        \"loginCount\":77,\n"
                + "        \"previousLoginTime\":\"2014-12-02T11:11:58.561+0530\"\n"
                + "    }\n"
                + "}";

        ///
        final String newValue = "{\"name\": \"arby\"}";

        String json = "{\"id\" : \"1\"}";
        ObjectMapper mapper = new ObjectMapper();
//        JsonFactory factory = mapper.getFactory();
//        JsonParser jsonParser = factory.createParser(json);
//        JsonNode node = mapper.readTree(jsonParser);

        JsonNode node = mapper.readTree(json);

        ///
        JsonNode updatedJson = JsonPath.using(configuration).parse(originalJson).set("$.session.name", node).json();

        System.out.println(updatedJson.toString());
    }

    @Test
    public void testJsonString() throws Exception {
        String jsonString = readJsonAsString("13_headers/common_headers.json");
        System.out.println("### original: " + jsonString);

        final String escapedJson = StringEscapeUtils.escapeJavaScript(jsonString);
        System.out.println("### escapedJson: " + escapedJson);


        ObjectMapper mapper = new ObjectMapper();

        final String contentInsideDoubleQuotes = "\"" + escapedJson + "\"";

        JsonNode nodeString = mapper.readTree(contentInsideDoubleQuotes);
        //JsonNode node = mapper.readTree(jsonString);

        System.out.println("### nodeString: " + nodeString.toString());

        //final String unEscaped = nodeString.toString().replace("\\\"", "\"");
        final String unEscaped = StringEscapeUtils.unescapeJavaScript(nodeString.toString());
        System.out.println("###unEscaped string : " + unEscaped);

        final String staringJsonWoDoubleQt = unEscaped.substring(1, unEscaped.length() - 1);

        System.out.println("###staringJsonWoDoubleQt json : " + staringJsonWoDoubleQt);

        String unescapedJson = StringEscapeUtils.unescapeJavaScript(staringJsonWoDoubleQt);
        System.out.println("###unescapedJson  : " + unescapedJson);


        JsonNode nodeAgain = mapper.readTree(unescapedJson);
        System.out.println("### nodeAgain: " + staringJsonWoDoubleQt.toString());

    }

    @Test
    public void testStringPassByReference() throws Exception {

        String original = "original";
        String changed = changeString(original);


    }

    private String changeString(String original) {
        original = "did not change";

        return "Changed";
    }
}
