import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionManager {

    Connection connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");

    public TransactionManager() throws SQLException {
    }

    public static void updateTransactionHistory(Connection connection, int userId, String action, double amount) throws
        SQLException {
            String selectQuery = "SELECT transaction_history FROM users WHERE id = ?";
            String updateQuery = "UPDATE users SET balance = ?, transaction_history = ? WHERE id = ?";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String transactionInfo = "Action: " + action + ", Amount: " + amount + ", Date: " + dateFormat.format(new Date());

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            String currentHistory = "";
            if (resultSet.next()) {
                currentHistory = resultSet.getString("transaction_history");
            }

            String newTransaction = "Action: " + action + ", Amount: " + amount + ", Date: " + dateFormat.format(new Date());
            String newHistory = currentHistory + "\n" + transactionInfo;

            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, amount);
            updateStatement.setString(2, newHistory);
            updateStatement.setInt(3, userId);
            updateStatement.executeUpdate();

            updateStatement.close();
            selectStatement.close();
        }
        public static void showTransactionHistory(Connection connection, int userId) throws SQLException {
            String selectQuery = "SELECT transaction_history FROM users WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String transactionHistory = resultSet.getString("transaction_history");
                System.out.println("Transaction History:");
                System.out.println(transactionHistory);
            }else {
                System.out.println("No transaction history available.");
            }
            selectStatement.close();
        }
    }


