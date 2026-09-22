package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/** Runs SELECT statements. */
public class DBQuery {

    /**
     * Runs a SELECT and returns the rows as a disconnected result set, so the
     * database connection can be closed right away (the old version left every
     * connection open).
     */
    public static ResultSet fetch(String query) throws SQLException {
        try (Connection conn = DBConnect.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            CachedRowSet rows = RowSetProvider.newFactory().createCachedRowSet();
            rows.populate(rs);
            System.out.println("Fetched car records successfully.");
            return rows;
        }
    }
}
