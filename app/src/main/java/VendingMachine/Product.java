package VendingMachine;

import java.util.List;

public class Product {
    public final static int defaultQuantityRemain = 7;
    private String id;
    private String category;
    private String name;
    private int quantityRemain;
    private int quantitySold;
    private int dollars;
    private int cents;

    public Product (String id, String category, String name, int quantityRemain, int quantitySold, int dollars, int cents) throws IllegalArgumentException {
        this.setId(id);
        this.setCategory(category);
        this.setName(name);
        this.setQuantityRemain(quantityRemain);
        this.setQuantitySold(quantitySold);
        this.setDollars(dollars);
        this.setCents(cents);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws IllegalArgumentException{
        if (id == null) {
            throw new IllegalArgumentException("Product id cannot be null or empty");
        } else if (id.equals("")) {   // do not update id if it is empty
            return;
        }
        // id must be unique
        List<Product> productList = ProductData.getProducts();
        for (Product p: productList) {
            if (id.equals(p.getId())) {
                throw new IllegalArgumentException("Product id already existed.");
            }
        }
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Product category cannot be null or empty");
        } else if (category.equals("")) {   // do not update category if it is empty
            return;
        }
        for (String c: ProductData.categories) {
            if (c.equals(category)) {
                this.category = category;
                return;
            }
        }
        throw new IllegalArgumentException("Product category is not valid.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        } else if (name.equals("")) { // do not update name if it is empty
            return;
        }
        this.name = name;
    }

    public int getQuantityRemain() {
        return quantityRemain;
    }

    public void setQuantityRemain(int quantityRemain) throws IllegalArgumentException{
        // Product quantity remain must be between 0 and 15
        if (quantityRemain < 0 || quantityRemain > 15) {
            throw new IllegalArgumentException("Product quantity remain must be \nbetween 0 and 15.");
        }
        this.quantityRemain = quantityRemain;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) throws IllegalArgumentException {
        // Product quantity sold must be non-negative
        if (quantitySold < 0) {
            throw new IllegalArgumentException("Product quantity sold must be \nnon-negative.");
        }
        this.quantitySold = quantitySold;
    }

    public int getDollars() {
        return dollars;
    }

    public void setDollars(int dollars) throws IllegalArgumentException {
        // Dollars must be non-negative
        if (dollars < 0) {
            throw new IllegalArgumentException("Dollars must be non-negative.");
        }
        this.dollars = dollars;
    }

    public int getCents() {
        return cents;
    }

    public void setCents(int cents) throws IllegalArgumentException {
        // Cents must be non-negative
        if (cents < 0) {
            throw new IllegalArgumentException("Cents must be non-negative.");
        }
        // Cents must be smaller than 100
        if (cents >= 100) {
            throw new IllegalArgumentException("Cents must be smaller than 100.");
        }
        this.cents = cents;
    }

    /**
     * Example:
     * return "1,Mineral Water,Drinks,7,0,2,50"
     * @return The string representation of the product which matches the format in products_inventory.txt file
     */
    @Override
    public String toString() {
        return this.getId() + "," +
                this.getName() + "," +
                this.getCategory() + "," +
                this.getQuantityRemain() + "," +
                this.getQuantitySold() + "," +
                this.getDollars() + "," +
                this.getCents();
    }

}

