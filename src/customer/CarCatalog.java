package customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBQuery;

public class CarCatalog {

    public static List<Car> fetchAvailable() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE car_status = 'available'";

        ResultSet rs = DBQuery.fetch(query);
        while (rs.next()) {
            cars.add(new Car(rs.getInt("reg_id"), rs.getString("car_name"), rs.getString("car_brand"),
                    rs.getString("car_model"), rs.getString("car_engine_no"), rs.getString("car_chassis_no"),
                    rs.getString("car_status"), rs.getDouble("car_price")));
        }
        return cars;
    }
}
