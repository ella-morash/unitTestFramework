package com.telran.desirialization;

public class Main {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        Employee employee = new Employee();
        FileFactory fileFactory = new FileFactory();
        String className = testClass.getClass().getCanonicalName();
        String name = employee.getClass().getCanonicalName();
        System.out.println(className);
        fileFactory.readFromFile(className);
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.readFromFile(name);
        System.out.println(testClass);

    }
}
