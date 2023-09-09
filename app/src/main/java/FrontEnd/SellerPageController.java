package FrontEnd;

import VendingMachine.App;
import VendingMachine.ProductData;
import VendingMachine.Seller;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SellerPageController {
    public Button modify;
    public Pane modifyMenu;
    public ListView<String> availableProducts;
    public ListView<String> summary;
    public TextField previousCode;
    public TextField newCode;
    public TextField name;
    public ChoiceBox<String> category;
    public TextField quantity;
    public TextField dollars;
    public TextField cents;

    public void initialize() {
        availableProducts.setVisible(false);
        modifyMenu.setVisible(false);
        summary.setVisible(false);

        category.getItems().addAll(ProductData.getAllCategories());
    }

    public void availableShow(ActionEvent actionEvent) {
        availableProducts.setItems(FXCollections.observableArrayList(Seller.getAvailableProducts()));
        availableProducts.setVisible(true);
        modifyMenu.setVisible(false);
        summary.setVisible(false);
    }

    public void summaryShow(ActionEvent actionEvent) {
        summary.setItems(FXCollections.observableArrayList(Seller.productSummary()));
        summary.setVisible(true);
        availableProducts.setVisible(false);
        modifyMenu.setVisible(false);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void modifyShow(ActionEvent actionEvent) {
        modifyMenu.setVisible(true);
        availableProducts.setVisible(false);
        summary.setVisible(false);
        quantity.setPromptText("<= 15");
    }

    public void submit(ActionEvent actionEvent) {
        String previousCode = this.previousCode.getText();
        String newCode = this.newCode.getText();
        String name = this.name.getText();
        String category = this.category.getValue();
        // default no input is null...
        if (category == null) {
            category = "";
        }
        String quantity = this.quantity.getText();
        String dollars = this.dollars.getText();
        String cents = this.cents.getText();
        try {
            Seller.modifyProduct(previousCode, newCode, name, category, quantity, dollars, cents);
            AlertWindow.alert("successfully modified");
        } catch (Exception e) {
            AlertWindow.alert(e.getMessage());
        }
        new App().changeScene("SellerPage.fxml", "Welcome Seller");
    }
}
