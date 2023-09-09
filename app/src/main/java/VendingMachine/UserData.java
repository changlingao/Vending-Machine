package VendingMachine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserData extends DataBase {
    public static String[] roles = {"customer", "cashier", "seller", "owner"};
    // cases for this function to return false are either the file in read unsuccessfully, file is empty
    // username is not exits or password is incorrect

    public static Customer getAnonymousCustomer() {
        return new Customer("anonymous", "password");
    }

    public static boolean checkPassword(String username, String password) {
        // assume every first item in the row is the username and every second item is the password
        if (username == null) {
            return false;
        }
        // check if the username given exists in the file, if not returns false
        ArrayList<String> lst = DataBase.Read(userFile);
        if (lst == null) {
            return false;
        } // read is unsuccessful

        String user = null;
        int row = -1;
        for (int i = 0; i < lst.size(); i++) {
            user = lst.get(i).split(",")[0];
            if (user.equals(username)) {
                row = i;
                break;
            }
        }
        // the username is not found in the file
        if (row == -1) {
            return false;
        } else {
            String pass = lst.get(row).split(",")[1];
            if (pass.equals(password)) {
                return true;
            }
        }
        return false;
    }

    // return the row index (from 0) that the user is in, otherwise return -1
    public static int checkuserexists(String username) {
        ArrayList<String> lst = DataBase.Read(userFile);
        if (lst == null) {
            return -1;
        } // read is unsuccessful

        String user;
        int row;
        for (int i = 0; i < lst.size(); i++) {
            user = lst.get(i).split(",")[0];
            if (user.equals(username)) {
                row = i;
                return row; // username match
            }
        }
        return -1;
    }

    public static String getRole(String username) {
        int row = checkuserexists(username);
        if (row == -1)
            return null; // the user does not exist
        ArrayList<String> lst = getUserList();
        String role = lst.get(row).split(",")[2];
        return role.toLowerCase();
    }

    public static String[] getRecentFive(String username) {
        if (username == null || username.replaceAll("^[ \t]+|[ \t]+$", "").equals("")) {
            username = "anonymous";
        }
        int row = checkuserexists(username);
        if (row == -1) {
            return null; // the user give does not exist in the database
        } else {
            String listOFItem = getUserList().get(row).split(",")[5];
            listOFItem = listOFItem.replaceAll("^[ {]+|[ }]+$", ""); // remove curly bruckets
            return listOFItem.split(";");
        }

    }

    public static boolean checkValid(String usernameORpassword){
        // username or password can't have ,;or space in it
        String[] inValid = {" ", ",", "{", ";", "}"};
        for (String each: inValid) {
            if(usernameORpassword.contains(each)){ // if the string conatin any above
                return false;
            }
        }
        if(usernameORpassword.strip().equals("")) return false;
        return true;
    }

    // return 0 if update success, 1 if there is an error
    public static int updateRecentFive(String username, HashMap<String, Integer> shoppingCart) {
        // convert shoppingCart to array of product names
        ArrayList<String> names = new ArrayList<>();
        for (String code: shoppingCart.keySet()) {
            names.add(ProductData.getName(code));
        }
        // String[] newItems = names.toArray();

        if (username == null || username.replaceAll("^[ \t]+|[ \t]+$", "").equals("")) {
            username = "anonymous";
        }
        int row = checkuserexists(username);
        ArrayList<String> comb = new ArrayList<>();
        String[] olditems = getRecentFive(username);
        if (row == -1) {
            return 1; // the user give does not exist in the database
        } else {
            String[] listOFItem = getUserList().get(row).split(","); //
             // items originally saved

            if (olditems != null){
                for (String name: olditems
                     ) { comb.add(name);

                }
            }
            comb.removeAll(names); // com remove all duplicates
            // comb.addAll(Arrays.asList(newItems));
            comb.addAll(names);
            //comb.addAll(names);
            while (comb.size() > 5) // if the combines list is greater than 5
            {
                comb.remove(0);

            }

            String knite = "{" + String.join(";", comb) + "}";
            listOFItem[5] = knite; // store in the 5th place
            String finalString = String.join(",", listOFItem);
            ArrayList<String> lst = getUserList();
            lst.set(row, finalString);
            Write(lst, userFile); // write the whole array back in the file
//            listOFItem = listOFItem.replaceAll("^[ {]+|[ }]+$", ""); // remove curly bruckets
//            String[] item =  listOFItem.split(";");

        }
        return 0;
    }


    // return in form of arraylist the user file
    // every entry in the array represents a user
    public static ArrayList<String> getUserList() {
        ArrayList<String> lst = DataBase.Read(userFile);
        return lst;
    }

    // assume when a user is added, there is no credit card initially and no last five purchased item

    // if there is an error, method returns -1, otherwise return 0


    public static Customer addUser(String username, String password, String role) {
        String insert = username + "," + password + "," + role + "," + ",,{;;;;}"; // place holder for later addine card and products
        ArrayList<String> list = getUserList();
        list.add(insert);
        /// Write(list, userfile);
        // bug: return new Customer();
        switch (role) {
            case "customer": {
                Write(list, userFile);
                return new Customer(username, password);}
            case "seller": {
                Write(list, userFile);
                return new Seller(username, password);}
            case "cashier": {
                Write(list, userFile);
                return new Cashier(username, password);}
            case "owner":{
                Write(list, userFile);
                return new Owner(username, password);}
            default:
                return null;
        }
    }


    /////////////////////// END /////////////////////
    public static Customer loginOrCreate(String username, String password) throws Exception {
        username = username.strip();
        password = password.strip();

        if (!checkValid(username) || !checkValid(password))
            throw new IllegalArgumentException("can not contain comma or semi column");
        boolean isEmpty = false;
        if (username == null || username.replaceAll("^[ \t]+|[ \t]+$", "").equals("")) {
            username = "anonymous";
            password = "password";
            String[] history = getRecentFive(username);
            isEmpty = true;
            return new Customer(history);
        }
        int row = checkuserexists(username);
        if (row == -1) { // user does not exist in the database, create one
            return addUser(username, password, "customer");
        } else if (!checkPassword(username, password)) {
            throw new IllegalArgumentException("password is incorrect");
        } else { // user exists in the database
            ArrayList<String> lst = getUserList();
            String role = lst.get(row).split(",")[2];
            String card = lst.get(row).split(",")[3];
            String number = lst.get(row).split(",")[4];
            String[] history = getRecentFive(username);
            switch (role.toLowerCase()) {
                case "cashier":
                    //throw new Exception("userName or PassWard is wrong");
                    return new Cashier(username, password, card, number, history);
                case "seller":
                    //throw new Exception("userName or PassWard is wrong");
                    return new Seller(username, password, card, number, history);
                case "owner":
                    //throw new Exception("userName or PassWard is wrong");
                    return new Owner(username, password, card, number, history);
                case "customer":
                    //throw new Exception("userName or PassWard is wrong");
                    return new Customer(username, password, card, number, history);

            }
            throw new IllegalArgumentException("role can not be interpreted");
        }
    }

        // add card to given username, return 1 if success, return 0 otherwise
        public static int updateCard (String Username, String cardname, String cardnumber){
            int row = checkuserexists(Username);
            if (row == -1) return 0;
            ArrayList<String> users = getUserList();
            // check if card name exists
            if (Payment.verifyCard(cardname, cardnumber)) {
                String user = users.get(row);
                String[] info = user.split(",");
                info[3] = cardname;
                info[4] = cardnumber;
                users.set(row, String.join(",", info));
                Write(users, userFile);
                return 1;
            }
            // check if password is correct
            // check if user exists
            // add the card
            return 0;
        }

        public static void remove(String username){
        if (username.equals("") || username.equals("anonymous")){
            throw new IllegalArgumentException("input can not be null or anonymous");

        }
        int row = checkuserexists(username);
        if (row == -1){
            throw new IllegalArgumentException("username not found");

        }
        ArrayList<String> users = getUserList();
            users.remove(row);
            Write(users, userFile);

        }

    public static List<String> getAllUsers() {
        String prompt = "(user_name, role)";
        ArrayList<String> original_users = Read(userFile);
        ArrayList<String> users = new ArrayList<>();
        users.add(prompt);
        for (String s: original_users) {
            String[] attributes = s.split(",");
            String cell = attributes[0] + ", " + attributes[2];
            users.add(cell);
        }
        return users;
    }

    public static String[] getAllUsernames() {
        ArrayList<String> original_users = getUserList();
        int size = original_users.size() - 1;
        ArrayList<String> users = new ArrayList<>();
        int i = 0;
        for (String s : original_users) {
            String[] attributes = s.split(",");
            String cell = attributes[0];
            String role = attributes[2];
            if (!cell.equals("anonymous") && !role.equals("customer")) {
                users.add(cell);
                i++;
            }


        }
        String[] user = users.toArray(new String[users.size()]);
        return user;

    }
}

