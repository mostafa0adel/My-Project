package Controller;

import Model.CatalogItem;
import Model.cart;
import View.ToffeeStoreViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CartController {
    public cart UserCart;
    CatalogController catalogController = new CatalogController();
    ToffeeStoreViewer toffeeStoreViewer = new ToffeeStoreViewer();
    public CartController(String email)
    {
        UserCart = new cart(email);
    }
    public CartController()
    {

    }
    public void addToCart(CatalogItem item, double quantity, String email)
    {
        UserCart.addItemToCart(item, quantity, email);
        saveCart(email);
    }
    public void displayCart(String email)
    {
        toffeeStoreViewer.viewCart(UserCart, email);
    }

    public void clearCart(String email)
    {
        UserCart.clearCart(email);
    }
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
                    CatalogItem catalogItem = catalogController.getCatalogItem(Integer.parseInt(lineArray[1]));
                    // check if the cart items hashmap contains the user email
                    ArrayList<CatalogItem> items;
                    if (UserCart.getCartItems().containsKey(email)) {
                        items = UserCart.getCartItems().get(email);
                    } else {
                        // create a new arraylist to store the user cart items
                        items = new ArrayList<>();

                    }
                    items.add(catalogItem);
                    UserCart.setTotalPrice(UserCart.getTotalPrice() + (catalogItem.getPrice()
                            - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                            * Double.parseDouble(lineArray[2]));

                    UserCart.getCartItemQuantity().put(catalogItem.getId(), Double.parseDouble(lineArray[2]));
                    UserCart.getCartItems().put(email, items);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cart file not found!");
        }
    }
    public void saveCart(String email) {
        System.out.println("Saving cart...");
        List<String> fileContent = new ArrayList<>();
        ArrayList<CatalogItem> itms = UserCart.getCartItems().get(email);
        File file = new File("Cart.txt");
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                // split the line by comma
                String[] lineArray = line.split(",");
                if (lineArray[0].equals(email)) {
                    // get the catalog item by id
                    CatalogItem catalogItem = catalogController.getCatalogItem(Integer.parseInt(lineArray[1]));
                    // check if the cart items hashmap contains the user email
                    if (UserCart.getCartItems().containsKey(email)) {
                        // get the user cart items
                        for (CatalogItem itm : itms) {
                            if (itm.getId() == Integer.parseInt(lineArray[1])) {
                                lineArray[2] = String.valueOf(UserCart.getCartItemQuantity().get(catalogItem.getId()));
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
            fileContent.add(email + "," + catalogItem.getId() + "," + UserCart.getCartItemQuantity().get(catalogItem.getId()));
        }
        try {
            FileWriter writer = new FileWriter("Cart.txt");
            for (String line : fileContent) {
                writer.write(line + System.lineSeparator());
            }
            writer.close();
            UserCart.getCartItems().remove(email);
            UserCart.setTotalPrice(0);
            loadCartItems(email);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}
