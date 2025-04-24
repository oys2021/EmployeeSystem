package com.example.employeesystem.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDatabaseTest {

    EmployeeDatabase<String> employeedb;
    Employee<String> employee;

    @BeforeEach
    public void setup() {
        employeedb = new EmployeeDatabase<>();
        employeedb.validDepartments = Set.of("HR", "Finance", "Engineering", "Marketing");
    }

    @Test
    void testAddEmployee() throws EmployeeDetailRequiredException, InvalidDepartmentException, InvalidSalaryException {
        employee = new Employee<>("E123", "Alice", "Engineering", 50000, 45.00, 15, true);

        String result = employeedb.addEmployee(employee);

        assertEquals("Employee Alice created successfully", result);
        assertNotNull(employeedb.employeeHashMap.get("E123"));
    }

    @Test
    public void testAddEmployeeWithNullName() {
        Employee<String> employee = new Employee<>("E124", null, "HR", 40000, 45.00, 15, true);

        Exception exception = assertThrows(EmployeeDetailRequiredException.class, () -> {
            employeedb.addEmployee(employee);
        });

        assertEquals("Employee name is required.", exception.getMessage());
    }


    @Test
    public void testAddEmployeeWithNegativeSalary() {
        Employee<String> employee = new Employee<>("E125", "Bob", "HR", -40000, 45.00, 15, true);

        Exception exception = assertThrows(InvalidSalaryException.class, () -> {
            employeedb.addEmployee(employee);
        });

        assertEquals("Salary cannot be negative.", exception.getMessage());
    }

    @Test
    public void testAddEmployeeWithInvalidDepartment() {
        Employee<String> employee = new Employee<>("E126", "Bob", "Science", 40000, 45.00, 15, true);

        Exception exception = assertThrows(InvalidDepartmentException.class, () -> {
            employeedb.addEmployee(employee);
        });

        assertTrue(exception.getMessage().startsWith("Department is not valid"));
    }

    @Test
    public void testAddEmployeeWithExistingId() throws EmployeeDetailRequiredException, InvalidDepartmentException, InvalidSalaryException {
        Employee<String> employee1 = new Employee<>("E127", "Alice", "Finance", 50000, 45.00, 15, true);
        Employee<String> employee2 = new Employee<>("E127", "Bob", "HR", 40000, 45.00, 15, true);

        employeedb.addEmployee(employee1);
        String result = employeedb.addEmployee(employee2);

        assertEquals("Employee with Id E127 already exists", result);
    }

    @Test
    void testRemoveExistingEmployee() throws Exception {
        Employee<String> employee = new Employee<>("E101", "Alice", "HR", 45000, 45.0, 10, true);
        employeedb.addEmployee(employee);

        String result = employeedb.removeEmployee("E101");

        assertEquals("Employee with ID E101 has been removed successfully", result);
        assertFalse(employeedb.employeeHashMap.containsKey("E101"));
    }

    @Test
    void testRemoveNonExistingEmployee() {
        Exception exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeedb.removeEmployee("E999");
        });

        assertEquals("Employee with ID E999 does not exist", exception.getMessage());
    }

    @Test
    void testFindEmployeesByDepartmentSuccess() throws Exception {
        Employee<String> emp1 = new Employee<>("E001", "John", "Engineering", 60000, 45.0, 10, true);
        Employee<String> emp2 = new Employee<>("E002", "Jane", "Engineering", 62000, 45.0, 12, true);
        Employee<String> emp3 = new Employee<>("E003", "Alex", "HR", 50000, 45.0, 8, true);
        employeedb.addEmployee(emp1);
        employeedb.addEmployee(emp2);
        employeedb.addEmployee(emp3);

        String result = employeedb.findEmployeesByDepartment("engineer");

        assertTrue(result.contains(emp1.toString()));
        assertTrue(result.contains(emp2.toString()));
        assertFalse(result.contains(emp3.toString()));
    }

    @Test
    void testFindEmployeesByDepartmentNotFound() {
        assertThrows(EmployeeSearchException.class, () -> {
            employeedb.findEmployeesByDepartment("Legal");
        });
    }
}
