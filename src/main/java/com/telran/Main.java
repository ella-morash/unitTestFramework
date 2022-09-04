package com.telran;

import java.lang.reflect.InvocationTargetException;

public class Main {
    private final static String className = ToyTest.class.getName();

    public static void main(String[] args)  {

        TestRunner testRunner = new TestRunner();

        testRunner.run(className);

        testRunner.run("test2",className);


    }
}
