package com.telran.desirialization;

import com.telran.annotations.FileResource;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileFactory {

    @SneakyThrows
    public void readFromFile(Object object) {



        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(FileResource.class))
                .forEach(f -> {
                    try {
                        var str = FileUtils.readFileToString(new File(f.getAnnotation(FileResource.class).value()), StandardCharsets.UTF_8);
                        f.setAccessible(true);
                        try {
                            f.set(object, str);
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    } catch (IOException e) {

                        if (f.getAnnotation(FileResource.class).nullIfError()) {
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

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
      //Arrays.stream(TestClass.class.getDeclaredFields()).forEach(f-> System.out.println(f.getName()));


        var instance = TestClass.class.getDeclaredConstructors()[0].newInstance();

        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(FileResource.class))

                .forEach(f -> {
                    System.out.println(f.getName());
                    try {
                        var str =FileUtils.readFileToString(new File(f.getAnnotation(FileResource.class).value()), StandardCharsets.UTF_8);
                        System.out.println(str);
                        f.setAccessible(true);
                        try {
                            f.set(instance, str);
                            System.out.println(str);

                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
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

