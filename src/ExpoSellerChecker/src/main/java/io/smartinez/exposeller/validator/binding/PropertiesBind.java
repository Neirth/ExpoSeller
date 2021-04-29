package io.smartinez.exposeller.validator.binding;

import jakarta.enterprise.inject.Produces;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("unused")
public class PropertiesBind {
    @Produces
    public Properties produceProperties() {
        Properties properties = new Properties();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
