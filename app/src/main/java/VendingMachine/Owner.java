package VendingMachine;

import java.util.ArrayList;

public class Owner extends Customer {

    /**
     * Constructor for Owner
     * @param username username
     * @param password password
     * @param cardholderName cardholder name
     * @param creditCardNumber credit card number
     * @param lastFiveProductID the id of the last five products the user bought
     * @throws IllegalArgumentException throw exception if the username is equal to anonymous username
     */
    public Owner(String username, String password, String cardholderName, String creditCardNumber, String[] lastFiveProductID) throws IllegalArgumentException {
        super(username, password, cardholderName, creditCardNumber, lastFiveProductID);
        this.role = "owner";    // assign correct role
    }
    public Owner(String username, String password){
        super(username, password);
        this.role = "owner";
    }

    public static void addNew(String username, String password, String role){ // returns the customer object and write to the file
        System.out.println(role);
        if (role == null) throw new IllegalArgumentException("role must be chosen");
        if (username.equals("anonymous")) throw new IllegalArgumentException("can not edit anonymous");
        int row = UserData.checkuserexists(username);
        if (row != -1){ // username exists in the database
            ArrayList<String> users = UserData.getUserList();
            String listOFItem = users.get(row).split(",")[5];
            String card = users.get(row).split(",")[3];
            String card2 = users.get(row).split(",")[4];
            String newInfo = username + "," + password + "," + role + "," + card + "," + card2 + "," + listOFItem;
            users.set(row, newInfo);
            DataBase.Write(users, UserData.userFile);
        } else
        if (!UserData.checkValid(username) || !UserData.checkValid(password)) {
            throw new IllegalArgumentException("username or password \n contain illegal arguments");
        }else{ // username or password is correct
            Customer added = UserData.addUser(username, password, role);
            if (added == null) throw new IllegalArgumentException("role don;t exists");

        }
    }
}
