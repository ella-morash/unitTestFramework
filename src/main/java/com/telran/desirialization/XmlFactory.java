package com.telran.desirialization;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.telran.annotations.JsonResource;
import com.telran.annotations.XmlResource;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class XmlFactory {

    private XmlMapper xmlMapper = new XmlMapper();


    @SneakyThrows
    public void readFromFile(String className) {

        var instance = Class.forName(className).getDeclaredConstructors()[0];

        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(XmlResource.class))
                .forEach(f -> {
                    try {
                        xmlMapper.readValue(new File(f.getAnnotation(XmlResource.class).value()), instance.getClass());
                    } catch (IOException e) {
                        if (f.getAnnotation(XmlResource.class).nullIfError()) {
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
