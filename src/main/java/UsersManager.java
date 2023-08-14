import java.sql.*;

public class UsersManager {

    Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/sqlite3/mydatabase.db");

    PreparedStatement selectStatement = null;
    ResultSet resultSet = null;

    public UsersManager() throws SQLException {
    }

    public void changePassword(Connection connection,int userId, String currentPassword, String newPassword) throws SQLException {

        try {
            String selectQuery = "SELECT password FROM users WHERE id = ?";
            String updateQuery = "UPDATE users SET password = ? WHERE id = ? AND password = ?";

            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (currentPassword.equals(storedPassword)) {
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, newPassword);
                    updateStatement.setInt(2, userId);
                    updateStatement.setString(3, currentPassword);

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Password changed successfully.");
                    } else {
                        System.out.println("Failed to change password. Incorrect current password.");
                    }
                    updateStatement.close();
                } else {
                    System.out.println("User not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (selectStatement != null) {
                selectStatement.close();
            }
        }
    }


    public void addUser(String username, String password, double balance) {
        try {

            String insertQuery = "INSERT INTO users (username, password, balance ) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setDouble(3, balance);

            preparedStatement.executeUpdate();

            preparedStatement.close();


            System.out.println("User added !");
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                System.out.println("User with the same username already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void removeUser(String username) {

        try {
            String deleteQuery = "DELETE FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, username);

            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();


            if (rowsAffected > 0 ){
                System.out.println("User " + username + " deleted.");
            }else {
                System.out.println("User not found.");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {

        UsersManager usersManager = new UsersManager();

        //usersManager.addUser("Aldo","12345",5000);

        //usersManager.removeUser("Pol");

    }
}


