package com.example.employeesystem.employee;

public class Employee<T> implements Comparable<Employee<T>> {
    T employeeId;
    String name;
    String department;
    double salary;
    double performanceRating;
    int yearsOfExperience;
    boolean isActive;

    public Employee(T employeeId , String name , String department, double salary , double performanceRating, int yearsOfExperience ,boolean isActive ){
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.salary =  salary;
        this.performanceRating = performanceRating;
        this.yearsOfExperience = yearsOfExperience;
        this.isActive = isActive;
    }

    public T getEmployeeId(){
        return employeeId;
    }

    public String getName(){
        return  name;
    }

    public String getDepartment(){
        return department;
    }

    public double getSalary(){
        return salary;
    }

    public double getPerformanceRating(){
        return  performanceRating;
    }

    public int getYearsOfExperience(){
        return yearsOfExperience;
    }

    public  boolean getisActive(){
        return  isActive;
    }

    @Override
    public int compareTo(Employee<T> other){
      return  Integer.compare(other.yearsOfExperience,this.yearsOfExperience);
    }

    public void setSalary(double newSalary) {
        this.salary =newSalary;
    }



}
