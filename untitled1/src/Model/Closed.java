package Model;

/**
 * This class represents the Closed status of the order
 * The order is closed when the admin delivers the order
 */
public class Closed extends OrderStatue {
    @Override
    public void activateStatue() {
        status = "Closed";
    }
}
