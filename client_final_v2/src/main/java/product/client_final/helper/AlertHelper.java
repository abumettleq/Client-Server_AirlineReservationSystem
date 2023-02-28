package product.client_final.helper;

import javafx.scene.control.Alert;
import static java.lang.System.exit;

public class AlertHelper {
    public static void showAlert(Alert.AlertType alertType, String message, String advice) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Message Window");
        alert.setHeaderText(message);
        alert.setContentText(advice);
        alert.showAndWait();
    }
    public static void Exit()
    {
        exit(-1);
    }
}
