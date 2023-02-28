/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import product.client_final.helper.AlertHelper;
import product.client_final.main.Socket;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;


public class ShopController implements Initializable {

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
    private final ObservableList<String> countries = FXCollections.observableArrayList();

    @FXML
    private final ObservableList<String> choices = FXCollections.observableArrayList();

    @FXML
    public ComboBox<String> oneway_from_country = new ComboBox<>(choices);

    @FXML
    public ComboBox<String> oneway_to_country = new ComboBox<>(countries);

    @FXML
    public ComboBox<String> oneway_type = new ComboBox<>(countries);

    @FXML
    public DatePicker oneway_departureDate = new DatePicker();

    @FXML
    public Button autofillButton, reservationButton;

    @FXML
    public TextField oneway_fname, oneway_fname2, oneway_email, oneway_mobile, oneway_seat, oneway_meal;

    public void autofill_button(ActionEvent event)
    {
        oneway_fname.setText(LoginInfo.login_name);
        oneway_fname2.setText(LoginInfo.login_name);
        oneway_email.setText(LoginInfo.login_email);
        oneway_mobile.setText(LoginInfo.login_mobileno);
    }

    public void oneway_reservation_button(ActionEvent event) /// Continue hereeeeeeeeeeeeeeeefaef aefawefafascasdcsaaaaaaaasac
    {
        if(oneway_email.getText() == null)
        {

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        choices.add(0,"Economy");
        choices.add(1,"Business");
        choices.add(2,"First");
        choices.add(3,"Premium");

        String[] locales1 = Locale.getISOCountries();
        for (String countrylist : locales1) {
            Locale obj = new Locale("", countrylist);
            String[] city = { obj.getDisplayCountry() };
            for (int x = 0; x < city.length; x++) {
                countries.add(obj.getDisplayCountry());
            }
        }
        oneway_from_country.setItems(countries);
        oneway_to_country.setItems(countries);
        oneway_type.setItems(choices);

        LocalDate minDate = LocalDate.now();
        oneway_departureDate.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate));
                    }});

    autofillButton.setOnAction(this::autofill_button);
    }
}
