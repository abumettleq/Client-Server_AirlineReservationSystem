/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import product.client_final.helper.AlertHelper;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import product.client_final.main.Socket;

public class RegisterController implements Initializable {
    private static class INFO
    {
        String signup_fname, signup_mname, signup_lname, signup_email, signup_password, signup_dob, signup_mobileno;
    }

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
    public TextField firstName;

    @FXML
    public TextField middleName;

    @FXML
    public TextField lastName;

    @FXML
    public DatePicker Birthdate = new DatePicker();

    @FXML
    public TextField email;

    @FXML
    public TextField MobileNumber;

    @FXML
    public TextField password;

    @FXML
    public TextField confirmPassword;

    @FXML
    public Button registerButton;

    Window window;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate minDate = LocalDate.of(1942, 1, 1);
        LocalDate maxDate = LocalDate.now();
        Birthdate.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }});
    }

    public RegisterController() {

    }

    @FXML
    public void register() {
        window = registerButton.getScene().getWindow();
        if (this.isValidated()) {
                INFO info = new INFO();
                info.signup_fname = firstName.getText();
                info.signup_mname = middleName.getText();
                info.signup_lname = lastName.getText();
                info.signup_dob = Birthdate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                info.signup_mobileno = MobileNumber.getText();
                info.signup_email = email.getText().toLowerCase(Locale.ROOT);
                info.signup_password = password.getText();

                String sign_this_up = "insert into Account(Email, Password, FName, MName, LName, DOB, Mobile_No) values ('" + info.signup_email + "','" + info.signup_password +"','" + info.signup_fname + "','" + info.signup_mname +"', '"+ info.signup_lname +"', TO_DATE('"+ info.signup_dob +"','YYYY-MM-DD'), '"+ info.signup_mobileno +"')";

                socket.send("signup="+info.signup_email);
                if (socket.receive().equals("Continue")) {
                    socket.send(sign_this_up);
                    this.clearForm();
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, "You have registered successfully.",
                            "You'll be able to login now, make sure you memorize your email and password.");
                } else {
                    AlertHelper.showAlert(Alert.AlertType.WARNING, "The email is already taken by someone else.",
                            "Maybe try to enter another email, or log in with the email entered if it's yours.");
                    email.requestFocus();
                }
        }
    }

    public boolean isValidated() {

        window = registerButton.getScene().getWindow();
        if (firstName.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "First name text field cannot be blank.");
            firstName.requestFocus();
        } else if (firstName.getText().length() < 2 || firstName.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "First name text field cannot be less than 2 and greator than 25 characters.");
            firstName.requestFocus();
        } else if (lastName.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Last name text field cannot be blank.");
            lastName.requestFocus();
        } else if (lastName.getText().length() < 2 || lastName.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Last name text field cannot be less than 2 and greator than 25 characters.");
            lastName.requestFocus();
        } else if (email.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Email text field cannot be blank.");
            email.requestFocus();
        } else if (email.getText().length() < 5 || email.getText().length() > 45) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Email text field cannot be less than 5 and greator than 45 characters.");
            email.requestFocus();
        } else if (password.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Password text field cannot be blank.");
            password.requestFocus();
        } else if (password.getText().length() < 5 || password.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Password text field cannot be less than 5 and greator than 25 characters.");
            password.requestFocus();
        } else if (confirmPassword.getText().equals("")) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Confirm password text field cannot be blank.");
            confirmPassword.requestFocus();
        } else if (confirmPassword.getText().length() < 5 || password.getText().length() > 25) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Confirm password text field cannot be less than 5 and greator than 25 characters.");
            confirmPassword.requestFocus();
        } else if (!password.getText().equals(confirmPassword.getText())) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Error",
                    "Password and confirm password text fields does not match.");
            password.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    private void clearForm() {
        firstName.clear();
        middleName.clear();
        lastName.clear();
        Birthdate.getEditor().clear();
        MobileNumber.clear();
        email.clear();
        password.clear();
        confirmPassword.clear();
    }

    @FXML
    public void showLoginStage() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LoginView.fxml")));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();
    }
}
