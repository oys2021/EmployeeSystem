package com.example.employeesystem;

import com.example.employeesystem.employee.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.transformation.FilteredList;

import java.util.Comparator;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;


public class EmployeeFormController {

    @FXML private TextField nameField;
    @FXML private TextField departmentField;
    @FXML private TextField salaryField;
    @FXML private TextField ratingField;
    @FXML private TextField experienceField;
    @FXML private CheckBox isActiveBox;

    @FXML private TextField searchNameField;
    @FXML private TextField searchDepartmentField;
    @FXML private TextField minRatingField;
    @FXML private TextField maxRatingField;

    @FXML private ComboBox<String> sortComboBox;

    // Add to class properties
    private SortedList<Employee<UUID>> sortedEmployees;

    private FilteredList<Employee<UUID>> filteredEmployees;

    @FXML private TableView<Employee<UUID>> employeeTable;
    @FXML private TableColumn<Employee<UUID>, String> nameCol;
    @FXML private TableColumn<Employee<UUID>, String> departmentCol;
    @FXML private TableColumn<Employee<UUID>, Double> salaryCol;
    @FXML private TableColumn<Employee<UUID>, Double> ratingCol;
    @FXML private TableColumn<Employee<UUID>, Integer> experienceCol;
    @FXML private TableColumn<Employee<UUID>, Boolean> isActiveCol;

    private EmployeeDatabase<UUID> database = new EmployeeDatabase<>();
    private ObservableList<Employee<UUID>> employeeList = FXCollections.observableArrayList();

    private static final Logger logger = Logger.getLogger(EmployeeFormController.class.getName());


    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        departmentCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDepartment()));
        salaryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getSalary()).asObject());
        ratingCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPerformanceRating()).asObject());
        experienceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getYearsOfExperience()).asObject());
        isActiveCol.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().getisActive()).asObject());


        filteredEmployees = new FilteredList<>(employeeList, p -> true);
        sortedEmployees = new SortedList<>(filteredEmployees);

        employeeTable.setItems(sortedEmployees);


        sortComboBox.getItems().addAll(
                "Name (A-Z)",
                "Name (Z-A)",
                "Salary (High to Low)",
                "Salary (Low to High)",
                "Rating (High to Low)",
                "Experience (High to Low)"
        );

    }


    @FXML
    public void addEmployee() {
        try {
            String name = nameField.getText();
            String dept = departmentField.getText();
            String salaryStr = salaryField.getText();
            String ratingStr = ratingField.getText();
            String expStr = experienceField.getText();
            boolean isActive = isActiveBox.isSelected();

            if (name == null || name.trim().isEmpty() ||
                    dept == null || dept.trim().isEmpty() ||
                    salaryStr == null || salaryStr.trim().isEmpty() ||
                    ratingStr == null || ratingStr.trim().isEmpty() ||
                    expStr == null || expStr.trim().isEmpty()) {

                showAlert("Input Error", "All fields must be filled in.");
                return;
            }

            double salary = Double.parseDouble(salaryStr);
            double rating = Double.parseDouble(ratingStr);
            int exp = Integer.parseInt(expStr);

            Employee<UUID> emp = new Employee<>(UUID.randomUUID(), name, dept, salary, rating, exp, isActive);
            database.addEmployee(emp);
            employeeList.add(emp);

            clearForm();

        } catch (EmployeeDetailRequiredException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            showAlert("Error:", e.getMessage());

        } catch (InvalidSalaryException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            showAlert("Error:", ex.getMessage());

        } catch (InvalidDepartmentException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
            showAlert("Error:", ex.getMessage());

        } catch (NumberFormatException ex) {
            logger.log(Level.WARNING, "Invalid number input", ex);
            showAlert("Input Error", "Please enter valid numeric values for salary, rating, and experience.");

        }
    }


    @FXML
    public void searchEmployees() {
        String nameSearch = searchNameField.getText().trim().toLowerCase();
        String deptSearch = searchDepartmentField.getText().trim().toLowerCase();
        double minRating = parseDouble(minRatingField.getText().trim(), 0.0);
        double maxRating = parseDouble(maxRatingField.getText().trim(), Double.MAX_VALUE);
        double minSalary = parseDouble(minRatingField.getText().trim(), 0.0);
        double maxSalary = parseDouble(maxRatingField.getText().trim(), Double.MAX_VALUE);

        filteredEmployees.setPredicate(emp -> {
            boolean nameMatches = nameSearch.isEmpty() || emp.getName().toLowerCase().contains(nameSearch);
            boolean deptMatches = deptSearch.isEmpty() || emp.getDepartment().toLowerCase().contains(deptSearch);
            boolean ratingMatches = emp.getPerformanceRating() >= minRating && emp.getPerformanceRating() <= maxRating;
            boolean salaryMatches = emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary;
            return nameMatches && deptMatches && ratingMatches && salaryMatches;
        });

        if (filteredEmployees.isEmpty()) {
            showAlert("No Results", "No employees found matching the search criteria.");
            logger.info("Search returned no results.");
        }
    }



    @FXML
    public void clearSearch() {
        searchNameField.clear();
        searchDepartmentField.clear();
        minRatingField.clear();
        maxRatingField.clear();
        filteredEmployees.setPredicate(emp -> true);
    }

    private double parseDouble(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }




    @FXML
    public void removeSelectedEmployee() {
        Employee<UUID> selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an employee to remove.");
            return;
        }
        try {
            database.removeEmployee(selected.getEmployeeId());
            employeeList.remove(selected);
        } catch (EmployeeNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            showAlert("Error:",e.getMessage());
        }

    }


    @FXML
    private void handleSort() {
        String selected = sortComboBox.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Comparator<Employee<UUID>> comparator = null;

        switch (selected) {
            case "Name (A-Z)":
                comparator = Comparator.comparing(e -> e.getName().toLowerCase());
                break;
            case "Name (Z-A)":
                comparator = Comparator.comparing((Employee<UUID> e) -> e.getName().toLowerCase()).reversed();
                break;
            case "Salary (High to Low)":
                comparator = new EmployeeSalaryComparator<>();
                break;
            case "Salary (Low to High)":
                comparator = new EmployeeSalaryComparator<UUID>().reversed();
                break;
            case "Rating (High to Low)":
                comparator = new EmployeePerformanceComparator<>();
                break;
            case "Experience (High to Low)":
                comparator = Comparator.naturalOrder();
                break;
        }

        if (comparator != null) {
            sortedEmployees.setComparator(comparator);
        }
    }

    private void clearForm() {
        nameField.clear();
        departmentField.clear();
        salaryField.clear();
        ratingField.clear();
        experienceField.clear();
        isActiveBox.setSelected(false);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
