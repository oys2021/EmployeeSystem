package com.example.employeesystem.employee;

public class NoEmployeesFoundException extends Exception {
    public NoEmployeesFoundException(String message) {
        super(message);
    }
}
