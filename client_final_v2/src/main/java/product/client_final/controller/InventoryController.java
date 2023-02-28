/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.client_final.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import product.client_final.helper.AlertHelper;
import product.client_final.main.*;

public class InventoryController implements Initializable {

    Socket socket;
    {
        try {
            socket = new Socket();
        } catch (IOException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Can not connect to the server.","Maybe server is under maintenance, so try again later.");
            AlertHelper.Exit();
        }
    }

    private MyInventoryListener myinventoryListener = this::setChosenTicket;
    private String chosenId;

    @FXML
    private GridPane grid2;
    @FXML
    private Button cancelButton = new Button();
    @FXML
    private Label inventoryLabel;
    @FXML
    private Label inventoryPrice;
    @FXML
    private Label inventoryID;

    @FXML
    private void handleButton(ActionEvent actionEvent)
    {
        socket.send("cancel_ticket="+chosenId);
        socket.send(LoginInfo.login_email);
        tickets.clear();
        grid2.getColumnConstraints().clear();
        grid2.getRowConstraints().clear();
        grid2.getChildren().clear();
        show_tickets();
    }


    private final List<Inventory> tickets = new ArrayList<>();

    public InventoryController() {
    }

    private List<Inventory> getData() throws IOException {
        List<Inventory> tickets = new ArrayList<>();
        Inventory ticket = new Inventory();

        socket.send("show_inv="+ LoginInfo.login_email);
        if (socket.receive().equals("receive_this")) {
            while (!socket.receive().equals("search_ends_here")) {
                ticket.TicketInfo_setter(socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive(),
                        socket.receive());
                ticket.setImgSrc("/airline-boarding-pass-v1.jpg");
                tickets.add(ticket);
            }
        } else {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "You don't own any ticket!", "You need to book a ticket first in order to see it in your inventory.");
        }
        return tickets;
    }

    private void setChosenTicket(Inventory ticket) {
        cancelButton.setDisable(false);
        inventoryLabel.setText(ticket.getAirLine_Name());
        inventoryPrice.setText(MainPanel.CURRENCY + ticket.getDiscountPrice());
        inventoryID.setText("TicketID:  "+ticket.getTicketID());
        chosenId = ticket.getTicketID();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        show_tickets();
    }

    public void show_tickets()
    {
        try {
            tickets.addAll(getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tickets.size() > 0) {
            setChosenTicket(tickets.get(0));
            myinventoryListener = this::setChosenTicket;
        }
        int column = 0;
        int row = 1;
        try {
             for (Inventory ticket : tickets) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/inventoryTicket.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                TicketController ticketController = fxmlLoader.getController();
                ticketController.setData2(ticket, myinventoryListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid2.add(anchorPane, column++, row); //(child,column,row)
                //set grid2 width
                grid2.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid2.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid2.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid2 height
                grid2.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid2.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid2.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(20));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cancelButton.setOnAction(this::handleButton);
    }

}

