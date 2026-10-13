package week03_daos.persistence;

import org.jspecify.annotations.NonNull;
import week03_daos.entities.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDaoImpl implements CustomerDao {
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://127.0.0.1:3306/classicmodels";
    private String username = "root";
    private String password = "";

    public List<Customer> selectCustomersByName(String name) {
        List<Customer> customers = new ArrayList<>();

        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return customers;
        }

        // Prepare statement - Write an SQL statement and compile it into something
        // the database can actually run
        String sql = "SELECT * FROM customers WHERE customerName = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            // Run query - Execute the SQL that has been compiled and get the results
            try (ResultSet rs = ps.executeQuery()) {
                // Process results - loop through each row in resultset until it's empty
                while (rs.next()) {
                    // Extract pieces from the result row - we can explicitly ask for each piece by column name
                    Customer customer = mapRow(rs);
                    customers.add(customer);
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tIssue occurred when processing query or results");
            }
        } catch (SQLException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare statement: " + sql);
        }

        return customers;
    }

    private static @NonNull Customer mapRow(ResultSet rs) throws SQLException {
        Customer customer = new Customer(
                rs.getInt("customerNumber"),
                rs.getString("customerName"),
                rs.getString("contactLastName"),
                rs.getString("contactFirstName"),
                rs.getString("phone"),
                rs.getString("addressLine1"),
                rs.getString("addressLine2"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("postalCode"),
                rs.getString("country"),
                rs.getInt("salesRepEmployeeNumber"),
                rs.getDouble("creditLimit")
        );
        return customer;
    }
    public List<Customer> selectCustomersContainingName(String name) {
        List<Customer> customers = new ArrayList<>();

        Connector connector = new Connector();
        Connection conn = connector.getConnection();

        // Prepare statement - Write an SQL statement and compile it into something
        // the database can actually run
        String sql = "SELECT * FROM customers WHERE customerName LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            // Run query - Execute the SQL that has been compiled and get the results
            try (ResultSet rs = ps.executeQuery()) {
                // Process results - loop through each row in resultset until it's empty
                while (rs.next()) {
                    // Extract pieces from the result row - we can explicitly ask for each piece by column name
                    Customer customer = mapRow(rs);
                    customers.add(customer);
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tIssue occurred when processing query or results");
            }
        } catch (SQLException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare statement: " + sql);
        }
        return customers;
    }

    static void main(String[] args) {
        CustomerDao customerDao = new CustomerDaoImpl();
        List<Customer> customers = customerDao.selectCustomersByName("Atelier Graphique");

        System.out.println("Customers with matching Atelier Graphique:");
        System.out.println(customers);

        customers = customerDao.selectCustomersContainingName("Z");
        System.out.println("Customers with name containing z:");
        System.out.println(customers);
    }

    public Customer findCustomerById(int id) {
        Customer c = null;

        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                // Prepare statement - Write an SQL statement and compile it into something
                // the database can actually run
                String sql = "SELECT * FROM customers WHERE customerNumber = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    // Populate placeholder
                    ps.setInt(1, id);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int customerNumber = rs.getInt("customerNumber");
                        String customerName = rs.getString("customerName");
                        String contactLastName = rs.getString("contactLastName");
                        String contactFirstName = rs.getString("contactFirstName");
                        String phone = rs.getString("phone");
                        String addressLine1 = rs.getString("addressLine1");
                        String addressLine2 = rs.getString("addressLine2");
                        String city = rs.getString("city");
                        String state = rs.getString("state");
                        String postalCode = rs.getString("postalCode");
                        String country = rs.getString("country");
                        Integer salesRepEmployeeNumber = rs.getInt("salesRepEmployeeNumber");
                        Double creditLimit = rs.getDouble("creditLimit");

                        c = new Customer(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit);
                    }

                } catch (SQLException e) {
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tCannot prepare statement: " + sql);
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot establish a connection to " + url);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tNo driver files found - please check dependencies.");
        }
        return c;
    }

    public boolean addCustomer(Customer c) {

        try {
            // Load driver - pull in library of Java code to work with MySQL database
            Class.forName(driver);

            // Connect to database - make a connection to the specified URL with the supplied credentials
            try(Connection conn = DriverManager.getConnection(url, username, password)){
                // Prepare statement - Write an SQL statement and compile it into something
                // the database can actually run
                String sql = "INSERT INTO customers VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try(PreparedStatement ps = conn.prepareStatement(sql)){
                    // Populate placeholder
                    ps.setInt(1, c.customerNumber());
                    ps.setString(2, c.customerName());
                    ps.setString(3, c.contactLastName());
                    ps.setString(4, c.contactFirstName());
                    ps.setString(5, c.phone());
                    ps.setString(6, c.addressLine1());
                    ps.setString(7, c.addressLine2());
                    ps.setString(8, c.city());
                    ps.setString(9, c.state());
                    ps.setString(10, c.postalCode());
                    ps.setString(11, c.country());
                    ps.setInt(12, c.salesRepEmployeeNumber());
                    ps.setDouble(13, c.creditLimit());

                    // Run query - Execute the SQL that has been compiled and get the results
                    int rowsAffected = ps.executeUpdate();
                    if(rowsAffected > 0){
                        return true;
                    }
                }catch(SQLException e){
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tCannot prepare statement: " + sql);
                }
            }catch(SQLException e){
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot establish a connection to " + url);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tNo driver files found - please check dependencies.");
        }
        return false;
    }

}