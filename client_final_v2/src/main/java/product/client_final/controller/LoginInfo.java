package product.client_final.controller;
import javafx.scene.control.Alert;
import product.client_final.helper.AlertHelper;
import product.client_final.main.Socket;

import java.io.IOException;

public class LoginInfo {

    Socket socket;
    {
        try {
            socket = new Socket();
        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Can not connect to the server.","Maybe server is under maintenance, so try again later.");
            AlertHelper.Exit();
        }
    }

    public static String login_email;
    public static String login_password;
    public static String login_name;
    public static String login_dob;
    public static String login_mobileno;
    public static String login_createdon;

    public LoginInfo() {
    }

    public void get_updatedInfo() throws IOException {
        socket.send("get_info="+login_email);
        login_name = socket.receive();
        login_dob = socket.receive();
        login_mobileno = socket.receive();
        login_createdon = socket.receive();
        login_password = socket.receive();
    }
}
