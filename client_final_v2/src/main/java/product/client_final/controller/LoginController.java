/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;
import product.client_final.helper.AlertHelper;
import product.client_final.main.Socket;

import java.io.IOException;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;


public class LoginController implements Initializable {

    Socket socket;
    {
        try {
            socket = new Socket();
        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Can not connect to the server.","Maybe server is under maintenance, so try again later.");
            AlertHelper.Exit();
        }
    }


    @FXML
    public TextField email;

    @FXML
    public TextField password;

    @FXML
    public Button loginButton;

    Window window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public LoginController() {}

    @FXML
    public void login() {

        if (this.isValidated()) {
            try {
                LoginInfo.login_email = email.getText().toLowerCase(Locale.ROOT);
                LoginInfo.login_password = password.getText();

                socket.send("login="+ LoginInfo.login_email);
                if (socket.receive().equals("Continue")) {
                    socket.send(LoginInfo.login_password);
                    if(socket.receive().equals("Login Success."))
                    {
                        LoginInfo.login_name = socket.receive();
                        LoginInfo.login_dob = socket.receive();
                        LoginInfo.login_mobileno = socket.receive();
                        LoginInfo.login_createdon = socket.receive();

                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();

                        Parent root = FXMLLoader.load(getClass().getResource("/MainPanelView.fxml"));
                        Scene scene = new Scene(root);

                        stage.setScene(scene);
                        stage.setTitle("Airline Reservation Application");
                        stage.getIcons().add(new Image("/icon.png"));
                        stage.show();
                    }
                    else {
                        AlertHelper.showAlert(Alert.AlertType.WARNING, "Password is incorrect.", "Try to re-write your password, Carefully.");
                        email.requestFocus();
                    }
                }
                else {
                    AlertHelper.showAlert(Alert.AlertType.WARNING, "Couldn't find email.", "Make sure you entered the email correctly.");
                    email.requestFocus();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isValidated() {

        window = loginButton.getScene().getWindow();
        if (email.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error",
                    "Username text field cannot be blank.");
            email.requestFocus();
        } else if (email.getText().length() < 5 || email.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error",
                    "Username text field cannot be less than 5 and greator than 25 characters.");
            email.requestFocus();
        } else if (password.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error",
                    "Password text field cannot be blank.");
            password.requestFocus();
        } else if (password.getText().length() < 5 || password.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Error",
                    "Password text field cannot be less than 5 and greator than 25 characters.");
            password.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    @FXML
    public void showRegisterStage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/RegisterView.fxml")));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();
    }
}
