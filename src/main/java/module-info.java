module com.example.employeesystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.employeesystem to javafx.fxml;
    exports com.example.employeesystem;
    exports com.example.employeesystem.employee;
    opens com.example.employeesystem.employee to javafx.fxml;
}