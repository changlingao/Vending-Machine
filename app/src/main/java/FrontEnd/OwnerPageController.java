package FrontEnd;

import VendingMachine.App;
import VendingMachine.AppData;
import VendingMachine.TransactionData;
import VendingMachine.UserData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;

public class OwnerPageController {

    public TextField username;
    public TextField password;
    public TextField role;
    public Pane userinfo;
    public TextField cancellname;
    public Pane cancelluser;
    public ListView<String> cancelledTransactions;
    public ListView<String> allUsers;
   public ComboBox<String> addrole;
    public ComboBox<String> removeStaff;

    public void initialize(){
        userinfo.setVisible(false);
        cancelluser.setVisible(false);
        cancelledTransactions.setVisible(false);
        allUsers.setVisible(false);
    }

    public void cancell(){
        removeStaff.getItems().setAll(UserData.getAllUsernames());
        allUsers.setItems(FXCollections.observableArrayList(UserData.getAllUsers()));
        allUsers.setVisible(true);
        userinfo.setVisible(false);
        cancelluser.setVisible(true);
        //allUsers.setVisible(false);
        cancelledTransactions.setVisible(false);
    }

    public void confirmRemove(){
        String chosen_user = removeStaff.getValue();
        boolean isOwner = UserData.getRole(chosen_user).equals("owner");
        try{
            VendingMachine.UserData.remove(chosen_user);
            AlertWindow.alert("remove was successful!");
        }catch (IllegalArgumentException e){
            AlertWindow.alert(e.getMessage());
        }
        removeStaff.getItems().setAll(UserData.getAllUsernames());
        allUsers.setItems(FXCollections.observableArrayList(UserData.getAllUsers()));

        // if owner is removed, log out automatically
        if (isOwner) {
            AppData.reset();
            new App().changeScene("MainMenu.fxml", "Vending Machine");
        }
    }

    public void addUser(){
        //addrole =
        String[] role = {"seller", "cashier","owner"};
        //ddrole = new ComboBox(FXCollections.observableArrayList(role));
        addrole.getItems().setAll(role);
        //addrole.getItems().addAll("customer");
        allUsers.setItems(FXCollections.observableArrayList(UserData.getAllUsers()));
        userinfo.setVisible(true);
        cancelluser.setVisible(false);
        allUsers.setVisible(true);
        cancelledTransactions.setVisible(false);
    }

    public void seller() throws Exception{
       new App().changeScene("SellerPage.fxml", "Welcome Seller");

    }

    public void submitUser(){
        String chosen_role = addrole.getValue();
        try {
            VendingMachine.Owner.addNew(username.getText(), password.getText(), chosen_role);
            AlertWindow.alert("insertion was successful!");
        }catch (IllegalArgumentException e){
            AlertWindow.alert(e.getMessage());
        }
        String[] role = {"seller", "cashier","owner"};
        //ddrole = new ComboBox(FXCollections.observableArrayList(role));
        addrole.getItems().setAll(role);
        allUsers.setItems(FXCollections.observableArrayList(UserData.getAllUsers()));
        username.setText("");password.setText("");//role.setText("");


    }

    public void cashier(ActionEvent actionEvent) throws IOException {
        new App().changeScene("CashierPage.fxml", "Welcome Cashier");
    }

    public void cancelledTransactionShow(ActionEvent actionEvent) {
        cancelledTransactions.setItems(FXCollections.observableArrayList(TransactionData.getCancelledTransactions()));
        cancelledTransactions.setVisible(true);
        userinfo.setVisible(false);
        cancelluser.setVisible(false);
        allUsers.setVisible(false);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void getAllUsers(ActionEvent actionEvent) {
        allUsers.setItems(FXCollections.observableArrayList(UserData.getAllUsers()));
        allUsers.setVisible(true);
        userinfo.setVisible(false);
        cancelluser.setVisible(false);
        cancelledTransactions.setVisible(false);
    }


}
