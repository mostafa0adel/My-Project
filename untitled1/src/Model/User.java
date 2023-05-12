package Model;
import Controller.UserController;

import java.io.*;
import java.util.*;

/**
 * This class represents the User of the store
 * A user has an email, password, address, isLoggedIn, shopping cart and order
 * The user can log in, register, logout, add items to cart, display cart, make an order, display orders, show order status
 * Author: @Candy_Crush Team
 * Version: 1.0
 * Date: 2023/05/4
 */
public class User{
    private String email;
    private String password;
    private String address;
    private boolean isLoggedIn;
    // make shopping cart for logged-in user only
    private cart ShoppingCart;
    private order Order;
    private static int loyaltyPoints;
    private static  ArrayList<String> VoucherCodes = new ArrayList<>();


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

    public static void setVoucherCodes(ArrayList<String> strings) {
        VoucherCodes = strings;
    }

    public static ArrayList<String> getVoucherCodes() {
        return VoucherCodes;
    }




    /**
     * @param email The email of the user
     * @return the user with the given email
     * This method is used to get the user with the given email
     * It reads the user's information from Users.txt file
     */
    public User getUser(String email) {
        // read the users information from Users.txt file
        try {
            File file = new File("Users.txt");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] user = sc.nextLine().split(",");
                if (user[0].equals(email)) {
                    return new User(user[0], user[1], user[2]);
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
        // generate random otp
        Random rand = new Random();
        int otp = rand.nextInt(1000000);
        // send otp to the user email

    }

    /**
     * @param email The email of the user
     * This method is used to let the user register
     * The user must enter his email, password and address
     * The user must enter the otp sent to his email
     * The user is registered
     */
    public void register(String email, String password, String address) {
        this.email = email;
        this.password = password;
        this.address = address;
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
     * This method is used to log in the user * must enter his email and password
     * The user is logged in
     */
    public void login(String userEmailAddress, String userPassword) {
        this.isLoggedIn = true;
        System.out.println("Login successful");
        this.email = userEmailAddress;
        this.password = userPassword;
        ShoppingCart = new cart(this.email);
        Order = new order(this.email);
        UserController.loadUserVoucherCode(this.email);
    }



    /**
     * This method is used to log out the user
     */
    public void logout() {
        // set isLoggedIn to false
        if (!this.isLoggedIn) {
            System.out.println("You are already logged out!");
            return;
        }
        else{
            this.isLoggedIn = false;
            System.out.println("You are logged out!");
        }
    }

    /**
     * This method is used to confirm the order
     * The user must be logged in first
     * The user can confirm the order
     * The user can cancel the order
     * The user can buy the items in his cart
     * The user can view the order total price
     */

    public static int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    public static void setLoyaltyPoints(int lp) {
        loyaltyPoints = lp;
    }

    public String getEmail() {
        return email;
    }
}


