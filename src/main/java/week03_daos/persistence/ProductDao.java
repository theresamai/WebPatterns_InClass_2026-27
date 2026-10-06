package week03_daos.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDao {
    public static boolean addProduct(String productCode, String productName, String productLine, String productScale, String productVendor, String productDescription, int quantityInStock, double buyPrice, double MSRP) {
        // Declare database constants
        String driver = "com.mysql.cj.jdbc.Driver";
        String dbUrl = "jdbc:mysql://127.0.0.1:3306/classicmodels";
        String username = "root";
        String password = "";

        try {
            // 1) Add driver files
            Class.forName(driver);

            // 2) Create connection to database
            try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
                // 3) Write SQL to be used
                String sql = "INSERT INTO products VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // 4) Prepare SQL for execution - compile it into something that can be run
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    // Populate placeholders with real data
                    ps.setString(1, productCode);
                    ps.setString(2, productName);
                    ps.setString(3, productLine);
                    ps.setString(4, productScale);
                    ps.setString(5, productVendor);
                    ps.setString(6, productDescription);
                    ps.setInt(7, quantityInStock);
                    ps.setDouble(8, buyPrice);
                    ps.setDouble(9, MSRP);

                    // 5) Execute insert (a form of update) and see how many rows are impacted
                    int rowsAffected = ps.executeUpdate();

                    //Check if add was successful
                    if (rowsAffected > 0) {
                        return true;
                    }

                    System.out.println("Number of rows added = " + rowsAffected);
                } catch (SQLException e) {
                    System.out.println("Could not prepare SQL: \"" + sql + "\"");
                    System.out.println("Exception reads: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.out.println("Could not establish a connection to " + dbUrl +
                        " using " + username + "as username");
                System.out.println("Exception reads: " + e.getMessage());
            }

        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: Driver cannot be found");
            System.out.println("Exception reads: " + e.getMessage());
            System.out.println("System terminating...");
        }
        return false;
    }
}