package Model;

/**
 * This class represents the Created status of the order
 * The order is created when the user places an order
 */
public class created extends OrderStatue {
    @Override
    public void activateStatue() {
        status = "Created";
    }
}
