package Model;

/**
 * This class represents the Delivered status of the order
 */

public class Delivered extends OrderStatue {

    @Override
    public void activateStatue() {
        status = "Delivered";
    }
}
