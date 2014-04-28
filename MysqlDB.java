package lock;
import java.sql.*;

public class MysqlDB {
    public static Connection connection;
    public static Statement statement;
    
    public static Statement connect() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://195.178.232.7:4040/ad1067","ad1067","Alfa2013");
            statement = connection.createStatement();
        } catch(ClassNotFoundException e1) {
            System.out.println("Databas-driver hittades ej: "+e1);
        }
        return statement;
    }
    
    public static void disconnect() throws SQLException {
        statement.close();
        connection.close();
    }
}