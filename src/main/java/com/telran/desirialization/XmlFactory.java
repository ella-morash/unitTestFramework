package com.telran.desirialization;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.telran.annotations.JsonResource;
import com.telran.annotations.XmlResource;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class XmlFactory {

    private XmlMapper xmlMapper = new XmlMapper();


    @SneakyThrows
    public void readFromFile(Object object) {


        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(JsonResource.class))
                .forEach(f -> {
                    Object newObj = null;
                    try {
                        newObj = f.getType().getConstructor(new Class[]{}).newInstance();

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    try {

                        var obj = xmlMapper.readValue(new File(f.getAnnotation(JsonResource.class).value()),newObj.getClass());

                        f.setAccessible(true);
                        try {
                            f.set(object, obj);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    } catch (IOException e) {

                        if (f.getAnnotation(JsonResource.class).nullIfError()) {
                            f.setAccessible(true);
                            try {
                                f.set(object, null);
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
