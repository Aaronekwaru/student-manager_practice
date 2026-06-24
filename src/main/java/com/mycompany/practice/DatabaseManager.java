package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 * This is the database layer. It handles:
 *   1. Connecting to the SQLite database file
 *   2. Creating the tables (courses and students) if they
 *      don't already exist
 *
 * Think of it as the "bridge" between our Java app and the
 * database. Every other class calls DatabaseManager to get
 * a connection and run SQL queries.
 * ============================================================
 */

// Import the JDBC classes we need for database work
import java.sql.Connection;      // Represents a connection to the database
import java.sql.DriverManager;   // Creates the connection using a URL
import java.sql.SQLException;    // For catching database errors
import java.sql.Statement;       // For running SQL commands

public class DatabaseManager {

    /*
     * JDBC URL format:  jdbc:sqlite:filename.db
     *
     * This tells Java: "Use the SQLite driver to connect to
     * a file called studentmanager.db".
     *
     * The .db file will be created in the project's root
     * folder automatically when we first connect.
     */
    private static final String DB_URL = "jdbc:sqlite:studentmanager.db";

    /*
     * getConnection()
     * ---------------
     * Called whenever another class needs to talk to the DB.
     *
     * It returns a Connection object, which is like opening
     * a phone line to the database. We can then send SQL
     * commands through this connection.
     *
     * Usage example:
     *   Connection conn = DatabaseManager.getConnection();
     *   Statement stmt = conn.createStatement();
     *   stmt.execute("SELECT ...");
     *   conn.close();  // Always close when done!
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /*
     * initialize()
     * ------------
     * Called ONCE when the app starts (from StudentManager's
     * constructor).
     *
     * It creates the database tables if they don't exist yet.
     * This is called "schema initialization".
     *
     * We run two CREATE TABLE IF NOT EXISTS statements:
     *   1. courses  - stores the list of courses
     *   2. students - stores student info, linked to a course
     *
     * The "try-with-resources" pattern (try(...)) automatically
     * closes the Connection and Statement when done, so we
     * don't need to call .close() manually.
     */
    public static void initialize() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            /*
             * CREATE TABLE courses
             * --------------------
             * This table stores each course/class:
             *
             *   id          - unique number for each course
             *                 AUTOINCREMENT means SQLite
             *                 automatically assigns 1, 2, 3...
             *                 PRIMARY KEY means this is the
             *                 main identifier for each row
             *
             *   course_name - the name (e.g. "Math 101")
             *                 TEXT = string, NOT NULL = required
             *
             *   description - optional details about the course
             *
             *   credits     - number of credit hours
             *                 DEFAULT 3 means if we don't
             *                 specify a value, it uses 3
             */
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS courses (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  course_name TEXT NOT NULL," +
                "  description TEXT," +
                "  credits INTEGER DEFAULT 3" +
                ")"
            );

            /*
             * CREATE TABLE students
             * ---------------------
             * This table stores each student:
             *
             *   id         - unique student number (auto)
             *
             *   name       - student's full name (required)
             *
             *   email      - email address
             *
             *   phone      - phone number
             *
             *   course_id  - which course the student is in
             *                This is a FOREIGN KEY, meaning
             *                it references the courses table.
             *                If course_id = 2, that means
             *                this student is in the course
             *                with id = 2.
             *
             *   enrollment_date - when they enrolled
             *                DEFAULT CURRENT_TIMESTAMP means
             *                SQLite automatically fills in
             *                the current date/time
             */
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
            /*
             * If something goes wrong (e.g. can't create file,
             * disk full, etc.), we print the error so the
             * developer can see what happened.
             */
            e.printStackTrace();
        }
    }

    /*
     * SUMMARY: HOW THE DATABASE WORKS
     * ================================
     *
     *   courses table                students table
     *   ┌──────────────┐             ┌──────────────────┐
     *   │ id (PK)      │◄────┐      │ id (PK)          │
     *   │ course_name  │     └──────│ course_id (FK)   │
     *   │ description  │            │ name             │
     *   │ credits      │            │ email            │
     *   └──────────────┘            │ phone            │
     *                               │ enrollment_date  │
     *                               └──────────────────┘
     *
     *   PK = Primary Key (unique ID for each row)
     *   FK = Foreign Key (links to another table's PK)
     *
     *   One course can have MANY students.
     *   This is called a "one-to-many" relationship.
     */
}
