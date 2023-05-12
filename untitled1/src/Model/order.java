package Model;


import Controller.UserController;

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

    private  HashMap<String, ArrayList<CatalogItem>> orderItems;
    private  HashMap<Integer, Double> orderItemQuantity;
    private double totalPrice;
    private OrderStatue orderstat;
    private  String email;
    private String phoneNumber;
    private String shippingAddress;
    private Payment paymentMethod;
    private String orderOwnerName;
    private String orderID;
    public HashMap<String, ArrayList<CatalogItem>> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(HashMap<String, ArrayList<CatalogItem>> orderItems) {
        this.orderItems = orderItems;
    }

    public HashMap<Integer, Double> getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(HashMap<Integer, Double> orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatue getOrderstat() {
        return orderstat;
    }

    public void setOrderstat(OrderStatue orderstat) {
        this.orderstat = orderstat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Payment getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderOwnerName() {
        return orderOwnerName;
    }

    public void setOrderOwnerName(String orderOwnerName) {
        this.orderOwnerName = orderOwnerName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }


    /**
     * @param email This is the email of the user
     */
    public order(String email) {
        orderItems = new HashMap<>();
        orderItemQuantity = new HashMap<>();
        totalPrice = 0;
        this.email = email;
    }

    /**
     * @param items This is the items of the order
     * @param itemsQuantity This is the quantity of the items
     * @param name This is the name of the user
     * @param phone This is the phone number of the user
     * @param shippingaddress-address This is the shipping address of the user
     * @param email This is the email of the user
     * @param orderID This is the order ID
     * This is the constructor of the order class
     */
    public order(ArrayList<CatalogItem> items,
                 HashMap<Integer, Double> itemsQuantity, String name, String phone, String shippingaddress, String email,
                 String orderID) {
        this.email = email;
        orderItems = new HashMap<>();
        orderItems.put(orderID, items);
        this.orderItemQuantity = itemsQuantity;
        this.phoneNumber = phone;
        this.shippingAddress = shippingaddress;
        this.orderOwnerName = name;
        this.orderstat = new created();
        orderstat.activateStatue();
        this.totalPrice = 0;
        this.orderID = orderID;
        ArrayList<CatalogItem> OrderItem = orderItems.get(orderID);
        for (CatalogItem item : OrderItem) {
            totalPrice += (item.getPrice() - (item.getPrice() * item.getDiscountPercentage() / 100))
                    * orderItemQuantity.get(item.getId());
        }
    }






}


