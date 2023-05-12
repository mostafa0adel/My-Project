package Controller;
import Model.*;
import View.ToffeeStoreViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class OrderController {
    order Order ;
    CatalogController catalogController = new CatalogController();
    private ToffeeStoreViewer toffeStoreViewer = new ToffeeStoreViewer();

    OrderController(String email){
        Order = new order(email);
    }
    public void loadOrderItems(String orderId) {
        try {
            // create a new file
            File file = new File("Order.txt");
            // create a new scanner
            Scanner sc = new Scanner(file);
            // loop through the file lines
            while (sc.hasNextLine()) {
                // get the line
                String line = sc.nextLine();
                // split the line by comma
                String[] lineArray = line.split(",");
                // check if the user email is equal to the email in the file
                if (lineArray[0].equals(Order.getEmail()) && lineArray.length == 4 && lineArray[1].equals(orderId)) {
                    // get the catalog item by id
                    CatalogItem catalogItem = catalogController.getCatalogItem(Integer.parseInt(lineArray[2]));
                    Order.setOrderID(lineArray[1]);
                    ArrayList<CatalogItem> items;
                    // check if the order items hashmap contains the user email
                    if (Order.getOrderItems().containsKey(orderId)) {
                        // get the user order items
                        items = Order.getOrderItems().get(orderId);
                    } else {
                        // create a new arraylist to store the user order items
                        items = new ArrayList<>();
                    }
                    items.add(catalogItem);
                    Order.getOrderItems().put(orderId, items);
                    Order.getOrderItemQuantity().put(catalogItem.getId(), Double.parseDouble(lineArray[3]));
                    // calculate the total price with discount if exists
                    Order.setTotalPrice(Order.getTotalPrice() + (catalogItem.getPrice()
                            - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                            * Double.parseDouble(lineArray[3]));
                }
                if (lineArray.length == 7 && lineArray[0].equals(Order.getEmail()) && lineArray[1].equals(orderId)) {
                    Order.setOrderID(lineArray[1]);
                    Order.setOrderOwnerName(lineArray[2]);
                    Order.setShippingAddress(lineArray[3]);
                    Order.setPhoneNumber(lineArray[4]);
                    Order.setTotalPrice(Double.parseDouble(lineArray[5]));
                    if (lineArray[6].equals("Created")) {
                        Order.setOrderstat(new Delivered());
                        Order.getOrderstat().activateStatue();
                    }

                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Order file not found!");
        }
    }
    public void saveOrder(String email, String orderId) {
        try {
            // create a new file
            File file = new File("Order.txt");
            // create a new file writer
            FileWriter fw = new FileWriter(file, true);
            // loop through the order items hashmap
            ArrayList<CatalogItem> items = Order.getOrderItems().get(orderId);
            // this loop iterates through the cart items and writes the cart items to the
            // file
            for (CatalogItem catalogItem : items) {
                fw.write(email + "," + orderId + "," + catalogItem.getId() + ","
                        + Order.getOrderItemQuantity().get(catalogItem.getId()));
                fw.write("\n");
            }
            // close the print writer
            fw.write(email + "," + orderId + "," + Order.getOrderOwnerName() + "," + Order.getShippingAddress() + ","
                    + Order.getPhoneNumber() + "," + Order.getTotalPrice() + "," + Order.getOrderstat().status);
            fw.write("\n");
            fw.close();

            this.Order.getOrderItems().clear();
            this.Order.getOrderItemQuantity().clear();
            this.loadOrderItems(orderId);
        } catch (IOException e) {
            System.out.println("Order file not found!");
        }
    }
    public void makeOrderFromCatalog(String email, int id, double quantity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name: ");
        this.Order.setOrderOwnerName(sc.nextLine());
        System.out.println("Enter your phone number: ");
        this.Order.setPhoneNumber(sc.nextLine());
        System.out.println("Enter your shipping address: ");
        this.Order.setShippingAddress(sc.nextLine());
        // display the order total and confirm the order
        // calculate the total price and apply the discount if item has discount
       this.Order.setTotalPrice(this.Order.getTotalPrice() + (catalogController.getCatalogItem(id).getPrice()
                - (catalogController.getCatalogItem(id).getPrice()
                        * catalogController.getCatalogItem(id).getDiscountPercentage() / 100))
                * quantity);
        System.out.println("Order total: $" + this.Order.getTotalPrice());
        System.out.println("Are you sure you want to confirm your order? (y/n)");
        String confirm = sc.nextLine();
        if (confirm.equals("y")) {
            // generate order id
            Random rand = new Random();
            int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
            String orderId = Integer.toString(randomNum);
            // get the catalog item by id
            CatalogItem catalogItem = catalogController.getCatalogItem(id);
            // check if the order items hashmap contains the user email
            ArrayList<CatalogItem> items = new ArrayList<>();
            items.add(catalogItem);
            this.Order.getOrderItems().put(orderId, items);
            this.Order.getOrderItemQuantity().put(catalogItem.getId(), quantity);

            // save the order
            this.Order.setPaymentMethod(new Payment(Order.getTotalPrice()));
            if(this.Order.getPaymentMethod().pay(Order.getEmail()))
            {
                Order.setTotalPrice(Order.getPaymentMethod().getTotalamount());
                User.setLoyaltyPoints(User.getLoyaltyPoints()+catalogItem.getLoyaltyPoints());
                UserController.saveUserLoyaltyPointsAndCodes(email);
                Order.setOrderstat(new created());
                Order.getOrderstat().activateStatue();
                System.out.println("Your Order ID is: " + orderId);
                System.out.println("Please Save it for future reference.");
                saveOrder(email, orderId);
            }

        } else {
            System.out.println("Your order has been cancelled.");
        }

    }
    public void placeOrderfromCart(cart userCart,String email){
        // check if the cart is empty
        if (userCart.getCartItems().isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        // get the shipping information
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = sc.nextLine();
        System.out.println("Enter your phone number: ");
        String phone = sc.nextLine();
        System.out.println("Enter your shipping address: ");
        String shippingAddress = sc.nextLine();
        // display the order total and confirm the order
        System.out.println("Order total: $" + userCart.getTotalPrice());
        System.out.println("Are you sure you want to confirm your order? (y/n)");
        String confirm = sc.nextLine();
        if (confirm.equals("y")) {
            Random rand = new Random();
            int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
            String orderId = Integer.toString(randomNum);
             Order = new order(userCart.getCartItems().get(email),
                    userCart.getCartItemQuantity(), name,
                    phone, shippingAddress,Order.getEmail(), orderId);
            Order.setPaymentMethod(new Payment(userCart.getTotalPrice()));
            if(Order.getPaymentMethod().pay(Order.getEmail()))
            {
                Order.setTotalPrice(Order.getPaymentMethod().getTotalamount());
                saveOrder(email, orderId);
                System.out.println("Your order has been confirmed!");
                System.out.println("Your Order ID is: " + orderId);
                ArrayList<CatalogItem> itms= userCart.getCartItems().get(email);
                for (CatalogItem itm : itms) {
                    User.setLoyaltyPoints(User.getLoyaltyPoints() + itm.getLoyaltyPoints());
                }
                UserController.saveUserLoyaltyPointsAndCodes(email);
            }
        } else {
            System.out.println("Your order has been cancelled.");
        }
    }
    public void ViewOrderstatues(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your order ID : ");
        String ID = input.nextLine();
        loadOrderItems(ID);
        toffeStoreViewer.ViewOrderState(Order);
        if (Order.getOrderstat().status.equals("Delivered")) {
            closeOrder(Order.getEmail(), ID);
        }
    }
    public void displayUserorder()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your order ID: ");
        String orderId = sc.nextLine();
        loadOrderItems(orderId);
        toffeStoreViewer.displayOrderItems(Order);
    }
    public void closeOrder(String email, String orderId) {
        // check if the order items hashmap contains the user email
        if (Order.getOrderItems().containsKey(email)) {
            // get the user order items
            ArrayList<CatalogItem> items = Order.getOrderItems().get(email);
            // loop through the user order items
            for (CatalogItem catalogItem : items) {
                // remove the catalog item from the order item quantity hashmap
                HashMap<Integer, Double> orderItemQuantity = Order.getOrderItemQuantity();
                orderItemQuantity.remove(catalogItem.getId());
                Order.setOrderItemQuantity(orderItemQuantity);
            }
            // remove the user email from the order items hashmap
            HashMap<String, ArrayList<CatalogItem>> orderItems = Order.getOrderItems();
            orderItems.remove(email);
            Order.setOrderItems(orderItems);
            File file = new File("Order.txt");
            try {
                Scanner sc = new Scanner(file);
                ArrayList<String> lines = new ArrayList<>();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] lineArray = line.split(",");
                    if (!lineArray[0].equals(email) && !lineArray[1].equals(orderId)) {
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
                Order.setOrderstat(new Closed());
                Order.getOrderstat().activateStatue();
            } catch (IOException e) {
                System.out.println("Orders file not found!");
            }
        } else {
            System.out.println("This customer has no orders!");
        }
    }

}
