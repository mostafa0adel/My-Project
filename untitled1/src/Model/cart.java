package Model;


import Controller.CartController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the user cart
 * A cart has a hashmap to store user email and its items in the cart
 * A cart has a hashmap to store each item in the cart and its quantity
 * A cart has a total price
 * The cart items are loaded from the Cart.txt file
 * The cart items are displayed to the user
 * Author: @Candy_Crush Team
 * Version: 1.0
 * Date: 2023/05/4
 */
public class cart {
    // hashmap to store user email and its items in the cart
    private  HashMap<String, ArrayList<CatalogItem>> cartItems;
    // each item in the cart and its quantity
    private  HashMap<Integer, Double> cartItemQuantity;
    // total price of the cart
    private  double totalPrice;

    // construct
    /**
     * @param email
     * This constructor is used to create a new cart
     */
    public cart(String email) {
        // initialize the cart items
        cartItems = new HashMap<>();
        // initialize the cart item quantity
        cartItemQuantity = new HashMap<>();
        // initialize the total price
        totalPrice = 0;
    }




    /**
     * @return the cart items total price
     */
    public  double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    /**
     * @param catalogItem This is the catalog item to be added to the cart
     * @param quantity  This is the quantity of the catalog item to be added to the
     * @param email    This is the user email
     * This method is used to add items to the cart
     */
    public void addItemToCart(CatalogItem catalogItem, double quantity, String email) {
        boolean found = false;
        // check if the user has items in the cart
        if (cartItems.containsKey(email)) {
            System.out.println("hello form here");
            // get the user cart items
            ArrayList<CatalogItem> items = cartItems.get(email);
            for (CatalogItem item : items) {
                if (item.getId() == catalogItem.getId()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                // get the catalog item quantity
                Double itemQuantity = cartItemQuantity.get(catalogItem.getId());
                // update the catalog item quantity
                cartItemQuantity.put(catalogItem.getId(), itemQuantity + quantity);
            } else {
                // add the catalog item to the cart items
                items.add(catalogItem);
                cartItems.put(email, items);
                // add the catalog item and its quantity to the cart item quantity
                cartItemQuantity.put(catalogItem.getId(), quantity);
            }
        } else {
            // add the catalog item to the cart items
            ArrayList<CatalogItem> items = new ArrayList<>();
            items.add(catalogItem);
            cartItems.put(email, items);
            // add the catalog item and its quantity to the cart item quantity
            cartItemQuantity.put(catalogItem.getId(), quantity);
        }
        // calculate the total price
        totalPrice += (catalogItem.getPrice() - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                * quantity;
        System.out.println("Item added to cart successfully!");

    }

    /**
     * @param email
     * This method is used to clear the cart
     */
    public  void clearCart(String email) {
        // check if the user has items in the cart
        if (cartItems.containsKey(email)) {
            // get the user cart items
            ArrayList<CatalogItem> items = cartItems.get(email);
            // this loop iterates through the cart items and removes the cart items
            for (CatalogItem catalogItem : items) {
                cartItemQuantity.remove(catalogItem.getId());
            }
            // remove the user cart items
            cartItems.remove(email);
            // set the total price to zero
            totalPrice = 0;
            // save the cart
            File file = new File("Cart.txt");
            // delete the user line from the file
            try {
                Scanner sc = new Scanner(file);
                ArrayList<String> lines = new ArrayList<>();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] lineArray = line.split(",");
                    if (!lineArray[0].equals(email)) {
                        lines.add(line);
                    }
                }
                sc.close();
                FileWriter fw = new FileWriter(file);
                for (String line : lines) {
                    fw.write(line);
                    fw.write("\n");
                }
                fw.close();
            } catch (IOException e) {
                System.out.println("Cart file not found!");
            }
        }
    }

    /**
     * @return the cart items
     */
    public  HashMap<String, ArrayList<CatalogItem>> getCartItems() {
        return cartItems;
    }

    /**
     * @return the cart item quantity
     */
    public  HashMap<Integer, Double> getCartItemQuantity() {
        return cartItemQuantity;
    }

}

