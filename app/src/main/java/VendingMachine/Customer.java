package VendingMachine;

public class Customer{
    protected static final String ANONYMOUS_USERNAME = "anonymous";
    protected static final String ANONYMOUS_PASSWORD = "password";
    protected String username;
    protected String password;
    protected String role;
    private String cardholderName;
    private String creditCardNumber;
    private String[] lastFiveProductID; // last five item

    /**
     * Constructor for anonymous user (customer)
     */
    public Customer(String[] lastFiveProductID) {
        this.username = ANONYMOUS_USERNAME;
        this.password = ANONYMOUS_PASSWORD;
        this.role = "customer";
        this.lastFiveProductID = lastFiveProductID;
        this.cardholderName = "";
        this.creditCardNumber = "";
    }

    /**
     * Constructor for actual user (customer)
     * @param username username
     * @param password password
     * @param cardholderName cardholder name
     * @param creditCardNumber credit card number
     * @param lastFiveProductID the id of the last five products the user bought
     * @throws IllegalArgumentException throw exception if the username is equal to anonymous username
     */
    public Customer(String username, String password, String cardholderName, String creditCardNumber, String[] lastFiveProductID) throws IllegalArgumentException {
        if(username.equals(ANONYMOUS_USERNAME)) {
            throw new IllegalArgumentException("User name cannot be " + ANONYMOUS_USERNAME);
        }
        this.username = username;
        this.password = password;
        this.role = "customer";
        this.lastFiveProductID = lastFiveProductID;
        this.cardholderName = cardholderName;
        this.creditCardNumber = creditCardNumber;
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * Getter method for last five product the customer bought
     * @return String array of length 5, i-th element is the product id of the last i-th product the customer bought
     */
    public String[] getLastFiveProductID() {
        return this.lastFiveProductID;
    }

    /**
     * Add credit card information for the customer by updating the <code>cardholderName</code> and
     * <code>creditCardNumber</code> attributes and the <code>user.txt</code> data file
     * @param cardholderName cardholder name
     * @param creditCardNumber credit card number
     */
    public void addCardInfo(String cardholderName, String creditCardNumber) {
        this.cardholderName = cardholderName;
        this.creditCardNumber = creditCardNumber;
        UserData.updateCard(this.getUsername(), cardholderName, creditCardNumber);
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * anonymous user
     * @param username username
     * @param password password
     */
    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "customer";
    }

    public String getRole() {
        return role;
    }
}
