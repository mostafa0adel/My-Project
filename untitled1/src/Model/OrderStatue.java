package Model;

/**
 * This class represents the status of the order
 * A status can be created, delivered or closed
 * The order status is changed by the admin
 * The user can view the status of his order
 * The order status is closed by the admin when the order is delivered
 */
public abstract class OrderStatue {
    /**
     * The status of the order
     */
    public String status;

    /**
     * This method is used to change the status of the order
     */
    public abstract void activateStatue();
}

