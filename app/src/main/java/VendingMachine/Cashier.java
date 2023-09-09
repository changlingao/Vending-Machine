package VendingMachine;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Cashier extends Customer{

    /**
     * Constructor for Cashier
     * @param username username
     * @param password password
     * @param cardholderName cardholder name
     * @param creditCardNumber credit card number
     * @param lastFiveProductID the id of the last five products the user bought
     * @throws IllegalArgumentException throw exception if the username is equal to anonymous username
     */
    public Cashier(String username, String password, String cardholderName, String creditCardNumber, String[] lastFiveProductID) throws IllegalArgumentException {
        super(username, password, cardholderName, creditCardNumber, lastFiveProductID);
        this.role = "cashier";    // assign correct role
    }

    public Cashier(String username, String password){
        super(username, password);
        this.role = "cashier";
    }

    public static ArrayList<String> getSummaryNotes(LinkedHashMap<String, Integer> machine_notes){

        ArrayList<String> summary = new ArrayList<>();
        for(String key: machine_notes.keySet()){
            summary.add(key+": "+machine_notes.get(key));
        }
        return summary;
    }

    public static void SetMachineNotes(LinkedHashMap<String,Integer> machine_notes, String notes_type, String quantity) throws Exception{
        if (notes_type.equals("")) {
            throw new IllegalArgumentException("Please choose a value");
        }
        int q = 0;
        try {
            q = Integer.parseInt(quantity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        if (q <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        machine_notes.put(notes_type, q);
        Payment.WriteMachineNotes(machine_notes);
    }

}
