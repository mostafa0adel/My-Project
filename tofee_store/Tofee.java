package tofee_store;
import java.util.Scanner;
/**
 * This class controls the flow of the program
 * It displays the menu to the user
 * It takes the user's choice and calls the appropriate method
 * Author: @Candy_Crush Team
 * Version: 1.0
 * Date: 2023/05/4
 */
class Tofee {
    public static void main(String[] args) {
        CatalogItem.loadCatalogItems();
        User user = new User();
        while (true) {
            System.out.println("1-Login");
            System.out.println("2-Register");
            System.out.println("3-View Catalog");
            System.out.println("4-Add Items to Cart");
            System.out.println("5-View My Cart");
            System.out.println("6-Place Order");
            System.out.println("7-View Order Details");
            System.out.println("8-View Order Status");
            System.out.println("9-Logout");
            System.out.println("10-Exit");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    user.login();
                    break;
                case 2:
                    user.register("1234");
                    break;
                case 3:
                    CatalogItem.displayCatalog();
                    break;
                case 4:
                    user.addToCart();
                    break;
                case 5:
                    user.displayCart();
                    break;
                case 6:
                    user.makeanOrder();
                    break;
                case 7:
                    user.displayOrders();
                    break;
                case 8:
                    user.showOrderstatue();
                    break;
                case 9:
                    user.logout();
                    break;
                case 10:
                    System.out.println("Thank you for using our Store");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }

    }
}
