package com.telran.unittest;

import com.telran.annotations.*;


@DisplayName("this is a toy test")
public class ToyTest {

    @BeforeEach
    @DisplayName("before each")
    public  void init () {
        System.out.println("i am before each");
    }
    @BeforeAll
    @DisplayName("before all")
    public  void initAll() {
        System.out.println("i am before all");
    }

    @AfterEach
    @DisplayName("after each")
    public  void conclude() {
        System.out.println(" i am after each");
    }

    @AfterAll
    @DisplayName("after all")
    public  void concludeAll() {
        System.out.println("i am after all");
    }

    @Test
    @DisplayName("test 1")
   public  void test1() {
        System.out.println("test 1");
    }

    @Test
    @DisplayName("test 2")
    public  void test2() {
        System.out.println("test 2");
    }
}
