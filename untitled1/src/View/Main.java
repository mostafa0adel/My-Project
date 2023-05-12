package View;
import Controller.UserController;

public class Main {
    public static void main(String[] args) {
    UserController userController = new UserController();
    userController.viewCatalog();
    userController.login();
    userController.addToCart();
    userController.displayCart();
    userController.makeanOrder();
    userController.displayOrders();
    userController.makeanOrder();
    userController.displayOrders();
    userController.ShowOrderstate();
    userController.ShowOrderstate();
    userController.ShowOrderstate();
    userController.logout();
    }
}