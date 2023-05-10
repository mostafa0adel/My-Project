package tofee_store;

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

/**
 * This class represents the Delivered status of the order
 */

class Delivered extends OrderStatue {

    @Override
    public void activateStatue() {
        status = "Delivered";
    }
}

/**
 * This class represents the Created status of the order
 * The order is created when the user places an order
 */
class created extends OrderStatue {
    @Override
    public void activateStatue() {
        status = "Created";
    }
}

/**
 * This class represents the Closed status of the order
 * The order is closed when the admin delivers the order
 */
class Closed extends OrderStatue {
    @Override
    public void activateStatue() {
        status = "Closed";
    }
}
