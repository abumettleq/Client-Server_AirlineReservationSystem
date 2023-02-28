/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPanelController implements Initializable {

    @FXML
    public BorderPane borderPane = new BorderPane();

    public List<Button> menus = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadFXML("HomeView");
    }

    public void changeButtonBackground(ActionEvent e) {
        for (Button menu : menus) {
            Button clickedButton = (Button) e.getSource();
            if (clickedButton == menu) {
                clickedButton.setStyle("-fx-text-fill:#f0f0f0;-fx-background-color:#2b2a26;");
            } else {
                if (menu != null) {
                    menu.setStyle("-fx-text-fill:#f0f0f0;-fx-background-color:#404040;");
                }
            }
        }
    }

    @FXML
    public void loadFXML(String fileName) {
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource("/" + fileName + ".fxml"));
            borderPane.setCenter(parent);

        } catch (IOException ex) {
            Logger.getLogger(MainPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void close() throws IOException {

        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.getIcons().add(new Image("/icon.png"));
        stage.show();
    }

    @FXML
    public void loadMyInfoView(ActionEvent e) {
        loadFXML("MyInfoView");
        changeButtonBackground(e);
    }

    @FXML
    public void loadInventoryView(ActionEvent e) {
        loadFXML("InventoryView");
        changeButtonBackground(e);
    }

    @FXML
    public void loadShopView(ActionEvent e) {
        loadFXML("ShopView");
        changeButtonBackground(e);
    }

    @FXML
    public void loadPage04View(ActionEvent e) {
        loadFXML("Page04View");
        changeButtonBackground(e);
    }

    @FXML
    public void loadPage05View(ActionEvent e) {
        loadFXML("Page05View");
        changeButtonBackground(e);
    }

    @FXML
    public void loadPage06View(ActionEvent e) {
        loadFXML("Page06View");
        changeButtonBackground(e);
    }

    @FXML
    public void loadPage07View(ActionEvent e) {
        loadFXML("Page07View");
        changeButtonBackground(e);
    }

    @FXML
    public void loadPage08View(ActionEvent e) {
        loadFXML("Page08View");
        changeButtonBackground(e);
    }

    @FXML
    public void loadPage09View(ActionEvent e) {
        loadFXML("Page09View");
        changeButtonBackground(e);
    }

    @FXML
    public void loadUpdateInfoView(ActionEvent e) {
        loadFXML("UpdateInfoView");
        changeButtonBackground(e);
    }

    @FXML
    public void loadHomeView(ActionEvent e) {
        loadFXML("HomeView");
        changeButtonBackground(e);
    }
}
