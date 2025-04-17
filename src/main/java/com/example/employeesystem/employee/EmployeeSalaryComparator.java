package com.example.employeesystem.employee;

import java.util.Comparator;

public class EmployeeSalaryComparator<T>  implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> employee1, Employee<T> employee2){
        return  Double.compare(employee2.salary,employee1.salary);
    }
}
