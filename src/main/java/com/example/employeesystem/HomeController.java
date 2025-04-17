package com.example.employeesystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomeController {
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void handleSubmit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("employee_form.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {

    }



}
