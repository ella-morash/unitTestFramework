package com.telran;

import com.telran.annotations.*;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;


public class TestRunner {

    @SneakyThrows
    public void run(Method test,String testClassName) {

        var instance = Class.forName(testClassName).getDeclaredConstructors()[0].newInstance();

        if(isValidMethod(test)) {
            test.invoke(instance);

        }



    }

    @SneakyThrows
    public void run(String testClassName) {

        var instance = Class.forName(testClassName).getDeclaredConstructors()[0].newInstance();

        displayName(instance);

        runBeforeAll(instance);

        Arrays.stream(Class.forName(testClassName)
                    .getDeclaredMethods())
                    .forEach(method -> {
                        try {
                            runBeforeEach(instance);

                            if(isValidMethod(method)) {
                                displayName(method);
                                method.invoke(instance);
                            }
                            runAfterEach(instance);

                            //return test result hier
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });

        runAfterAll(instance);



    }

    private void displayName(Method test) {

       var info = test.getAnnotation(DisplayName.class).value();
        System.out.println(info);

    }
    private void displayName(Object instance) {

        var info = instance.getClass().getAnnotation(DisplayName.class).value();
        System.out.println(info);

    }

    private void runAfterAll(Object instance) {

        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> Arrays.stream(method
                                .getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(AfterAll.class)))
                .forEach(method -> {
                    try {
                        if(isValidMethod(method)) {
                            method.invoke(instance);
                        }

                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void runBeforeAll(Object instance) {

        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> Arrays.stream(method
                                .getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(BeforeAll.class)))
                .forEach(method -> {
                    try {
                        if(isValidMethod(method)) {
                            method.invoke(instance);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }


    private void runBeforeEach(Object instance) {

    Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> Arrays.stream(method
                                      .getDeclaredAnnotations())
                                      .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(BeforeEach.class)))
               .forEach(method -> {
                   try {
                       if(isValidMethod(method)) {
                           method.invoke(instance);
                       }
                   } catch (IllegalAccessException | InvocationTargetException e) {
                       e.printStackTrace();
                   }
               });

    }

    private void runAfterEach(Object instance) {

        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> Arrays.stream(method
                                .getDeclaredAnnotations())
                        .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(AfterEach.class)))
                .forEach(method -> {
                    try {
                        if(isValidMethod(method)) {
                            method.invoke(instance);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });

    }


    private boolean isValidMethod(Method method) {

         return Arrays
                 .stream(method.getDeclaredAnnotations())
                 .anyMatch(annotation->annotation.annotationType().isAnnotationPresent(Test.class))
                && Modifier.isPublic(method.getModifiers())
                && method.getReturnType() == void.class
                && method.getParameterCount() == 0
                && Modifier.isStatic(method.getModifiers());

    }


}
