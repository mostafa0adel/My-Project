package Model;

/**
 * This class is used to create a loose catalog item
 * A loose catalog item is a catalog item that is sold in kg
 * The stock of a loose catalog item is in kg
 */
public class Looseitems extends CatalogItem {
    public Looseitems(String name, String category, String description, int id, String brand, double price,
                      double discountPercentage, double inStock, int LoyaltyPoints) {
        super(name, category, description, id, brand, price, discountPercentage, LoyaltyPoints);
        this.setLooseStock(inStock);
    }


}
