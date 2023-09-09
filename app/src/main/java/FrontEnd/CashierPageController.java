package FrontEnd;

import VendingMachine.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CashierPageController {
    public ListView<String> availableChange;
    public ListView<String> summaryTransactions;
    public ChoiceBox<String> cb;
    public TextField quantity;
    public Pane modifyMoney;

    public void initialize() {
        availableChange.setVisible(false);
        summaryTransactions.setVisible(false);
        modifyMoney.setVisible(false);
    }

    public void availableShow(ActionEvent actionEvent) {
        availableChange.setItems(FXCollections.observableArrayList(Cashier.getSummaryNotes(Payment.getMachineNotes())));
        availableChange.setVisible(true);
        summaryTransactions.setVisible(false);
        modifyMoney.setVisible(false);
    }

    public void summaryShow(ActionEvent actionEvent) {
        summaryTransactions.setItems(FXCollections.observableArrayList(TransactionData.getSuccessfulTransactions()));
        summaryTransactions.setVisible(true);
        availableChange.setVisible(false);
        modifyMoney.setVisible(false);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void modifyShow(ActionEvent actionEvent) {
        modifyMoney.setVisible(true);
        summaryTransactions.setVisible(false);
        availableChange.setVisible(false);

        cb.getItems().setAll("100$", "50$", "20$", "10$", "5$", "2$", "1$", "50c", "20c", "10c", "5c");
    }

    public void submit(ActionEvent actionEvent) {
        String value = cb.getValue();
        String quantity = this.quantity.getText();
        if (value == null) {
            value = "";
        }
        try {
            Cashier.SetMachineNotes(Payment.getMachineNotes(), value, quantity);
            AlertWindow.alert("successfully modified");
        } catch (Exception e) {
            AlertWindow.alert(e.getMessage());
        }

        new App().changeScene("CashierPage.fxml", "Welcome Cashier");
    }
}
