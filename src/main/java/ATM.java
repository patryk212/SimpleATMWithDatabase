
import java.sql.*;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args) throws SQLException {

        TransactionManager transactionManager = new TransactionManager();
        UsersManager usersManager = new UsersManager();

        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            Scanner scanner = new Scanner(System.in);

            System.out.println("Welcome to the ATM");

            System.out.println("Enter your username: ");
            String enterdUsername = scanner.nextLine();

            System.out.println("Enter your password: ");
            String enteredPassword = scanner.nextLine();

            String selectQuery = "SELECT * FROM users where username = ?;";

            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, enterdUsername);
            resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String storedPassword = resultSet.getString("password");
                double balance = resultSet.getDouble("balance");

                if (enteredPassword.equals(storedPassword)) {
                    System.out.println("Login successful.");
                    while (true) {
                        System.out.println("Your balance: " + balance + "$");

                        System.out.println("1. Deposit money.");
                        System.out.println("2. Withdraw money.");
                        System.out.println("3. Show transaction history.");
                        System.out.println("4. Change password.");
                        System.out.println("5. EXIT.");
                        System.out.println("Enter your choice: ");

                        int choice = scanner.nextInt();

                        if (choice == 1) {
                            System.out.println("Enter amount to deposit: " + "$");
                            double depositAmount = scanner.nextDouble();
                            balance += depositAmount;

                            TransactionManager.updateTransactionHistory(connection, userId, "Deposit", depositAmount);
                            System.out.println("Deposit successful. Your new balance: " + balance + "$");

                        } else if (choice == 2) {
                            System.out.println("Enter amount to withdraw: " + "$");
                            double withdrawAmount = scanner.nextDouble();

                            if (withdrawAmount > balance) {
                                System.out.println("Insufficient funds. Cannot withdraw.");
                                TransactionManager.updateTransactionHistory(connection, userId, "Withdraw (Failed)", withdrawAmount);
                            } else {
                                balance -= withdrawAmount;
                                TransactionManager.updateTransactionHistory(connection, userId, "Withdraw", withdrawAmount);
                                System.out.println("Withdraw successful. Your new balance: " + balance + "$");
                            }


                        } else if (choice == 5) {
                            break;
                        } else if (choice == 3) {
                            TransactionManager.showTransactionHistory(connection, userId);
                        } else if (choice == 4) {
                            scanner.nextLine();
                            System.out.println("Enter current password: ");
                            String currentPassword = scanner.nextLine();
                            System.out.println("Enter new password: ");
                            String newPassword = scanner.nextLine();

                            usersManager.changePassword(connection,userId, currentPassword, newPassword);

                        }

                    }

                } else {
                    System.out.println("Incorrect password!");
                }
            } else {
                System.out.println("User not found.");
            }
            resultSet.close();
            selectStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
