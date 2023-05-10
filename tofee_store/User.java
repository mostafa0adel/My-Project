package tofee_store;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class represents the User of the store 
 * A user has an email, password, address, isLoggedIn, shopping cart and order
 * The user can login, register, logout, add items to cart, display cart, make an order, display orders, show order status
 * Author: @Candy_Crush Team
 * Version: 1.0
 * Date: 2023/05/4
 */
public class User {
    private String email;
    private String password;
    private String address;
    private boolean isLoggedIn;
    // make shopping cart for logged in user only
    private cart ShoppingCart;
    private order Order;

    // default constructor
    /**
     * This is the default constructor for the User class
     */
    public User() {
        this.isLoggedIn = false;
    }

    /**
     * @param email The email of the user
     * @param password The password of the user
     * @param address The address of the user
     * This constructor is used to create a new user
     */
    public User(String email, String password, String address) {
        this.email = email;
        this.password = password;
        this.address = address;
        this.isLoggedIn = false;
    }

    /**
     * @return the email of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * This method is used to let the user add items to cart
     * The user must be logged in to add items to cart
     * The user must enter the item id and quantity
     * The item is added to the cart
     */
    public void addToCart() {
        Scanner sc = new Scanner(System.in);
        // check if the user is logged in
        if (this.isLoggedIn == false) {
            System.out.println("You must login first!");
            this.login();
        }
        System.out.println("Enter the item id");
        int id = sc.nextInt();
        if (CatalogItem.getCatalogItem(id).getSealeditemsStock() == 0
                && CatalogItem.getCatalogItem(id).getLooseitemsStock() == 0) {
            System.out.println("Sorry, this item is out of stock");
            return;
        }
        System.out.println("Enter the item quantity");
        if(id%2==0){
            int quantity = sc.nextInt();
            while (quantity > CatalogItem.getCatalogItem(id).getSealeditemsStock()) {
                System.out.println("Sorry, this quantity is not available");
                System.out.println("Enter the item quantity");
                quantity = sc.nextInt();
            }
            CatalogItem.getCatalogItem(id).setSealeditemsStock(CatalogItem.getCatalogItem(id).getSealeditemsStock()-quantity);
            this.ShoppingCart.addItemToCart(CatalogItem.getCatalogItem(id),(double) quantity, this.email);
        }
        else{
            double quantity = sc.nextDouble();
            while (quantity > CatalogItem.getCatalogItem(id).getLooseitemsStock()) {
                System.out.println("Sorry, this quantity is not available");
                System.out.println("Enter the item quantity");
                quantity = sc.nextDouble();
            }
            CatalogItem.getCatalogItem(id).setLooseitemsStock(CatalogItem.getCatalogItem(id).getLooseitemsStock()-quantity);
            this.ShoppingCart.addItemToCart(CatalogItem.getCatalogItem(id), quantity, this.email);
        }

    }

    /**
     * @param email The email of the user
     * @return the user with the given email
     * This method is used to get the user with the given email
     * It reads the users information from Users.txt file
     */
    public User getUser(String email) {
        // read the users information from Users.txt file
        try {
            File file = new File("Users.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] user = sc.nextLine().split(",");
                if (user[0].equals(email)) {
                    User retrievedUser = new User(user[0], user[1], user[2]);
                    return retrievedUser;
                }
            }
            sc.close();
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        }
    }

