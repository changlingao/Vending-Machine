package VendingMachine;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDataTest {
    public static ArrayList<String> userdatabefore = new ArrayList<>();
    @BeforeEach
    public void UpdateUserFileAddress(){
        UserData.userFile = "src/main/resources/usertest.txt";
        ProductData.productFile = "src/main/resources/product_test.txt";
        userdatabefore = UserData.Read(UserData.userFile);
    }

    @AfterEach
    public void ReturnUserFileBack(){
        UserData.Write(userdatabefore,UserData.userFile);
        UserData.userFile = "src/main/resources/user.txt";
        ProductData.productFile = "src/main/resources/product_inventory.txt";
    }

    @Test
    public void getAnonymousCustomerTest(){
        Customer a = UserData.getAnonymousCustomer();
        assertEquals("anonymous",a.getUsername());
        assertEquals("password",a.password);
    }

    @Test
    public void checkPasswordTest(){
        // If we input correct username and password.
        assertTrue(UserData.checkPassword("12","12"));

        // If we input correct username but incorrect password.
        assertFalse(UserData.checkPassword("cashier","24523"));

        // If we input incorrect username.
        assertFalse(UserData.checkPassword("Zelin","123"));
    }

    @Test
    public void checkUserExistsTest(){
        // Test whether we can get correct row for particular existing user.
        int row_actual = UserData.checkuserexists("345");
        int expected = 3;
        assertEquals(expected,row_actual);

        // Test username in machine doesn't exist.
        int row_actual2 = UserData.checkuserexists("Zelin");
        assertEquals(-1,row_actual2);
    }

    @Test
    // integration test.
    public void getRoleTest(){
        // Test username exists.
        String expected = "owner";
        String actual = UserData.getRole("12");
        assertEquals(expected,actual);

        // Test username doesn't exist.
        String actual1 = UserData.getRole("Zelin");
        assertEquals(null,actual1);
    }

    @Test
    public void getRecentFiveTest(){
        // Test correct input(username exists)

        String[] list_actual = UserData.getRecentFive("cashier");
        String[] expected = {"Mars","Mineral Water"};
        for(int i = 0; i<expected.length;i++){
            assertEquals(list_actual[i],expected[i]);
        }


        // Test incorrect input
        // username is blank
        String[] list_actual1 = UserData.getRecentFive(null);
        String[] expected1 = {"Thins","Pringles","Snickers","Mineral Water","Juice"};
        for(int i = 0; i<expected.length;i++){
            assertEquals(list_actual1[i],expected1[i]);
        }

        // Test "" input
        String[] list_actual2 = UserData.getRecentFive("");
        for(int i = 0; i<expected.length;i++){
            assertEquals(list_actual2[i],expected1[i]);
        }

        // Test username doesn't exist
        String[] list_actual3 = UserData.getRecentFive("Zelin");
        assertEquals(null,list_actual3);
    }

    @Test
    // Test whether string input by customer is valid without " ", ",", "{", "}", ";"
    public void checkValidTest(){
        assertFalse(UserData.checkValid(",;{}Z{Elin"));
        assertTrue(UserData.checkValid("Zelin"));
        assertFalse(UserData.checkValid("{Zeli}"));
    }

    @Test
    // Integration test with getRecentFive
    public void updateRecentFiveTest(){
        HashMap<String,Integer> shopping_cart = new LinkedHashMap<>();
        shopping_cart.put("5",3);
        shopping_cart.put("10",2);
        shopping_cart.put("4",2);
        // Test if we input username doesn't exist:
        assertEquals(1,UserData.updateRecentFive("Zelin",shopping_cart));


        // Test if input username is blank:
        // This should be anonymous.
        assertEquals(0,UserData.updateRecentFive("",shopping_cart));
        // Here we should update our test userdata file
        // Then we use getRecentFive to get them
        String[] expected = {"Snickers","Mineral Water","Juice","Smiths","Pepsi"};
        String[] actual = UserData.getRecentFive("anonymous");
        for(int i=0; i< expected.length;i++){
            assertEquals(expected[i],actual[i]);
        }

        // Test if input username is correct and exists:
        assertEquals(0,UserData.updateRecentFive("user1",shopping_cart));
        String[] expected1 = {"Juice", "Smiths", "Pepsi"};
        String[] actual1 = UserData.getRecentFive("user1");
        for(int i=0; i< actual1.length;i++){
            assertEquals(expected1[i],actual1[i]);
        }
    }

    @Test
    public void getUserListTest(){
        ArrayList<String> expected = new ArrayList<>();
        expected.add("anonymous,123,customer,,,{Thins;Pringles;Snickers;Mineral Water;Juice}");
        expected.add("user1,password1,customer,Charles,40691,{;;;;}");
        expected.add("12,12,owner,Ruth,55134,{Mineral Water;Sprite;Juice;;}");
        expected.add("345,345,customer,Kasey,60146,{Sprite;;;;}");
        expected.add("seller,123,seller,,,{Juice;;;;}");
        expected.add("cashier,123,cashier,,,{Mars;Mineral Water;;;}");
        expected.add("1,,cashier,,,{;;;;}");
        ArrayList<String> actual = UserData.getUserList();
        assertEquals(expected,actual);
    }

    @Test
    // In this test, we firstly get original user list,and add user in arraylist by ourselves,
    // then we add new user by function and get userlist again to get new userlist
    // compare these two user list.
    public void addUserTest(){
        ArrayList<String> original = UserData.getUserList();
        // Four different role:
        original.add("Zelin,12345,cashier,,,{;;;;}");
        original.add("Tom,12242,seller,,,{;;;;}");
        original.add("Jerry,12422,customer,,,{;;;;}");
        original.add("Mouse,1423,owner,,,{;;;;}");
        Customer Zelin = UserData.addUser("Zelin","12345","cashier");
        Customer Tom = UserData.addUser("Tom","12242","seller");
        Customer Jerry = UserData.addUser("Jerry","12422","customer");
        Customer Mouse = UserData.addUser("Mouse","1423","owner");
        ArrayList<String> actual = UserData.getUserList();
        // Check file
        assertEquals(original,actual);
        // Check role
        assertEquals(Zelin.getRole(),"cashier");
        assertEquals(Tom.getRole(),"seller");
        assertEquals(Jerry.getRole(),"customer");
        assertEquals(Mouse.getRole(),"owner");
    }

    @Test
    // Test Create account
    // Test invalid input username.
    public void loginOrCreateTest(){
        try{
            Customer invalid = UserData.loginOrCreate("*{zelin}","as{^");
        }catch(Exception e){
            assertEquals("can not contain comma or semi column",e.getMessage());
        }
    }

    @Test
    // Test Create account
    // Test input username is empty.
    public void loginOrCreateTest1(){
        try{
            Customer anonymous = UserData.loginOrCreate("","");
        }catch(Exception e){
            // Since "" means anonymous, so we cannot to log in or create an account as anonymous
            assertEquals("can not contain comma or semi column",e.getMessage());
        }
    }

    @Test
    // Test exist username with correct password.
    public void loginOrCreateTest2(){
        try{
            // Four role condition
            Customer exist_cust = UserData.loginOrCreate("345","345");
            assertEquals("345",exist_cust.getUsername());
            assertEquals("customer",exist_cust.getRole());

            Customer exist_seller = UserData.loginOrCreate("seller","123");
            assertEquals("seller",exist_seller.getUsername());
            assertEquals("seller",exist_seller.getRole());

            Customer exist_cashier = UserData.loginOrCreate("cashier","123");
            assertEquals("cashier",exist_cashier.getUsername());
            assertEquals("cashier",exist_cashier.getRole());

            Customer exist_owner = UserData.loginOrCreate("12","12");
            assertEquals("12",exist_owner.getUsername());
            assertEquals("owner",exist_owner.getRole());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    // Test exist username with incorrect password.
    public void loginOrCreateTest3(){
        try{
            Customer exist_incorrect = UserData.loginOrCreate("345","635");
        }catch(Exception e){
            assertEquals("password is incorrect",e.getMessage());
        }
    }

    @Test
    // Test not existed username and with one any password.
    public void loginOrCreateTest4(){
        try{
            ArrayList<String> expected = UserData.getUserList();
            expected.add("apple,123,customer,,,{;;;;}");
            Customer not_exist = UserData.loginOrCreate("apple","123");
            ArrayList<String> actual = UserData.getUserList();
            assertEquals("apple",not_exist.getUsername());
            assertEquals("customer",not_exist.getRole());
            assertEquals(expected,actual);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    // Test get all users:
    public void getAllUsersTest(){
        List<String> actual_users = UserData.getAllUsers();
        List<String> expected_users = new ArrayList<>();
        expected_users.add("(user_name, role)");
        expected_users.add("anonymous, customer");
        expected_users.add("user1, customer");
        expected_users.add("12, owner");
        expected_users.add("345, customer");
        expected_users.add("seller, seller");
        expected_users.add("cashier, cashier");
        expected_users.add("1, cashier");
        for(int i = 0; i<expected_users.size();i++){
            assertEquals(actual_users.get(i),expected_users.get(i));
        }
    }

    @Test
    // Test get all users' name excluding customer and anonymous
    public void getAllUserNameTest(){
        String[] actual = UserData.getAllUsernames();
        String[] expected = {"12","seller","cashier","1"};
        for(int i=0;i<actual.length;i++){
            assertEquals(expected[i],actual[i]);
        }
    }

}
