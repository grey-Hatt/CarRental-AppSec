package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sends INSERT / UPDATE / DELETE statements to the MySQL database.
 * Connections are always closed again (try-with-resources), and problems are
 * reported to the caller as SQLException so the screen can tell the user.
 */
public class DBConnect {

    private static final String URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String USER = "root";
    private static final String PASS = "root";

    /** Opens a new connection. Callers must close it. */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found. Put mysql-connector-j-9.3.0.jar in the lib folder.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /** Runs one statement and returns the number of rows it changed. */
    public static int executeStatementUpdate(String query) throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            int result = stmt.executeUpdate(query);
            System.out.println("Query executed successfully: " + query);
            return result;
        }
    }

    /**
     * Runs several statements as one unit of work: either all of them change at
     * least one row and are committed, or nothing is changed at all.
     *
     * @return true if everything was committed, false if one statement changed no rows
     */
    public static boolean executeTransaction(String... queries) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                for (String query : queries) {
                    if (stmt.executeUpdate(query) < 1) {
                        conn.rollback();
                        return false;
                    }
                    System.out.println("Query executed successfully: " + query);
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
