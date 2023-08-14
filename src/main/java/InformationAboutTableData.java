import java.sql.*;

public class InformationAboutTableData{
    public static void main(String[] args) throws SQLException {

        Connection connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");

        try {


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = databaseMetaData.getTables(null, null, null ,new String[]{"TABLE"});
            System.out.println("List of tables:");

            while (tables.next()){
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }

            while (resultSet.next()) {
                System.out.println("");
                System.out.println("Users:");
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    System.out.println(columnName + ": " + value);
                }
                System.out.println();
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

