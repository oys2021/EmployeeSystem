package com.example.employeesystem.employee;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {

    HashMap<T,Employee<T>> employeeHashMap;

    public EmployeeDatabase(){
        this.employeeHashMap = new HashMap<>();
    }

    public String addEmployee(Employee<T> employee ){
        if (employeeHashMap.containsKey(employee.getEmployeeId())){
            return "Employee with Id" + employee.getEmployeeId() + "already exist";
        }
        employeeHashMap.put(employee.getEmployeeId(),employee);
        return  "Employee" + employee.getName() + " created successfully";
    }

    public String removeEmployee(T employeeId) {
        if (!employeeHashMap.containsKey(employeeId)) {
            return "Employee with ID " + employeeId + " does not exist";
        }
        employeeHashMap.remove(employeeId);
        return "Employee with ID " + employeeId + " has been removed successfully";
    }


    public Collection<Employee<T>> getAllEmployees () {
       return  employeeHashMap.values();
    }

    public String updateEmployeeDetails(T employeeId, String field, Object newValue) {
        Employee<T> employee = employeeHashMap.get(employeeId);

        if (employee == null) {
            return "Employee with ID " + employeeId + " not found.";
        }

        switch (field.toLowerCase()) {
            case "name":
                if (newValue instanceof String) {
                    employee.name = (String) newValue;
                    return "Name updated.";
                }
                break;
            case "department":
                if (newValue instanceof String) {
                    employee.department = (String) newValue;
                    return "Department updated.";
                }
                break;
            case "salary":
                if (newValue instanceof Number) {
                    employee.salary = ((Number) newValue).doubleValue();
                    return "Salary updated.";
                }
                break;
            case "performancerating":
                if (newValue instanceof Number) {
                    employee.performanceRating = ((Number) newValue).doubleValue();
                    return "Performance rating updated.";
                }
                break;
            case "yearsofexperience":
                if (newValue instanceof Number) {
                    employee.yearsOfExperience = ((Number) newValue).intValue();
                    return "Years of experience updated.";
                }
                break;
            case "isactive":
                if (newValue instanceof Boolean) {
                    employee.isActive = (Boolean) newValue;
                    return "Employment status updated.";
                }
                break;
            default:
                return "Invalid field name: " + field;
        }

        return "Invalid value type for field: " + field;
    }


    public String sortByDepartment(String department){
        return employeeHashMap.values()
                .stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
                .map(Employee::toString)
                .collect(Collectors.joining());
    }

    public String findEmployeesByName(String searchTerm) {
        return employeeHashMap.values().stream()
                .filter(emp -> emp.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(Employee::toString)
                .collect(Collectors.joining(", "));
    }

    public String findEmployeesByRating(double minRating) {
        return employeeHashMap.values().stream()
                .filter(emp -> emp.getPerformanceRating() >= minRating)
                .map(Employee::toString)
                .collect(Collectors.joining(", "));
    }



    public String findEmployeesBySalaryRange(double minSalary, double maxSalary) {
        return employeeHashMap.values().stream()
                .filter(emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary)
                .map(Employee::toString)
                .collect(Collectors.joining(", "));
    }


    public void displayAllEmployeesUsingIterator() {
        Iterator<Employee<T>> iterator = employeeHashMap.values().iterator();
        while (iterator.hasNext()) {
            Employee emp = iterator.next();
            System.out.println(emp);
        }
    }



    public void giveRaiseToHighPerformers(double raisePercentage) {
        employeeHashMap.values().stream()
                .filter(e -> e.getPerformanceRating() >= 4.5)
                .forEach(e -> {
                    double newSalary = e.getSalary() * (1 + raisePercentage / 100);
                    e.setSalary(newSalary);
                });
    }

    public List<Employee<T>> getTop5HighestPaidEmployees() {
        return employeeHashMap.values().stream()
                .sorted((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()))
                .limit(5)
                .toList();
    }

    public double getAverageSalaryByDepartment(String department) {
        return employeeHashMap.values().stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    public void displayAllEmployees() {
        System.out.printf("%-10s %-20s %-12s %-10s %-10s %-8s %-10s%n",
                "ID", "Name", "Department", "Salary", "Rating", "Exp", "Active");
        System.out.println("--------------------------------------------------------------------------------");

        for (Employee<T> e : employeeHashMap.values()) {
            System.out.printf("%-10s %-20s %-12s %-10.2f %-10.1f %-8d %-10s%n",
                    e.getEmployeeId(),
                    e.getName(),
                    e.getDepartment(),
                    e.getSalary(),
                    e.getPerformanceRating(),
                    e.getYearsOfExperience(),
                    e.getisActive());
        }
    }


    public void displayFormattedReport() {
        String header = String.format("%-10s %-20s %-12s %-10s %-10s %-8s %-10s",
                "ID", "Name", "Department", "Salary", "Rating", "Exp", "Active");
        System.out.println(header);
        System.out.println("--------------------------------------------------------------------------------");

        employeeHashMap.values().stream()
                .sorted(Comparator.comparing(Employee::getName)) // Optional: sort alphabetically
                .forEach(e -> {
                    System.out.printf("%-10s %-20s %-12s %-10.2f %-10.1f %-8d %-10s%n",
                            e.getEmployeeId(),
                            e.getName(),
                            e.getDepartment(),
                            e.getSalary(),
                            e.getPerformanceRating(),
                            e.getYearsOfExperience(),
                            e.getisActive());
                });
    }




}








