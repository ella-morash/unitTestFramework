package com.telran.desirialization;

import com.telran.annotations.FileResource;
import com.telran.annotations.JsonResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.nio.file.Paths;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestClass {



    @FileResource(value = "src/main/java/com/telran/files/data.txt",nullIfError = true)
    private String data;

    @JsonResource(value = "src/main/java/com/telran/files/employee.json",nullIfError = true)
    private Employee employee;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
class Employee {
    private String name;
    private double salary;
}
