package com.unieus.garajea.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties loadFromClasspath(String filename) {
        try (InputStream is =
                PropertiesLoader.class.getClassLoader().getResourceAsStream(filename)) {

            if (is == null) {
                throw new RuntimeException("Ezin da aurkitu fitxategia: " + filename);
            }

            Properties p = new Properties();
            p.load(is);
            return p;

        } catch (IOException e) {
            throw new RuntimeException("Errorea properties fitxategia irakurtzean: " + filename, e);
        }
    }
}
