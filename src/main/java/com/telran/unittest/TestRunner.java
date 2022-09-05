package com.telran.unittest;

import com.telran.annotations.*;

import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;


public class TestRunner {

    @SneakyThrows
    public void run(String testName,String testClassName) {


        Object instance = Class.forName(testClassName).getDeclaredConstructors()[0].newInstance();


        var method = instance.getClass().getDeclaredMethod(testName);
        displayName(method);
        Annotation annotation =method.getDeclaredAnnotations()[0];

        if(isValidMethod(annotation,method)) {

                method.invoke(instance);
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

                            if(method.isAnnotationPresent(Test.class) && isValidMethod(method.getAnnotation(Test.class), method)) {
                                displayName(method);
                                method.invoke(instance);
                            }
                            runAfterEach(instance);


                        } catch (IllegalAccessException | InvocationTargetException e) {
                           throw new RuntimeException();
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
                .filter(method -> method.isAnnotationPresent(AfterAll.class))
                .forEach(method -> {
                    try {
                        if(isValidMethod(method.getAnnotation(AfterAll.class), method)) {
                            method.invoke(instance);
                        }

                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException();
                    }
                });
    }

    private void runBeforeAll(Object instance) {

        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeAll.class))
                .forEach(method -> {
                    try {
                        if(isValidMethod(method.getAnnotation(BeforeAll.class), method)) {
                            method.invoke(instance);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException();
                    }
                });
    }


    private void runBeforeEach(Object instance) {

    Arrays.stream(instance.getClass().getDeclaredMethods())
               .filter(method -> method.isAnnotationPresent(BeforeEach.class))
               .forEach(method -> {
                   try {
                       if(isValidMethod(method.getAnnotation(BeforeEach.class), method)) {
                           method.invoke(instance);
                       }
                   } catch (IllegalAccessException | InvocationTargetException e) {
                       throw new RuntimeException();
                   }
               });

    }

    private void runAfterEach(Object instance) {

        Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AfterEach.class))
                .forEach(method -> {
                    try {
                        if(isValidMethod(method.getAnnotation(AfterEach.class), method)) {
                            method.invoke(instance);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException();
                    }
                });

    }


    private boolean isValidMethod(Annotation ann, Method method) {

         return  method.isAnnotationPresent(ann.annotationType())
                 && Modifier.isPublic(method.getModifiers())
                 && method.getReturnType().equals(void.class)
                 && method.getParameterCount() == 0
                 && !Modifier.isStatic(method.getModifiers());

    }


}
