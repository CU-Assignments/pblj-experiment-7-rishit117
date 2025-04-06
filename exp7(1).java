import java.sql.*;
import java.util.Scanner;

public class ProductManagementApp {
    private static Connection connection;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            // Change the URL, username, and password as per your database configuration
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProductDB", "root", "password");
            scanner = new Scanner(System.in);
            showMenu();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\n--- Product Management System ---");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct() {
        System.out.print("Enter Product Name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try {
            connection.setAutoCommit(false); // Start transaction
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, productName);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);
                pstmt.executeUpdate();
                connection.commit(); // Commit transaction
                System.out.println("Product added successfully.");
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                System.out.println("Error adding product: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewProducts() {
        String query = "SELECT * FROM Product";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n--- Product List ---");
            while (rs.next()) {
                System.out.println("ProductID: " + rs.getInt("ProductID") +
                                   ", ProductName: " + rs.getString("ProductName") +
                                   ", Price: " + rs.getDouble("Price") +
                                   ", Quantity: " + rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.print
