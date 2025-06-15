package database;

import java.sql.*;

public class DB {
    private static final String URL = "jdbc:sqlite:militares.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
