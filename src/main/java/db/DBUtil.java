package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL="jdbc:mysql://127.0.0.1:3306/trainee";
    private static final String USERNAME="root";
    private static final String PASSWORD="";


    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            throw new ExceptionInInitializerError("MYSQL JDBC driver not found" + e.getMessage());
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
}
