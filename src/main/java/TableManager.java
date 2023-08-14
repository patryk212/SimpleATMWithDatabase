import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableManager {

    Connection connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");

    public TableManager(Connection connection) throws SQLException {
    }


    public void createTable(String tableName, String columnsDefinition){
        try {

            Statement statement = connection.createStatement();
            String createTableQuery = "CREATE TABLE " + tableName + " (" + columnsDefinition + ");";
            statement.executeUpdate(createTableQuery);
            statement.close();
            System.out.println("Table '" + tableName + "' created successfully.");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    public void deleteTable(String tableName) {
        try {

            Statement statement = connection.createStatement();

            String dropTableQuery = "DROP TABLE " + tableName + ";";

            statement.executeUpdate(dropTableQuery);

            statement.close();
            connection.close();

            System.out.println("Table "+ tableName + "'dropped successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/sqlite3/mydatabase.db");
        TableManager tableManager = new TableManager(connection);

        //String userTableDefinition = "id INTEGER PRIMARY KEY, users TEXT NOT NULL, balance REAL NOT NULL";
        //tableManager.createTable("test", userTableDefinition);



        tableManager.deleteTable("test");
        connection.close();

    }
}
