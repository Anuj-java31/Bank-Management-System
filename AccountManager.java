import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Connection connection;
    Scanner scanner;
    AccountManager(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void creditMoney(long account_number){
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount= scanner.nextDouble();
        scanner.nextLine();
        System.out.println();
        System.out.print("Enter security pin: ");
        String security_pin=scanner.nextLine();
        System.out.println();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                    String query="UPDATE accounts SET balance=balance+? WHERE account_number=?";
                    try{
                        PreparedStatement preparedStatement1=connection.prepareStatement(query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int affectedRows=preparedStatement1.executeUpdate();
                        if(affectedRows>0){
                            System.out.println(amount+" credited to your account successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    System.out.println("Invalid security pin");
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void debitMoney(long account_number){
        scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount= scanner.nextDouble();
        scanner.nextLine();
        System.out.println();
        System.out.print("Enter security pin: ");
        String security_pin=scanner.nextLine();
        System.out.println();
        try{
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet=preparedStatement.executeQuery();

                if(resultSet.next()){
                    String query="UPDATE accounts SET balance=balance-? WHERE account_number=?";
                    try{
                        PreparedStatement preparedStatement1=connection.prepareStatement(query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int affectedRows=preparedStatement1.executeUpdate();
                        if(affectedRows>0){
                            System.out.println(amount+" debited from your account successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    System.out.println("Invalid security pin");
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void transferMoney(long account_number){
        System.out.println();
        System.out.print("Receiver's account number ");
        long receiverAccountNumber=scanner.nextLong();
        System.out.println();
        System.out.print("Enter amount: ");
        int amount=scanner.nextInt();
        scanner.nextLine();
        System.out.println();
        System.out.print("Enter security pin: ");
        String security_pin=scanner.nextLine();
        System.out.println();
        try {
            PreparedStatement checkerPreparedStatement=connection.prepareStatement("SELECT * FROM accounts WHERE account_number=?");
            checkerPreparedStatement.setLong(1,receiverAccountNumber);
            ResultSet checkerResultSet=checkerPreparedStatement.executeQuery();
            if(!checkerResultSet.next()){
                System.out.println("Receiver's account does not exit is this banking system");
                return;
            }
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance=resultSet.getDouble("balance");
                    if(current_balance>=amount) {
                        connection.setAutoCommit(false);
                        String creditQuery = "UPDATE accounts SET balance=balance+? WHERE account_number=?";
                        String debitQuery = "UPDATE accounts SET balance=balance-? WHERE account_number=?";
                        PreparedStatement creditPreparedStatement=connection.prepareStatement(creditQuery);
                        PreparedStatement debitPreparedStatement=connection.prepareStatement(debitQuery);
                        creditPreparedStatement.setInt(1,amount);
                        debitPreparedStatement.setInt(1,amount);
                        creditPreparedStatement.setLong(2,receiverAccountNumber);
                        debitPreparedStatement.setLong(2,account_number);
                        int affectedRows1=debitPreparedStatement.executeUpdate();
                        int affectedRows2=creditPreparedStatement.executeUpdate();
                        if(affectedRows1>0 && affectedRows2>0){
                            System.out.println(amount+" is successfully transferred to "+receiverAccountNumber);
                            connection.commit();
                            connection.setAutoCommit(true);
                        }else{
                            System.out.println("Transferred failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient balance");
                    }
                } else {
                    System.out.println("Invalid security pin");
                }
            }
            connection.setAutoCommit(true);
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public void getBalance(long account_number){
        scanner.nextLine();
        System.out.print("Enter security pin: ");
        String security_pin=scanner.nextLine();
        try {
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number=? AND security_pin=?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Balance: "+resultSet.getDouble("balance"));
                } else {
                    System.out.println("Invalid security pin");
                }
            }
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

}
