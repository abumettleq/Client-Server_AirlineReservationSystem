package product.client_final.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import product.client_final.main.*;

public class TicketController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView img;

    private TicketShop ticket;
    private MyShopListener myListener;

    private Inventory ticket2;
    private MyInventoryListener myListener2;

    @FXML
    private void click2() {
        myListener2.onClickListener(ticket2);
    }

    @FXML
    private void click() {
        myListener.onClickListener(ticket);
    }

    void setData(TicketShop ticket, MyShopListener myListener) {
        this.ticket = ticket;
        this.myListener = myListener;
        nameLabel.setText(ticket.getAirLine_Name());
        priceLabel.setText(" " + MainPanel.CURRENCY + ticket.getDiscountPrice());
        Image image = new Image(getClass().getResourceAsStream("/airline-boarding-pass-v1.jpg"));
        img.setImage(image);
    }

    void setData2(Inventory ticket, MyInventoryListener myListener) {
        this.ticket2 = ticket;
        this.myListener2 = myListener;
        nameLabel.setText(ticket.getAirLine_Name());
        priceLabel.setText(" " + MainPanel.CURRENCY + ticket.getDiscountPrice());
        Image image = new Image(getClass().getResourceAsStream("/airline-boarding-pass-v1.jpg"));
        img.setImage(image);
    }

}