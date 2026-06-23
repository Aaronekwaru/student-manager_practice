package com.mycompany.practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:studentmanager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS courses (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  course_name TEXT NOT NULL," +
                "  description TEXT," +
                "  credits INTEGER DEFAULT 3" +
                ")"
            );
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS students (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  name TEXT NOT NULL," +
                "  email TEXT," +
                "  phone TEXT," +
                "  course_id INTEGER," +
                "  enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (course_id) REFERENCES courses(id)" +
                ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
