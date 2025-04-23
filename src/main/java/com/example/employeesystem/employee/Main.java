package com.example.employeesystem.employee;

import com.example.employeesystem.EmployeeFormController;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(EmployeeFormController.class.getName());

    public static void main(String[] args) throws EmployeeNotFoundException {
        EmployeeDatabase<UUID> db = new EmployeeDatabase<>();

        Employee<UUID> emp1 = new Employee<>(UUID.randomUUID(), "Alice Smith", "HR", 60000, 4.8, 6, true);
        Employee<UUID> emp2 = new Employee<>(UUID.randomUUID(), "Bob Johnson", "IT", 75000, 4.2, 4, true);
        Employee<UUID> emp3 = new Employee<>(UUID.randomUUID(), "Charlie Brown", "Finance", 50000, 4.6, 8, false);
        Employee<UUID> emp4 = new Employee<>(UUID.randomUUID(), "Daisy Adams", "IT", 82000, 4.9, 10, true);
        Employee<UUID> emp5 = new Employee<>(UUID.randomUUID(), "Eve Davis", "HR", 54000, 3.9, 2, true);

        try{
            db.addEmployee(emp1);
            db.addEmployee(emp2);
            db.addEmployee(emp3);
            db.addEmployee(emp4);
            db.addEmployee(emp5);
        }
        catch (EmployeeDetailRequiredException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            System.out.println(e.getMessage());
        }
        catch (InvalidSalaryException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            System.out.println(ex.getMessage());
        }
        catch (InvalidDepartmentException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            System.out.println(ex.getMessage());
        }


        System.out.println("\n--- All Employees ---");
        db.displayAllEmployees();

        db.giveRaiseToHighPerformers(10); // 10% raise
        System.out.println("\n--- After Raise for High Performers ---");
        db.displayAllEmployees();

        System.out.println("\n--- Top 5 Highest Paid Employees ---");
        db.getTop5HighestPaidEmployees().forEach(System.out::println);

        System.out.println("\n--- Average Salary in IT ---");
        System.out.printf("%.2f%n", db.getAverageSalaryByDepartment("IT"));

        System.out.println("\n--- Search by Name: 'ali' ---");
        System.out.println(db.findEmployeesByName("ali"));

        System.out.println("\n--- Employees with Rating >= 4.0 ---");
        System.out.println(db.findEmployeesByRating(4.0));

        System.out.println("\n--- Employees with Salary between 50000 and 70000 ---");
        System.out.println(db.findEmployeesBySalaryRange(50000, 70000));

        System.out.println("\n--- Display Using Iterator ---");
        db.displayAllEmployeesUsingIterator();

        System.out.println("\n--- Update Employee Detail ---");
        System.out.println(db.updateEmployeeDetails(emp1.getEmployeeId(), "salary", 65000.0));
        System.out.println(db.updateEmployeeDetails(emp1.getEmployeeId(), "name", "Alicia Smith"));
        db.displayAllEmployees();

        System.out.println("\n--- Formatted Report ---");
        db.displayFormattedReport();
    }
}
