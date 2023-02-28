/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class MyInfoController implements Initializable {

    LoginInfo loginInfo = new LoginInfo();

    @FXML
    Label Name = new Label();
    @FXML
    Label DOB = new Label();
    @FXML
    Label MobileNo = new Label();
    @FXML
    Label CreatedOn = new Label();

    public MyInfoController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            loginInfo.get_updatedInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Name.setText(LoginInfo.login_name);
        DOB.setText(LoginInfo.login_dob);
        MobileNo.setText(LoginInfo.login_mobileno);
        CreatedOn.setText(LoginInfo.login_createdon);
    }
}
