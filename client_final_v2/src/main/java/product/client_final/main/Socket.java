package product.client_final.main;

import javafx.scene.control.Alert;
import product.client_final.helper.AlertHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.AES.AESUtils;

public class Socket{

    final String secretKey = "JHKLXABYZC!!!!";
    private final java.net.Socket socket;
    private final PrintWriter toServer;
    private final BufferedReader fromServer;
    private String received;

    public Socket() throws IOException {
        socket = new java.net.Socket("localhost", 1234);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        toServer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String This)
    {
        try {
            toServer.println(AESUtils.encrypt(This,secretKey));
        }
        catch (Exception e)
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Could not connect to the server!","Check your internet connection.");
            AlertHelper.Exit();
        }
    }
    public String receive(){
        try {
            this.received = AESUtils.decrypt(fromServer.readLine(),secretKey);
        }
        catch (IOException e)
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Could not connect to the server!","Check your internet connection.");
            AlertHelper.Exit();
        }
        return this.received;
    }
}
