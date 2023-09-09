package VendingMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductData extends DataBase{
    private static List<Product> products = new ArrayList<>();

    public static boolean nameExists(String name) {
        for (Product p: products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Store the 4 product categories available in the vending machine
     */
    public final static String[] categories;

    static {
        categories = new String[]{"Drinks", "Chocolates", "Chips", "Candies"};
    }


    public static int checkProductExists(String id) {
        ArrayList<String> lst = DataBase.Read(productFile);
        if (lst == null) {
            return -1;
        } // read is unsuccessful

        String user;
        int row;
        for (int i = 0; i < lst.size(); i++) {
            user = lst.get(i).split(",")[0];
            if (user.equals(id)) {
                row = i;
                return row; // username match
            }
        }
        return -1;
    }
    public static int[] getPrice(String ProductID){
        ArrayList<String> lst = Read(productFile);
        int row = checkProductExists(ProductID);
        if(row == -1){
            return new int[] {-1, -1}; // product don't exist in the file
        }else{
            String item = lst .get(row);
            String[] items = item.split(",");
            int cent = Integer.parseInt(items[6].strip());
            int dollar = Integer.parseInt(items[5].strip());
            return new int[] {dollar, cent};
        }
    }

    public static int getQuantityRemain(String ProductID){
        ArrayList<String> products = Read(productFile);
        for (int i = 0; i < products.size(); i++){
            String items = products.get(i);
            String[] lst = items.split(",");
            if (lst[0].equals(ProductID)){
                return Integer.parseInt(lst[3]);
            }
        }
        return -1;
    }

    public static int getQuantitySOLD(String ProductID){
        ArrayList<String> products = Read(productFile);
        for (int i = 0; i < products.size(); i++){
            String items = products.get(i);
            String[] lst = items.split(",");
            if (lst[0].equals(ProductID)){
                return Integer.parseInt(lst[4]);
            }
        }
        return -1;
    }

    public static String getCategory(String ProductID){
        ArrayList<String> products = Read(productFile);
        for (int i = 0; i < products.size(); i++){
            String items = products.get(i);
            String[] lst = items.split(",");
            if (lst[0].equals(ProductID)){
                return lst[2];
            }
        }
        return null;
    }

    public static String getName(String ProductID){
        ArrayList<String> products = Read(productFile);
        for (int i = 0; i < products.size(); i++){
            String items = products.get(i);
            String[] lst = items.split(",");
            if (lst[0].equals(ProductID)){
                return lst[1];
            }
        }
        return null;
    }

    public static List<Product> readInventory() {

        ArrayList<String> items = Read(productFile);
        products = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            String[] lst = items.get(i).split(",");
            String id = lst[0];
            String name = lst[1];
            String cat = lst[2];
            int remain = Integer.valueOf(lst[3]);
            int sold = Integer.valueOf(lst[4]);
            int[] price = getPrice(id);
            Product item = new Product(id,cat,name,remain,sold,price[0], price[1]);
            products.add(item);
        }

        return products;
    }

    public static void writeInventory(List<Product> newProducts) {
        ArrayList<String> items = new ArrayList<>();
        for (Product p: newProducts) {
            items.add(p.getId() + "," + p.getName() + "," + p.getCategory() + "," +
                    p.getQuantityRemain() + "," + p.getQuantitySold() + "," +
                    p.getDollars() + "," + p.getCents());
        }
        Write(items, productFile);
    }

    // for menu display
    public static List<String> getProductsByCategory(String category) {
        List<String> list = new ArrayList<>();
        for (Product p: products) {
            if (p.getCategory().equals(category)) {
                String cell = p.getId() + ". " + p.getName() + "\n" +
                        "Price: " + p.getDollars()+"."+p.getCents() + "\n" +
                        "Available: " + p.getQuantityRemain();
                list.add(cell);
            }
        }
        return list;
    }

    public static List<Product> getProducts() {
        return products;
    }


    public static void setProducts(List<Product> newProducts) {
        products = newProducts;
        // Convert Product to String
        ArrayList<String> newProductList = new ArrayList<>();
        for (Product newProduct: newProducts) {
            newProductList.add(newProduct.toString());
        }
        // Update data in file products_inventory.txt
        Write(newProductList, productFile);
    }

    /**
     * update txt and AppData
     * @param shoppingList one entry in the map: (code, quantity)
     */
    public static void updateStocks(HashMap<String, Integer> shoppingList) {
        ArrayList<String> products = Read(productFile);
        for (String code: shoppingList.keySet()) {
            int index = checkProductExists(code);
            String entry = products.get(index);
            String[] splits = entry.split(",");
            int sold = shoppingList.get(code);
            splits[4] = sold + "";
            int remain = Integer.parseInt(splits[3]) - sold;
            splits[3] = remain + "";

            StringBuilder updated = new StringBuilder(splits[0]);
            for (int i = 1; i < splits.length ; i++) {
                updated.append(",").append(splits[i]);
            }

            products.remove(entry);
            products.add(index, updated.toString());
        }

        Write(products, productFile);
        ProductData.products = readInventory();
    }

    /**
     * @return all categories
     */
    public static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        for (Product p: products) {
            if (!categories.contains(p.getCategory())) {
                categories.add(p.getCategory());
            }
        }
        return categories;
    }



}
