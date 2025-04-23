package com.example.employeesystem.employee;

import java.util.*;
import java.util.stream.Collectors;



public class EmployeeDatabase<T> {

    HashMap<T,Employee<T>> employeeHashMap;

    Set<String> validDepartments = Set.of("HR", "Finance", "Engineering", "Marketing");

    public EmployeeDatabase(){
        this.employeeHashMap = new HashMap<>();
    }

    public String addEmployee(Employee<T> employee ) throws InvalidDepartmentException, InvalidSalaryException, EmployeeDetailRequiredException {

        if (employee.getName() == null || employee.getName().trim().isEmpty() ){
            throw new EmployeeDetailRequiredException("Employee name is required.");
        }

        if (employee.getEmployeeId() == null || employee.getName().trim().isEmpty()){
            throw new EmployeeDetailRequiredException("Employee ID is required.");
        }

        if (employee.getSalary() < 0){
            throw new InvalidSalaryException("Salary cannot be negative.");
        }

        if (!validDepartments.contains(employee.getDepartment())){
            throw new InvalidDepartmentException("Department is not valid or does not exist.These are the list of Departments you can consider ; \"HR\", \"Finance\", \"Engineering\", \"Marketing\"");
        }

        if (employeeHashMap.containsKey(employee.getEmployeeId())){
            return "Employee with Id" + employee.getEmployeeId() + "already exist";
        }
        employeeHashMap.put(employee.getEmployeeId(),employee);
        return  "Employee" + employee.getName() + " created successfully";
    }


    public String removeEmployee(T employeeId) throws EmployeeNotFoundException {

        if (!employeeHashMap.containsKey(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " does not exist");
        }
        employeeHashMap.remove(employeeId);
        return "Employee with ID " + employeeId + " has been removed successfully";
    }


    public Collection<Employee<T>> getAllEmployees () {
       return  employeeHashMap.values();
    }

    public String updateEmployeeDetails(T employeeId, String field, Object newValue) throws EmployeeNotFoundException {
        Employee<T> employee = employeeHashMap.get(employeeId);


        if (employee == null) {

            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " does not exist");
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


    public String sortByDepartment(String department) throws NoEmployeesFoundException {
        if (employeeHashMap.isEmpty()) {
            throw new NoEmployeesFoundException("No employee data available in the system.");
        }

        List<Employee<T>> filtered = employeeHashMap.values().stream()
                .filter(emp -> emp.getDepartment() != null && emp.getDepartment().equalsIgnoreCase(department))
                .sorted(Comparator.comparing(Employee::getName, Comparator.nullsLast(String::compareTo)))
                .toList();

        if (filtered.isEmpty()) {
            throw new NoEmployeesFoundException("No employees found in the department: " + department);
        }

        return filtered.stream()
                .map(Employee::toString)
                .collect(Collectors.joining("\n"));
    }


    public String findEmployeesByName(String searchTerm) throws EmployeeSearchException {
        List<Employee<T>> filtered = employeeHashMap.values().stream()
                .filter(emp -> emp.getName() != null && emp.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();

        if (filtered.isEmpty()) {
            throw new EmployeeSearchException("No employees found with name containing: " + searchTerm);
        }

        return filtered.stream()
                .map(Employee::toString)
                .collect(Collectors.joining(", "));
    }


    public String findEmployeesByRating(double minRating) throws EmployeeSearchException {
        List<Employee<T>> filtered = employeeHashMap.values().stream()
                .filter(emp -> emp.getPerformanceRating() >= minRating)
                .toList();

        if (filtered.isEmpty()) {
            throw new EmployeeSearchException("No employees found with rating >= " + minRating);
        }

        return filtered.stream()
                .map(Employee::toString)
                .collect(Collectors.joining(", "));
    }



    public String findEmployeesBySalaryRange(double minSalary, double maxSalary) throws EmployeeSearchException {
        List<Employee<T>> filtered = employeeHashMap.values().stream()
                .filter(emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary)
                .toList();

        if (filtered.isEmpty()) {
            throw new EmployeeSearchException("No employees found with salary between " + minSalary + " and " + maxSalary);
        }

        return filtered.stream()
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








