package com.example.employeesystem.employee;

import com.example.employeesystem.EmployeeFormController;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(EmployeeFormController.class.getName());

    public static void main(String[] args) {
        EmployeeDatabase<UUID> db = new EmployeeDatabase<>();

        // Sample employees
        Employee<UUID> emp1 = new Employee<>(UUID.randomUUID(), "Alice Smith", "HR", 60000, 4.8, 6, true);
        Employee<UUID> emp2 = new Employee<>(UUID.randomUUID(), "Bob Johnson", "IT", 75000, 4.2, 4, true);
        Employee<UUID> emp3 = new Employee<>(UUID.randomUUID(), "Charlie Brown", "Finance", 50000, 4.6, 8, false);
        Employee<UUID> emp4 = new Employee<>(UUID.randomUUID(), "Daisy Adams", "IT", 82000, 4.9, 10, true);
        Employee<UUID> emp5 = new Employee<>(UUID.randomUUID(), "Eve Davis", "HR", 54000, 3.9, 2, true);

        try {
            db.addEmployee(emp1);
            db.addEmployee(emp2);
            db.addEmployee(emp3);
            db.addEmployee(emp4);
            db.addEmployee(emp5);

            System.out.println("\n--- All Employees ---");
            db.displayAllEmployees();

            db.giveRaiseToHighPerformers(10);
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

        } catch (EmployeeDetailRequiredException | InvalidSalaryException | InvalidDepartmentException e) {
            logger.log(Level.WARNING, "Error adding employee: " + e.getMessage(), e);
            System.out.println("Add Error: " + e.getMessage());
        } catch (EmployeeSearchException e) {
            logger.log(Level.WARNING, "Search error: " + e.getMessage(), e);
            System.out.println("Search Error: " + e.getMessage());
        } catch (EmployeeNotFoundException e) {
            logger.log(Level.WARNING, "Update or removal failed: " + e.getMessage(), e);
            System.out.println("Employee Not Found: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error", e);
            System.out.println("Unexpected Error: " + e.getMessage());
        }
    }
}
