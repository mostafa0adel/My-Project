package Controller;

import Model.CatalogItem;
import Model.Looseitems;
import Model.Sealeditems;
import View.ToffeeStoreViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CatalogController {
   private CatalogItem catalogItm=new CatalogItem();
    private ToffeeStoreViewer toffeStoreViewer = new ToffeeStoreViewer();
    public CatalogController()
    {
        loadCatalogItems();
    }
    public  void loadCatalogItems() {

        CatalogItem[] catalogItems = new CatalogItem[100];
        CatalogItem catalogItem;
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
                            Double.parseDouble(catalogItemStringArray[7]), Integer.parseInt(catalogItemStringArray[4])
                            ,Integer.parseInt(catalogItemStringArray[8]));
                } else {
                    catalogItem = new Looseitems(catalogItemStringArray[0], catalogItemStringArray[1],
                            catalogItemStringArray[2], Integer.parseInt(catalogItemStringArray[3]),
                            catalogItemStringArray[5], Double.parseDouble(catalogItemStringArray[6]),
                            Double.parseDouble(catalogItemStringArray[7]),
                            Double.parseDouble(catalogItemStringArray[4]),
                            Integer.parseInt(catalogItemStringArray[8]));

                }
                catalogItems[i] = catalogItem;
                i++;
            }
            catalogScanner.close();
            catalogItm.setCatalogItems(catalogItems);
        } catch (FileNotFoundException e) {
            System.out.println("Catalog file not found!");
        }
    }
    public  CatalogItem getCatalogItem(int id) {
        for (CatalogItem catalogItem : catalogItm.getCatalogItems()) {
            if (catalogItem.getId() == id) {
                return catalogItem;
            }
        }
        return null;
    }
   public void displayCatalogItems()
    {
        toffeStoreViewer.viewCatalog(catalogItm.getCatalogItems());
    }
}
