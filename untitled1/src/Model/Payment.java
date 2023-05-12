package Model;

import Controller.UserController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

public class Payment {
    private final String paymentMethod = "Upon Delivery";
    double totalamount;
    private final HashMap<String, Double> coupon = new HashMap<>();

    public Payment(double totalamount) {
        this.totalamount = totalamount;
        loadVoucher();
    }
    public boolean redeemCoupon(String code)
    {
        if (coupon.containsKey(code))
        {
            if(coupon.get(code) >totalamount)
            {
                double reminder = coupon.get(code) - totalamount;
                totalamount = 0;
                coupon.put(code, reminder);
                System.out.println("You have redeemed " + totalamount + " points");
            }
            else{
                totalamount -= coupon.get(code);
                System.out.println("You have redeemed " + coupon.get(code) + " points");
                coupon.remove(code);
                UserController.updateVoucherCodes(code);
            }
            saveVoucher();
            return true;
        }
        else{
            return false;
        }
    }
    public double getTotalamount() {
        return totalamount;
    }
    public boolean redeemPoints(int points)
    {
        if (points > 0 && points <= User.getLoyaltyPoints())
        {
            if(points > totalamount)
            {
                System.out.println("You can't redeem more than the total amount");
                System.out.println("So, you can redeem " + totalamount + " points");
                System.out.println("Your Reminder amount is " + totalamount);
                points -= (int) totalamount;
                totalamount = 0;
            }
            else{
                totalamount -= points;
                System.out.println("You have redeemed " + points + " points");
                System.out.println("Your Reminder amount is " + totalamount);
            }
            User.setLoyaltyPoints(User.getLoyaltyPoints() - points);
            return true;
        }
        else{
            return false;
        }

    }
    public boolean pay(String email)
    {
        boolean paid = false;
        while (!paid){
            System.out.println("1- Redeem Points");
            System.out.println("2- Redeem Coupon");
            System.out.println("3- Pay Upon Delivery");
            int choice = new Scanner(System.in).nextInt();
            if(choice == 1)
            {
                System.out.println("Enter the number of points you want to redeem");
                int points = new Scanner(System.in).nextInt();
                if(redeemPoints(points))
                {
                    System.out.println("You have redeemed " + points + " points");
                    System.out.println("Your Reminder amount is " + totalamount);
                    System.out.println("Your order has been placed successfully");
                    System.out.println("Thank you for using our Store");
                    UserController.saveUserLoyaltyPointsAndCodes(email);
                    paid = true;
                }
                else{
                    System.out.println("You don't have enough points");
                }
            }
            else if(choice == 2)
            {
                System.out.println("Enter the coupon code");
                String code = new Scanner(System.in).nextLine();
                if(redeemCoupon(code))
                {
                    System.out.println("Your Reminder amount is " + totalamount);
                    System.out.println("Your order has been placed successfully");
                    System.out.println("Thank you for using our Store");
                    UserController.saveUserLoyaltyPointsAndCodes(email);
                    paid = true;
                }
                else{
                    System.out.println("Invalid Coupon Code");
                }
            }
            else if(choice == 3)
            {
                System.out.println("Your order has been placed successfully");
                System.out.println("Thank you for using our Store");
                UserController.saveUserLoyaltyPointsAndCodes(email);
                paid = true;
            }

        }
        return true;}
    public void loadVoucher()
    {
        File file = new File("VoucherCodes.txt");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] arr = line.split(",");
                coupon.put(arr[0], Double.parseDouble(arr[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void saveVoucher()
    {
        File file = new File("VoucherCodes.txt");
        try{
            FileWriter fw = new FileWriter(file);
            for (String key : coupon.keySet())
            {
                fw.write(key + "," + coupon.get(key) + "\n");
            }
            fw.close();
            coupon.clear();
            loadVoucher();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
