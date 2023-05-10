package tofee_store;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the catalog item
 * A catalog item has a name, category, description, id, brand, price, discount
 * percentage, sealed items stock and loose items stock
 * The catalog items are loaded from the Catalog.txt file
 * The catalog items are displayed to the user
 * Author: @Candy_Crush Team
 * Version: 1.0
 * Date: 2023/05/4
 * 
 */

public class CatalogItem {
    private int id;
    private String name;
    private String category;
    private String description;
    private String brand;
    private double price;
    private double discountPercentage;
    private int SealeditemsStock;
    private double LooseitemsStock;
    private static CatalogItem[] catalogItems = new CatalogItem[100];

    /**
     *  This is default constructor
     */
    public CatalogItem() {
    }

    /**
     * @param name The name of the catalog item
     * @param category The category of the catalog item
     * @param description The description of the catalog item
     * @param id The id of the catalog item
     * @param brand The brand of the catalog item 
     * @param price  The price of the catalog item
     * @param discountPercentage The discount percentage of the catalog item 
     * This constructor is used to create a new catalog item
     */
    public CatalogItem(String name, String category, String description, int id, String brand, double price,
            double discountPercentage) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.id = id;
        this.brand = brand;
        this.price = price;
        this.discountPercentage = discountPercentage;
    }
    // getters and setters for the CatalogItem class

    /**
     * @return name of the catalog item
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return category of the catalog item
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * @return description of the catalog item
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return id of the catalog item
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return brand of the catalog item
     */
    public String getBrand() {
        return this.brand;
    }

    /**
     * @return price of the catalog item
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * @return discount percentage of the catalog item
     */
    public double getDiscountPercentage() {
        return this.discountPercentage;
    }

    /**
     * @return stock of the sealed catalog item
     */
    public int getSealeditemsStock() {
        return this.SealeditemsStock;
    }

    /**
     * @return stock of the loose catalog item
     */
    public double getLooseitemsStock() {
        return this.LooseitemsStock;
    }

    /**
     * @param SealeditemsStock
     * This method is used to set the stock of the sealed catalog item
     */

    public void setSealeditemsStock(int SealeditemsStock) {
        this.SealeditemsStock = SealeditemsStock;
    }

    /**
     * @param LooseitemsStock
     * This method is used to set the stock of the loose catalog item
     */
    public void setLooseitemsStock(double LooseitemsStock) {
        this.LooseitemsStock = LooseitemsStock;
    }

    // get specific catalog item by id
    /**
     * @param id The id of the catalog item
     * @return catalog item with the given id
     * This method is used to get a specific catalog item by id
     */
    public static CatalogItem getCatalogItem(int id) {
        for (CatalogItem catalogItem : catalogItems) {
            if (catalogItem.getId() == id) {
                return catalogItem;
            }
        }
        return null;
    }

    /**
     * This method is used to load the catalog items from the Catalog.txt file
     * The catalog items are stored in the catalogItems array
     * The catalog items are either sealed or loose
     */
    public static void loadCatalogItems() {
        CatalogItem catalogItem = null;
        try {
            File catalogFile = new File("Catalog.txt");
            Scanner catalogScanner = new Scanner(catalogFile);
            int i = 0;
            while (catalogScanner.hasNextLine()) {
                String catalogItemString = catalogScanner.nextLine();
                String[] catalogItemStringArray = catalogItemString.split(",");
                if (Integer.parseInt(catalogItemStringArray[3]) % 2 == 0) {
                    catalogItem = new Sealeditems(catalogItemStringArray[0], catalogItemStringArray[1],
                            catalogItemStringArray[2], Integer.parseInt(catalogItemStringArray[3]),
                            catalogItemStringArray[5], Double.parseDouble(catalogItemStringArray[6]),
                            Double.parseDouble(catalogItemStringArray[7]), Integer.parseInt(catalogItemStringArray[4]));
                } else {
                    catalogItem = new Looseitems(catalogItemStringArray[0], catalogItemStringArray[1],
                            catalogItemStringArray[2], Integer.parseInt(catalogItemStringArray[3]),
                            catalogItemStringArray[5], Double.parseDouble(catalogItemStringArray[6]),
                            Double.parseDouble(catalogItemStringArray[7]),
                            Double.parseDouble(catalogItemStringArray[4]));

                }
                catalogItems[i] = catalogItem;
                i++;
            }
            catalogScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Catalog file not found!");
        }
    }

    /**
     * This method is used to display the catalog items to the user
     * The catalog items are displayed in the following format:
     * Item name: name
     * Item category: category
     * Item description: description
     * Item id: id
     * Item brand: brand
     * Item price: price
     * Item discount percentage: discountPercentage
     * Item stock: stock
     */
    public static void displayCatalog() {
        System.out.println("Catalog: ");
        System.out.println("--------------------------------------------------");
        int i = 0;
        // this loop iterates through the catalog items and displays the catalog items
        // if found
        while (catalogItems[i] != null) {
            System.out.println("Item name: " + catalogItems[i].name);
            System.out.println("Item category: " + catalogItems[i].category);
            System.out.println("Item description: " + catalogItems[i].description);
            System.out.println("Item id: " + catalogItems[i].id);
            System.out.println("Item brand: " + catalogItems[i].brand);
            System.out.println("Item price: " + catalogItems[i].price);
            System.out.println("Item discount percentage: " + catalogItems[i].discountPercentage);
            if (catalogItems[i].id % 2 == 0) {
                System.out.println("In Stock: " + catalogItems[i].SealeditemsStock + " units");
            } else {
                System.out.println("In Stock: " + catalogItems[i].LooseitemsStock + " kg");
            }
            System.out.println("--------------------------------------------------");
            i++;
        }
    }
}

/**
 * This class is used to create a sealed catalog item
 * A sealed catalog item is a catalog item that is sold in units
 * The stock of a sealed catalog item is in units
 */

class Sealeditems extends CatalogItem {
    public Sealeditems(String name, String category, String description, int id, String brand, double price,
            double discountPercentage, int inStock) {
        super(name, category, description, id, brand, price, discountPercentage);
        this.setSealeditemsStock(inStock);
    }

}

/**
 * This class is used to create a loose catalog item
 * A loose catalog item is a catalog item that is sold in kg
 * The stock of a loose catalog item is in kg
 */
class Looseitems extends CatalogItem {
    public Looseitems(String name, String category, String description, int id, String brand, double price,
            double discountPercentage, double inStock) {
        super(name, category, description, id, brand, price, discountPercentage);
        this.setLooseitemsStock(inStock);
    }

    
}