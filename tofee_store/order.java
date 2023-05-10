package tofee_store;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class responsible for the order process
 * Author: @Candy_Crush Team
 * Version: 1.0
 * Date: 2023/05/4
 */
public class order {
    private HashMap<String, ArrayList<CatalogItem>> orderItems;
    private HashMap<Integer, Double> orderItemQuantity;
    private double totalPrice;
    private OrderStatue orderstat;
    private String email;
    private String phoneNumber;
    private String shippingAddress;
    private String paymentMethod = "Cash";
    private String orderOwnerName;
    private String orderID;

    /**
     * @param email This is the email of the user
     */
    public order(String email) {
        orderItems = new HashMap<String, ArrayList<CatalogItem>>();
        orderItemQuantity = new HashMap<Integer, Double>();
        totalPrice = 0;
        this.email = email;
    }

    /**
     * @param items This is the items of the order
     * @param itemsQuantity This is the quantity of the items
     * @param name This is the name of the user
     * @param phone This is the phone number of the user
     * @param shippingaddress This is the shipping address of the user
     * @param email This is the email of the user
     * @param orderID This is the order ID
     * This is the constructor of the order class
     */
    public order(HashMap<String, ArrayList<CatalogItem>> items,
            HashMap<Integer, Double> itemsQuantity, String name, String phone, String shippingaddress, String email,
            String orderID) {
        this.email = email;
        this.orderItems = items;
        this.orderItemQuantity = itemsQuantity;
        this.phoneNumber = phone;
        this.shippingAddress = shippingaddress;
        this.orderOwnerName = name;
        this.orderstat = new created();
        orderstat.activateStatue();
        this.totalPrice = 0;
        this.orderID = orderID;
        ArrayList<CatalogItem> OrderItem = orderItems.get(email);
        for (CatalogItem item : OrderItem) {
            totalPrice += (item.getPrice() - (item.getPrice() * item.getDiscountPercentage() / 100))
                    * orderItemQuantity.get(item.getId());
        }
    }

