package com.telran;

import com.telran.annotations.Test;
import com.telran.testentity.TestResult;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {

    @SneakyThrows
    public TestResult run(Method test,Object instance) {

        if(verifyMethod(test)) {
            test.invoke(instance);

        }

        return TestResult.builder()
                .stackTrace(new Exception())
                .build();


    }

    @SneakyThrows
    public List<TestResult> run(String testClassName) {

        var instance = Class.forName(testClassName).getDeclaredConstructors()[0].newInstance();

        var satisfied =  Arrays.stream(Class.forName(testClassName)
                .getDeclaredMethods())
                .allMatch(this::verifyMethod);

        if (satisfied) {
            Arrays.stream(Class.forName(testClassName)
                    .getDeclaredMethods())
                    .forEach(method -> {
                        try {
                            method.invoke(instance);

                            //return test result hier

                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        }

        return new ArrayList<>();

    }


    private boolean verifyMethod(Method method) {

         return Arrays
                 .stream(method.getDeclaredAnnotations())
                 .anyMatch(annotation->annotation.annotationType().isAnnotationPresent(Test.class))
                && Modifier.isPublic(method.getModifiers())
                && method.getReturnType() == void.class
                && method.getParameterCount() == 0
                && Modifier.isStatic(method.getModifiers());

    }


}
