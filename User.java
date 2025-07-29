import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    Connection connection;
    Scanner scanner = new Scanner(System.in);

    User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() {
        scanner.nextLine();
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        if (userExist(email)) {
            System.out.println("Some user is already exist for this email");
            return;
        }
        String query = "INSERT INTO user VALUES (?,?,?)";
        try {
            password=getEncryptedSecurityPin(password);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User registered successfully");
            } else {
                System.out.println("Registration failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String login() {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String query = "SELECT * FROM user WHERE email=? AND password=?";
        try {
            password = getEncryptedSecurityPin(password);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("email");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getEncryptedSecurityPin(String securityPin) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < securityPin.length(); i++) {
            char c = securityPin.charAt(i);
            encrypted.append((char) (c + i)); // shift each character by 3
        }
        return encrypted.toString();
    }

    public boolean userExist(String email) {
        String query = "SELECT * FROM user WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

