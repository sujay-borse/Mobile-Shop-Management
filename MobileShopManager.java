import java.sql.*;
import java.util.Scanner;

public class MobileShopManager {
    static final String URL = "jdbc:mysql://localhost:3306/MobileShopDB";
    static final String USER = "root";
    static final String PASS = "8382";  // change this

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int choice;

            do {
                System.out.println("\n=== Mobile Shop Management ===");
                System.out.println("1. Add Mobile");
                System.out.println("2. View All Mobiles");
                System.out.println("3. Search Mobile by Brand");
                System.out.println("4. Update Mobile Stock");
                System.out.println("5. Delete Mobile");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Brand: ");
                        String brand = sc.nextLine();
                        System.out.print("Model: ");
                        String model = sc.nextLine();
                        System.out.print("Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Stock: ");
                        int stock = sc.nextInt();

                        String insertSQL = "INSERT INTO mobiles (brand, model, price, stock) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                            pstmt.setString(1, brand);
                            pstmt.setString(2, model);
                            pstmt.setDouble(3, price);
                            pstmt.setInt(4, stock);
                            pstmt.executeUpdate();
                            System.out.println("Mobile added successfully!");
                        }
                        break;

                    case 2:
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM mobiles");
                        System.out.println("\n--- Mobile List ---");
                        while (rs.next()) {
                            System.out.printf("ID: %d | Brand: %s | Model: %s | Price: %.2f | Stock: %d\n",
                                    rs.getInt("id"),
                                    rs.getString("brand"),
                                    rs.getString("model"),
                                    rs.getDouble("price"),
                                    rs.getInt("stock"));
                        }
                        break;

                    case 3:
                        System.out.print("Enter Brand: ");
                        String searchBrand = sc.nextLine();
                        String searchSQL = "SELECT * FROM mobiles WHERE brand LIKE ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(searchSQL)) {
                            pstmt.setString(1, "%" + searchBrand + "%");
                            ResultSet searchRs = pstmt.executeQuery();
                            while (searchRs.next()) {
                                System.out.printf("ID: %d | Brand: %s | Model: %s | Price: %.2f | Stock: %d\n",
                                        searchRs.getInt("id"),
                                        searchRs.getString("brand"),
                                        searchRs.getString("model"),
                                        searchRs.getDouble("price"),
                                        searchRs.getInt("stock"));
                            }
                        }
                        break;

                    case 4:
                        System.out.print("Enter Mobile ID: ");
                        int updateId = sc.nextInt();
                        System.out.print("New Stock: ");
                        int newStock = sc.nextInt();
                        String updateSQL = "UPDATE mobiles SET stock = ? WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                            pstmt.setInt(1, newStock);
                            pstmt.setInt(2, updateId);
                            int rows = pstmt.executeUpdate();
                            if (rows > 0)
                                System.out.println("Stock updated!");
                            else
                                System.out.println("Mobile not found!");
                        }
                        break;

                    case 5:
                        System.out.print("Enter Mobile ID to Delete: ");
                        int deleteId = sc.nextInt();
                        String deleteSQL = "DELETE FROM mobiles WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                            pstmt.setInt(1, deleteId);
                            int rows = pstmt.executeUpdate();
                            if (rows > 0)
                                System.out.println("Mobile deleted!");
                            else
                                System.out.println("Mobile not found!");
                        }
                        break;

                    case 0:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } while (choice != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}