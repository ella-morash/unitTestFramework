package com.telran.desirialization;

public class Main {

    public static void main(String[] args) {

        Employee employee = new Employee("",0.0);
        TestClass testClass = new TestClass("",employee);
        FileFactory fileFactory = new FileFactory();
        String className = testClass.getClass().getCanonicalName();
        String name = employee.getClass().getCanonicalName();

        fileFactory.readFromFile(testClass);
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.readFromFile(testClass);
        System.out.println(testClass);

    }
}
