package Model;

/**
 * This class is used to create a sealed catalog item
 * A sealed catalog item is a catalog item that is sold in units
 * The stock of a sealed catalog item is in units
 */

public class Sealeditems extends CatalogItem {
    public Sealeditems(String name, String category, String description, int id, String brand, double price,
                       double discountPercentage, int inStock, int LoyaltyPoints) {
        super(name, category, description, id, brand, price, discountPercentage, LoyaltyPoints);
        this.setSealedStock(inStock);
    }

}
