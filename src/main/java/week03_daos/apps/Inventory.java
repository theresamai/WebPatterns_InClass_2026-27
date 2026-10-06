package week03_daos.apps;

import week03_daos.persistence.ProductDao;
import week03_daos.persistence.ProductDaoImpl;

public class Inventory {
    static void main(String[] args) {
        String productCode = "Hello002";
        String productName = "Test Product";
        String productLine = "Motorcycles";
        String productScale = "1:10";
        String productVendor = "Toyota";
        String productDescription = "Toy motorcycle";
        int quantityInStock = 100;
        double buyPrice = 1.29;
        double msrp = 5.99;

        ProductDao productDao = new ProductDao();
        boolean added = productDao.addProduct(productCode, productName, productLine, productScale, productVendor,
                productDescription, quantityInStock, buyPrice, msrp);

        if(added){
            System.out.println("Product " + productCode + " added to database");
        }else{
            System.out.println("Product " + productCode + " cannot be added to database");
        }
    }
}
