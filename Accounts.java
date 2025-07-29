import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    Connection connection;
    Scanner scanner;

    Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long openAccount(String email) {
        if (!accountExist(email)) {
            String query = "INSERT INTO Accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = scanner.nextLine();
            try {
                security_pin=getEncryptedSecurityPin(security_pin);
                long account_num = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, account_num);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, security_pin);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    return account_num;
                } else {
                    throw new RuntimeException("Account creation failed");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Account already exist");
        }
    }

    public long getAccountNumber(String email) {
        String query = "SELECT * FROM accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("No account exits for this email");
    }

    public long generateAccountNumber() {
        String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long account_num = resultSet.getLong("account_number");
                return account_num + 1;
            } else {
                return 22014300L;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean accountExist(String email) {
        String query = "SELECT * FROM accounts WHERE email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
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
}
