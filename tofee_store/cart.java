package tofee_store;

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
    private static HashMap<String, ArrayList<CatalogItem>> cartItems;
    // each item in the cart and its quantity
    private static HashMap<Integer, Double> cartItemQuantity;
    // total price of the cart
    private static double totalPrice;

    // construct
    /**
     * @param email
     * This constructor is used to create a new cart
     */
    public cart(String email) {
        // initialize the cart items
        cartItems = new HashMap<String, ArrayList<CatalogItem>>();
        // initialize the cart item quantity
        cartItemQuantity = new HashMap<Integer, Double>();
        // initialize the total price
        totalPrice = 0;
        // load the cart items from the Cart.txt file
        loadCartItems(email);

    }

    
    /**
     * @param email
     * This method is used to load the cart items from the Cart.txt file
     * The cart items are stored in the cart items hashmap
     * The cart item quantity are stored in the cart item quantity hashmap
     * The total price is calculated
     */
    public void loadCartItems(String email) {
        try {
            // create a new file
            File file = new File("Cart.txt");
            // create a new scanner
            Scanner sc = new Scanner(file);
            // loop through the file lines
            while (sc.hasNextLine()) {
                // get the line
                String line = sc.nextLine();
                // split the line by comma
                String[] lineArray = line.split(",");
                // check if the user email is equal to the email in the file
                if (lineArray[0].equals(email)) {
                    // get the catalog item by id
                    CatalogItem catalogItem = CatalogItem.getCatalogItem(Integer.parseInt(lineArray[1]));
                    // check if the cart items hashmap contains the user email
                    if (cartItems.containsKey(email)) {
                        // get the user cart items
                        ArrayList<CatalogItem> items = cartItems.get(email);
                        // add the catalog item to the user cart items
                        items.add(catalogItem);
                        totalPrice += (catalogItem.getPrice()
                                - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                                * Double.parseDouble(lineArray[2]);
                        // add the catalog item and its quantity to the cart item quantity hashmap
                        cartItemQuantity.put(catalogItem.getId(), Double.parseDouble(lineArray[2]));
                        // add the user email and its cart items to the cart items hashmap
                        cartItems.put(email, items);
                    } else {
                        // create a new arraylist to store the user cart items
                        ArrayList<CatalogItem> items = new ArrayList<CatalogItem>();
                        // add the catalog item to the user cart items
                        items.add(catalogItem);
                        totalPrice += (catalogItem.getPrice()
                                - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                                * Double.parseDouble(lineArray[2]);
                        // add the catalog item and its quantity to the cart item quantity hashmap
                        cartItemQuantity.put(catalogItem.getId(), Double.parseDouble(lineArray[2]));
                        // add the user email and its cart items to the cart items hashmap
                        cartItems.put(email, items);
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cart file not found!");
        }
    }

    /**
     * @param email
     * This method is used to save the cart items to the Cart.txt file
     * The cart items are stored in the cart items hashmap
     * The cart item quantity are stored in the cart item quantity hashmap
     * The total price is calculated
     */
    public void saveCart(String email) {
        // read whole file and store it in a list of strings
        List<String> fileContent = new ArrayList<>();
        ArrayList<CatalogItem> itms = cartItems.get(email);
        File file = new File("Cart.txt");
        try (Scanner sc = new Scanner(file);) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // split the line by comma
                String[] lineArray = line.split(",");
                if (lineArray[0].equals(email)) {
                    // get the catalog item by id
                    CatalogItem catalogItem = CatalogItem.getCatalogItem(Integer.parseInt(lineArray[1]));
                    // check if the cart items hashmap contains the user email
                    if (cartItems.containsKey(email)) {
                        // get the user cart items
                        for (CatalogItem itm : itms) {
                            if (itm.getId() == Integer.parseInt(lineArray[1])) {
                                lineArray[2] = String.valueOf(cartItemQuantity.get(catalogItem.getId()));
                                fileContent.add(email + "," + catalogItem.getId() + "," + lineArray[2]);
                                itms.remove(itm);
                                break;
                            }
                        }
                    }
                } else {
                    fileContent.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Cart file not found!");
        }
        for (CatalogItem catalogItem : itms) {
            fileContent.add(email + "," + catalogItem.getId() + "," + cartItemQuantity.get(catalogItem.getId()));
        }

        try {
            FileWriter writer = new FileWriter("Cart.txt");
            for (String line : fileContent) {
                writer.write(line + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        cartItems.clear();
        totalPrice = 0;
        loadCartItems(email);   
    }

    /**
     * @return the cart items total price
     */
    public static double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param email
     * This method is used to display the cart items
     */
    public void displayCartItems(String email) {
        // check if the user has items in the cart
        if (cartItems.containsKey(email)) {
            System.out.println("Cart items: ");
            System.out.println("--------------------------------------------------");
            // this loop iterates through the cart items and displays the cart items
            // if found
            for (CatalogItem catalogItem : cartItems.get(email)) {
                System.out.println("Item name: " + catalogItem.getName());
                System.out.println("Item category: " + catalogItem.getCategory());
                System.out.println("Item description: " + catalogItem.getDescription());
                System.out.println("Item id: " + catalogItem.getId());
                System.out.println("Item brand: " + catalogItem.getBrand());
                System.out.println("Item price: " + catalogItem.getPrice());
                System.out.println("Item discount percentage: " + catalogItem.getDiscountPercentage());
                System.out.println("Item quantity: " + cartItemQuantity.get(catalogItem.getId()));
                System.out.println("--------------------------------------------------");

            }
            System.out.println("Total price: " + totalPrice);
        } else {
            System.out.println("No items in Your cart!");
        }

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
            ArrayList<CatalogItem> items = new ArrayList<CatalogItem>();
            items.add(catalogItem);
            cartItems.put(email, items);
            // add the catalog item and its quantity to the cart item quantity
            cartItemQuantity.put(catalogItem.getId(), quantity);
        }
        // calculate the total price
        totalPrice += (catalogItem.getPrice() - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                * quantity;
        // save the cart
        saveCart(email);
        System.out.println("Item added to cart successfully!");
    }

    /**
     * @param email
     * This method is used to clear the cart
     */
    public static void clearCart(String email) {
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
                ArrayList<String> lines = new ArrayList<String>();
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
    public static HashMap<String, ArrayList<CatalogItem>> getCartItems() {
        return cartItems;
    }

    /**
     * @return the cart item quantity
     */
    public static HashMap<Integer, Double> getCartItemQuantity() {
        return cartItemQuantity;
    }

    /**
     * @param email This is the user email
     * @return true if the cart is empty and false if not
     */
    public boolean isCartEmpty(String email) {
        if (cartItems.containsKey(email)) {
            return false;
        }
        return true;
    }

};
