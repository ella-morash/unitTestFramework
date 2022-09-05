package com.telran.desirialization;

import com.telran.annotations.FileResource;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileFactory {

    @SneakyThrows
    public void readFromFile(String className) {

        var instance = Class.forName(className).getDeclaredConstructors()[0];

        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(f->f.isAnnotationPresent(FileResource.class))
                .forEach(f-> {
                    try {
                        FileUtils.readFileToString(new File(f.getAnnotation(FileResource.class).value()),f.getName());
                    } catch (IOException e) {

                        if (f.getAnnotation(FileResource.class).nullIfError()) {
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
