package admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBQuery;
import ui.Formats;

public class CarInventory {

    public static final String[] COLUMNS = { "Reg ID", "Name", "Brand", "Model", "Engine No", "Chassis No", "Status",
            "Rent / Day" };

    private static final String AVAILABLE_QUERY = "SELECT * FROM cars WHERE car_status = 'available'";
    private static final String RENTED_QUERY = "SELECT * FROM cars WHERE car_status = 'rented'";

    public static List<Object[]> fetchAvailable() throws SQLException {
        return fetch(AVAILABLE_QUERY);
    }

    public static List<Object[]> fetchRented() throws SQLException {
        return fetch(RENTED_QUERY);
    }

    private static List<Object[]> fetch(String query) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        ResultSet rs = DBQuery.fetch(query);
        while (rs.next()) {
            rows.add(new Object[] { rs.getString("reg_id"), rs.getString("car_name"), rs.getString("car_brand"),
                    rs.getString("car_model"), rs.getString("car_engine_no"), rs.getString("car_chassis_no"),
                    Formats.capitalise(rs.getString("car_status")), rs.getDouble("car_price") });
        }
        return rows;
    }
}
