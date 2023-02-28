/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import product.client_final.helper.AlertHelper;
import product.client_final.main.Socket;

public class UpateInfoController implements Initializable {

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
    Button saveNewName, saveNewDOB, saveNewPass, saveNewMobile;

    @FXML
    TextField newFName, newMName, newLName, prevPassword, newPassword, cnewPassword, newMobile;

    @FXML
    DatePicker newDOB = new DatePicker();

    public UpateInfoController() {
    }

    void NewName(ActionEvent event) {
        int check_name = check_newName();
        if(check_name == 0)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Name Entered", "First name requires filling.");
            newFName.requestFocus();
        }
        else if(check_name == 1)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Name Entered", "Middle name requires filling.");
            newMName.requestFocus();
        }
        else if(check_name == 2)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Name Entered", "Last name requires filling.");
            newLName.requestFocus();
        }
        else
        {
            try {
                update_newName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    void NewDOB(ActionEvent event)  {
        if(check_newDOB() == 0)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Birthdate Entered", "Please choose a valid birthdate before saving.");
            newDOB.requestFocus();
        }
        else
        {
            try {
                update_newDOB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void NewPassword(ActionEvent event) {
        int check_password = check_newPassword();
        if(check_password == 0)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Password Entered", "Previous password must not be empty.");
            prevPassword.requestFocus();
        }
        else if(check_password == 1)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Password Entered", "New password must not be empty.");
            newPassword.requestFocus();
        }
        else if(check_password == 2)
        {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "Invalid Password Entered", "Confirmation password must not be empty.");
            cnewPassword.requestFocus();
        }
        else if(check_password == 3)
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Confirmation password and new password are not same!", "Try rewriting the confirmation password if not the new password.");
            cnewPassword.requestFocus();
        }
        else
        {
            try {
                update_newPassword();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void NewMobile(ActionEvent event)
    {
        int check_mobile = check_newMobile();
        if(check_mobile == 0)
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Mobile number text field is empty!", "Fill the mobile number text field before saving.");
            newMobile.requestFocus();
        }
        else update_newMobile();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate minDate = LocalDate.of(1942, 1, 1);
        LocalDate maxDate = LocalDate.now();
        newDOB.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }});

        saveNewMobile.setOnAction(this::NewMobile);
        saveNewPass.setOnAction(this::NewPassword);
        saveNewDOB.setOnAction(this::NewDOB);
        saveNewName.setOnAction(this::NewName);
    }

    private int check_newName()
    {
        if(newFName.getText() == null || newFName.getText().equals(""))
        {
            return 0;
        }
        else if(newMName.getText() == null || newMName.getText().equals(""))
        {
            return 1;
        }
        else if(newLName.getText() == null || newLName.getText().equals(""))
        {
            return 2;
        }
        else return -1;
    }
    private void update_newName() throws IOException {
        socket.send("update_info="+ LoginInfo.login_email);
        socket.send("first_name="+newFName.getText());

        socket.send("update_info="+ LoginInfo.login_email);
        socket.send("middle_name="+newMName.getText());

        socket.send("update_info="+ LoginInfo.login_email);
        socket.send("last_name="+newLName.getText());

        if(socket.receive().equals("updated"))
        {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Your name has been updated!", "Your name is required in ticket booking/canceling process, keep it always right.");
        }
        else
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "There was an error updating your name.", "Check your full name, try not to use symbols in your name.");
        }
    }

    private int check_newDOB()
    {
        if(newDOB.getEditor().getText() == null || newDOB.getEditor().getText().equals(""))
        {
            return 0;
        }
        else return -1;
    }
    private void update_newDOB() throws IOException {
        socket.send("update_info="+ LoginInfo.login_email);
        socket.send("birthdate="+newDOB.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if(socket.receive().equals("updated"))
        {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Your birthdate has been updated!", "Your birthdate is important to us, happy birthday maybe?");
        }
        else
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "There was an error updating your birthdate.", "Try choosing a valid birthdate then save again.");
        }
    }

    private int check_newPassword()
    {
        if(prevPassword.getText() == null || prevPassword.getText().equals(""))
        {
            return  0;
        }
        else if(newPassword.getText() == null || newPassword.getText().equals(""))
        {
            return 1;
        }
        else if(cnewPassword.getText() == null || cnewPassword.getText().equals(""))
        {
            return 2;
        }
        else if(!cnewPassword.getText().equals(newPassword.getText()))
        {
            return 3;
        }
        else return -1;
    }
    private void update_newPassword() throws IOException {
        socket.send("update_info="+ LoginInfo.login_email);
        socket.send("password="+prevPassword.getText());
        if(socket.receive().equals("Continue"))
        {
            socket.send(cnewPassword.getText());
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Your password has been updated!", "It is good to update your password from time to time to keep your account protected.");
        }
        else
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Your previous password is wrong!", "Try re-entering your previous password.");
            prevPassword.requestFocus();
        }
    }

    private int check_newMobile()
    {
        if (newMobile.getText() == null || newMobile.getText().equals(""))
        {
            return 0;
        }
        else return -1;
    }
    private void update_newMobile()
    {
        socket.send("update_info="+ LoginInfo.login_email);
        socket.send("mobile_no="+newMobile.getText());
        AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Your mobile number has been updated!", "Your phone number is used for security reasons, update it when required.");
    }

}
