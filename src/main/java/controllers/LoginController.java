package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;



public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;

    @FXML
    private void fazerLogin(ActionEvent event) throws IOException {
        if (emailField.getText().equals("c") && senhaField.getText().equals("c")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/militares.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } else {
            System.out.println("Login inválido");
        }
    }



    @FXML
    private void irParaCadastro(ActionEvent event) throws IOException {
        Parent cadastro = FXMLLoader.load(getClass().getResource("/views/cadastro.fxml"));
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(new Scene(cadastro));
    }




}