    /**
     * This method is used to save the user information in Users.txt file
     */
    public void saveUser() {
        try {
            FileWriter fw = new FileWriter("Users.txt", true);
            fw.write(this.email + "," + this.password + "," + this.address + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }

    /**
     * @param email
     * This method is used to send an otp to the user email
     */
    public void sendOtp(String email) {

    }

    /**
     * @param otp
     * This method is used to let the user register
     * The user must enter his email, password and address
     * The user must enter the otp sent to his email
     * The user is registered
     */
    public void register(String otp) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email address: ");
        String email = sc.nextLine();
        // Check email address validity
        while (!email.matches("^[a-zA-Z0-9]+[_.-]?[a-zA-Z0-9]+@[a-zA-Z0-9]+[-]?[a-zA-Z0-9]+\\.[a-zA-Z0-9]{2,}+$")) {
            System.out.println("Invalid email address, try again!");
            System.out.println("Enter your email address: ");
            email = sc.nextLine();
        }
        // Check if the user already exists
        while (getUser(email) != null) {
            System.out.println("User already exists, try again!");
            System.out.println("Enter your email address: ");
            email = sc.nextLine();
        }
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        // check password strength
        while (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            System.out.println(
                    "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character!");
            System.out.println("Enter your password: ");
            password = sc.nextLine();
        }
        System.out.println("Enter your address: ");
        String address = sc.nextLine();
        System.out.println("Enter your OTP: ");
        String enteredOtp = sc.nextLine();
        if (enteredOtp.equals(otp)) {
            User user = new User(email, password, address);
            user.saveUser();
            System.out.println("You have successfully registered!");
        } else {
            System.out.println("You have entered the wrong OTP!");
        }
    }

    /**
     * This method is used to display the cart items
     * The user must be logged in first
     */
    public void displayCart() {
        // check if the user is logged in
        if (this.isLoggedIn == false) {
            System.out.println("You must login first!");
            this.login();
        }
        // display the cart items
        this.ShoppingCart.displayCartItems(this.email);

    }
   
    /**
     * @param email The email of the user
     * @param password The password of the user
     * @return true if the user email and password are correct, false otherwise
     * This method is used to check if the user email and password are correct
     */
    public boolean checkUserPassword(String email, String password) {

        User retrievedUser = getUser(email);
        if (retrievedUser != null) {
            while (!retrievedUser.password.equals(password)) {
                System.out.println("Wrong password, try again!");
                System.out.println("Enter your password: ");
                password = new Scanner(System.in).nextLine();
            }
            return true;
        }
        return false;
    }

    /**
     * This method is used to login the user
     * The user must enter his email and password
     * The user is logged in
     */
    public void login() {
        // validate password and set isLoggedIn to true
        // return true if successful, false otherwise
        if (this.isLoggedIn == true) {
            System.out.println("You are already logged in!");
            System.out.println("If you want to login with another account, please enter 1 else enter 0");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            if (choice == 1) {
                this.isLoggedIn = false;
            } else {
                return;
            }
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your email address: ");
        String email = sc.nextLine();
        while (getUser(email) == null) {
            System.out.println("User not exists, try again!");
            System.out.println("Enter your email address: ");
            email = sc.nextLine();
        }
        System.out.println("Enter your password: ");
        String password = sc.nextLine();
        if (checkUserPassword(email, password)) {
            this.isLoggedIn = true;
            System.out.println("Login successful");
            this.email = email;
            this.password = password;
            ShoppingCart = new cart(this.email);
           Order = new order(this.email);
        }
        return;
    }

    /**
     * This method is used to let the user place an order
     * The user must be logged in first
     * The user must enter the item id and quantity
     * The user can confirm the order or cancel it
     * The user can buy the items in his cart
     */
    public void makeanOrder() {
        // check if the user is logged in
        if (!this.isLoggedIn) {
            System.out.println("You must login first!");
            this.login();
        }
        System.out.println("If you want to buy  items in your cart, please enter 1 else enter 0");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 1) {
           this.confirmOrder();
            return;
        }
        System.out.println("Enter the item id");
        int id = sc.nextInt();
        if (CatalogItem.getCatalogItem(id).getSealeditemsStock() == 0
                && CatalogItem.getCatalogItem(id).getLooseitemsStock() == 0) {
            System.out.println("Sorry, this item is out of stock");
            return;
        }
        System.out.println("Enter the item quantity");
        if (id % 2 == 0) {
            int quantity = sc.nextInt();
            while (quantity > CatalogItem.getCatalogItem(id).getSealeditemsStock()) {
                System.out.println("Sorry, this quantity is not available");
                System.out.println("Enter the item quantity");
                quantity = sc.nextInt();
            }
            CatalogItem.getCatalogItem(id)
                    .setSealeditemsStock(CatalogItem.getCatalogItem(id).getSealeditemsStock() - quantity);
            this.Order.makeOrderFromCatalog(email, id, quantity);
        } else {
            double quantity = sc.nextDouble();
            while (quantity > CatalogItem.getCatalogItem(id).getLooseitemsStock()) {
                System.out.println("Sorry, this quantity is not available");
                System.out.println("Enter the item quantity");
                quantity = sc.nextDouble();
            }
            CatalogItem.getCatalogItem(id)
                    .setLooseitemsStock(CatalogItem.getCatalogItem(id).getLooseitemsStock() - quantity);
            this.Order.makeOrderFromCatalog(email, id, quantity);
        }
    }
    
    /**
     * This method is used to display the user orders
     * The user must be logged in first
     * The user can view his orders
     */
    public void displayOrders() {
        // check if the user is logged in
        if (this.isLoggedIn == false) {
            System.out.println("You must login first!");
            this.login();
        }
        // display the orders
        this.Order.displayUserOrder();
    }

    /**
     * This method is used to display the order status
     * The user must be logged in first
     * The user can view the order status
     */
    public void showOrderstatue() {
        if (this.isLoggedIn == false) {
            System.out.println("You must login first!");
            this.login();
        }
        this.Order.ViewOrderStatues();

    }

    /**
     * This method is used to logout the user
     */
    public void logout() {
        // set isLoggedIn to false
        if (this.isLoggedIn == false) {
            System.out.println("You are already logged out!");
            return;
        }
        this.isLoggedIn = false;
    }
    
    /**
     * This method is used to confirm the order
     * The user must be logged in first
     * The user can confirm the order
     * The user can cancel the order
     * The user can buy the items in his cart
     * The user can view the order total price
     */
    public void confirmOrder() {
        if (this.isLoggedIn == false) {
            System.out.println("You must login first!");
            this.login();
        }
        // check if the cart is empty
        if (this.ShoppingCart.isCartEmpty(this.email)) {
            System.out.println("Your cart is empty!");
            return;
        }
        // display the cart items
        this.displayCart();
        // get the shipping information
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = sc.nextLine();
        System.out.println("Enter your phone number: ");
        String phone = sc.nextLine();
        System.out.println("Enter your shipping address: ");
        String shippingAddress = sc.nextLine();
        // display the order total and confirm the order
        System.out.println("Order total: $" + cart.getTotalPrice());
        System.out.println("Are you sure you want to confirm your order? (y/n)");
        String confirm = sc.nextLine();
        if (confirm.equals("y")) {
            Random rand = new Random();
            int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
            String orderId = Integer.toString(randomNum);     
            this.Order = new order(cart.getCartItems(), cart.getCartItemQuantity(), name,
                    phone, shippingAddress, this.email, orderId);
            this.Order.saveOrder(this.email, orderId);
            System.out.println("Your order has been confirmed!");
            System.out.println("Your Order ID is: " + orderId);
            System.out.println("Please Save it for future reference.");
        } else {
            System.out.println("Your order has been cancelled.");
        }
    }

}
