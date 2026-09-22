package admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBQuery;
import ui.Formats;

public class CarHistory {

    public static final String[] COLUMNS = { "CNIC", "Reg ID", "Name", "Brand", "Model", "Engine No", "Chassis No",
            "Rent Date", "Return Date", "Status", "Rent / Day" };

    public static List<Object[]> fetchAllRentals() throws SQLException {

        List<Object[]> rows = new ArrayList<>();

        String query = "SELECT c.*, r.rent_date, r.return_date, r.user_cnic FROM cars c "
                + "JOIN rented_cars r ON c.reg_id = r.reg_id ORDER BY r.rent_date DESC";

        ResultSet rs = DBQuery.fetch(query);

        while (rs.next()) {
            java.sql.Timestamp returned = rs.getTimestamp("return_date");
            rows.add(new Object[] { rs.getString("user_cnic"), rs.getString("reg_id"), rs.getString("car_name"),
                    rs.getString("car_brand"), rs.getString("car_model"), rs.getString("car_engine_no"),
                    rs.getString("car_chassis_no"), Formats.dateTime(rs.getTimestamp("rent_date")),
                    Formats.dateTime(returned), returned == null ? "Active" : "Returned", rs.getDouble("car_price") });
        }
        return rows;
    }
}
