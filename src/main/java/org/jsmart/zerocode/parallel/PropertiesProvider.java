package org.jsmart.zerocode.parallel;

import java.io.InputStream;
import java.util.Properties;

import static org.jsmart.zerocode.parallel.ZeroCodeException.uncheck;


public class PropertiesProvider {

    private static Properties properties = new Properties();

    static {
        InputStream inputStream = PropertiesProvider.class
                        .getClassLoader()
                        .getResourceAsStream("load_config_test.properties");

        uncheck(() -> {
            properties.load(inputStream);
            return null;
        });
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static Properties getProperties() {
        return properties;
    }
}
