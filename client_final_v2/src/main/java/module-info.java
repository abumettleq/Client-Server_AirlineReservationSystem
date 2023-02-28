module product.client_final_v2 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires controlsfx;
    requires java.logging;

    opens product.client_final.controller;
    opens product.client_final.main to javafx.graphics;
    exports product.client_final.controller;

}