    /**
     This method is used to get the order status
     */
    public void ViewOrderStatues() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter your order ID : ");
        String orderid = input.nextLine();
        loadOrderItems(orderid);
        if (this.orderstat == null) {
            System.out.println("Your order with ID : " + orderid + " is not found");
            return;
        } else {
            System.out.println("Your order with ID : " + orderid + " is " + orderstat.status);
            if (orderstat.status == "Delivered") {
                closeOrder(this.email, orderid);
            }
        }

    }

    /**
     * @param orderId
    This method is used to load the order items from the file
     */
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
                if (lineArray[0].equals(email) && lineArray.length == 4 && lineArray[1].equals(orderId)) {
                    // get the catalog item by id
                    CatalogItem catalogItem = CatalogItem.getCatalogItem(Integer.parseInt(lineArray[2]));
                    orderID = lineArray[1];
                    // check if the order items hashmap contains the user email
                    if (orderItems.containsKey(orderId)) {
                        // get the user order items
                        ArrayList<CatalogItem> items = orderItems.get(orderId);
                        // add the catalog item to the user order items
                        items.add(catalogItem);
                        // add the catalog item and its quantity to the order item quantity hashmap
                        orderItemQuantity.put(catalogItem.getId(), Double.parseDouble(lineArray[3]));
                        // add the user email and its order items to the order items hashmap
                        orderItems.put(orderId, items);
                    } else {
                        // create a new arraylist to store the user order items
                        ArrayList<CatalogItem> items = new ArrayList<CatalogItem>();
                        // add the catalog item to the user order items
                        items.add(catalogItem);
                        // add the catalog item and its quantity to the order item quantity hashmap
                        orderItemQuantity.put(catalogItem.getId(), Double.parseDouble(lineArray[3]));
                        // add the user email and its order items to the order items hashmap
                        orderItems.put(orderId, items);
                    }
                    // calculate the total price with discount if exists
                    totalPrice += (catalogItem.getPrice()
                            - (catalogItem.getPrice() * catalogItem.getDiscountPercentage() / 100))
                            * Double.parseDouble(lineArray[3]);

                }
                if (lineArray.length == 7 && lineArray[0].equals(email) && lineArray[1].equals(orderID)) {
                    this.orderID = lineArray[1];
                    this.orderOwnerName = lineArray[2];
                    this.shippingAddress = lineArray[3];
                    this.phoneNumber = lineArray[4];
                    this.totalPrice = Double.parseDouble(lineArray[5]);
                    if (lineArray[6].equals("created")) {
                        orderstat = new Delivered();
                        orderstat.activateStatue();
                    }
                    continue;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Order file not found!");
        }
    }

    /**
     This method is used to display the user order details
     */
    public void displayUserOrder() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your order ID: ");
        String orderId = sc.nextLine();
        loadOrderItems(orderId);
        // check if the user has items in the order
        if (orderItems.containsKey(orderId)) {
            // get the user order items
            ArrayList<CatalogItem> items = orderItems.get(orderId);
            // loop through the user order items
            System.out.println("--------------------------------------------------");
            System.out.println("Order ID: " + orderId);
            System.out.println("Order owner name: " + orderOwnerName);
            System.out.println("shipping address: " + shippingAddress);
            System.out.println("payment method: " + paymentMethod);
            System.out.println("--------------------------------------------------");
            for (CatalogItem catalogItem : items) {
                System.out.println("Item name: " + catalogItem.getName());
                System.out.println("Item price: " + catalogItem.getPrice());
                System.out.println("Item quantity: " + orderItemQuantity.get(catalogItem.getId()));
                System.out.println("--------------------------------------------------");
            }
            System.out.println("Total price: " + totalPrice);
        } else {
            System.out.println("You havn't ordered any items yet!");
        }
    }

    /**
     * @param email This is the user email
     * @param id   This is the catalog item id
     * @param quantity This is the catalog item quantity
     * This method is used to make an order from the catalog
     */
    public void makeOrderFromCatalog(String email, int id, double quantity) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name: ");
        this.orderOwnerName = sc.nextLine();
        System.out.println("Enter your phone number: ");
        this.phoneNumber = sc.nextLine();
        System.out.println("Enter your shipping address: ");
        this.shippingAddress = sc.nextLine();
        // display the order total and confirm the order
        // calculate the total price and apply the discount if item has discount
        totalPrice += (CatalogItem.getCatalogItem(id).getPrice()
                - (CatalogItem.getCatalogItem(id).getPrice() * CatalogItem.getCatalogItem(id).getDiscountPercentage()
                        / 100))
                * quantity;
        System.out.println("Order total: $" + totalPrice);
        System.out.println("Are you sure you want to confirm your order? (y/n)");
        String confirm = sc.nextLine();
        if (confirm.equals("y")) {
            // generate order id
            Random rand = new Random();
            int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
            String orderId = Integer.toString(randomNum);
            // get the catalog item by id
            CatalogItem catalogItem = CatalogItem.getCatalogItem(id);
            // check if the order items hashmap contains the user email
            if (orderItems.containsKey(orderId)) {
                // get the user order items
                ArrayList<CatalogItem> orderItemsList = orderItems.get(orderId);
                // check if the user order items contains the catalog item
                if (orderItemsList.contains(catalogItem)) {
                    // get the catalog item quantity
                    double itemQuantity = orderItemQuantity.get(catalogItem.getId());
                    // update the catalog item quantity
                    orderItemQuantity.put(catalogItem.getId(), itemQuantity + quantity);
                } else {
                    // add the catalog item to the user order items
                    orderItemsList.add(catalogItem);
                    // add the catalog item and its quantity to the order item quantity hashmap
                    orderItemQuantity.put(catalogItem.getId(), quantity);
                    // add the user email and its order items to the order items hashmap
                    orderItems.put(orderId, orderItemsList);
                }
            } else {
                // create a new arraylist to store the user order items
                ArrayList<CatalogItem> orderItemsList = new ArrayList<CatalogItem>();
                // add the catalog item to the user order items
                orderItemsList.add(catalogItem);
                // add the catalog item and its quantity to the order item quantity hashmap
                orderItemQuantity.put(catalogItem.getId(), quantity);
                // add the user email and its order items to the order items hashmap
                orderItems.put(orderId, orderItemsList);
            }
            // save the order
            saveOrder(email, orderId);
            System.out.println("Your Order has been confirmed!");
            orderstat = new created();
            orderstat.activateStatue();
            System.out.println("Your Order ID is: " + orderId);
            System.out.println("Please Save it for future reference.");

        } else {
            System.out.println("Your order has been cancelled.");
        }

    }

    /**
     * @param email This is the user email
     * @param orderId This is the order id
     * This method is used to save the order to the file
     */
    public void saveOrder(String email, String orderId) {
        try {
            // create a new file
            File file = new File("Order.txt");
            // create a new file writer
            FileWriter fw = new FileWriter(file, true);
            // loop through the order items hashmap
            ArrayList<CatalogItem> items = orderItems.get(orderId);
            // this loop iterates through the cart items and writes the cart items to the
            // file
            for (CatalogItem catalogItem : items) {
                fw.write(email + "," + orderId + "," + catalogItem.getId() + ","
                        + orderItemQuantity.get(catalogItem.getId()));
                fw.write("\n");
            }
            // close the print writer
            fw.write(email + "," + orderId + "," + orderOwnerName + "," + shippingAddress + "," + phoneNumber + ","
                    + totalPrice + "," + orderstat.status);
            fw.write("\n");
            fw.close();
            cart.clearCart(email);
            this.orderItems.clear();
            this.orderItemQuantity.clear();
            this.loadOrderItems(orderId);
        } catch (IOException e) {
            System.out.println("Order file not found!");
        }
    }

    /**
     * @param email This is the user email
     * @param orderId This is the order id
     * This method is used to close the order and remove it from the file
     */
    public void closeOrder(String email, String orderId) {
        // check if the order items hashmap contains the user email
        if (orderItems.containsKey(orderId)) {
            // get the user order items
            ArrayList<CatalogItem> items = orderItems.get(orderId);
            // loop through the user order items
            for (CatalogItem catalogItem : items) {
                // remove the catalog item from the order item quantity hashmap
                orderItemQuantity.remove(catalogItem.getId());
            }
            // remove the user email from the order items hashmap
            orderItems.remove(orderId);
            File file = new File("Order.txt");
            try {
                Scanner sc = new Scanner(file);
                ArrayList<String> lines = new ArrayList<String>();
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
                orderstat = new Closed();
                orderstat.activateStatue();
            } catch (IOException e) {
                System.out.println("Orders file not found!");
            }
            orderstat = new Delivered();
            orderstat.activateStatue();
        } else {
            System.out.println("This customer has no orders!");
        }
    }
};

