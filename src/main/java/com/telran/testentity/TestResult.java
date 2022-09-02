package com.telran.testentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestResult {

    Method method;
    TestResultStatus testResultStatus;
    Throwable stackTrace;
}
