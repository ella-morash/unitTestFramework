package com.telran.desirialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.annotations.FileResource;
import com.telran.annotations.JsonResource;
import com.telran.annotations.XmlResource;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class JsonFactory {
    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public void readFromFile(String className) {

        var instance = Class.forName(className).getDeclaredConstructors()[0];

        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(JsonResource.class))
                .forEach(f -> {
                    try {
                        objectMapper.readValue(new File(f.getAnnotation(JsonResource.class).value()),instance.getClass());
                    } catch (IOException e) {

                        if (f.getAnnotation(JsonResource.class).nullIfError()) {
                            f.setAccessible(true);
                            try {
                                f.set(instance, null);
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            e.printStackTrace();
                        }
                    }

                });
    }

}
