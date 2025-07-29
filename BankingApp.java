import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/bank_management";
    private static final String username = "root";
    private static final String password = "1234";

    public static void main(String[] args)  {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Scanner scanner=new Scanner(System.in);
            User user=new User(connection,scanner);
            Accounts accounts=new Accounts(connection,scanner);
            AccountManager accountManager=new AccountManager(connection,scanner);
            String email;
            long account_number;
            while(true){
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice=scanner.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User logged in");
                            if(!accounts.accountExist(email)){
                                System.out.println();
                                System.out.println("1. Open a new account");
                                System.out.println("2. exit");
                                int temp=scanner.nextInt();
                                if(temp==1){
                                    account_number =accounts.openAccount(email);
                                    System.out.println("Account opened successfully");
                                    System.out.println("Your account number is: "+account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number=accounts.getAccountNumber(email);
                            int choice2=0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 =scanner.nextInt();
                                switch (choice2){
                                    case 1:
                                        accountManager.debitMoney(account_number);
                                        break;
                                    case 2:
                                        accountManager.creditMoney(account_number);
                                        break;
                                    case 3:
                                        accountManager.transferMoney(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        System.out.println("User logged out");
                                        break;
                                }
                            }
                        }else{
                            System.out.println("Incorrect email");
                        }
                        break;
                    case 3:
                        System.out.println("Thanks for using Bank Management App");
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Enter valid choice");
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}