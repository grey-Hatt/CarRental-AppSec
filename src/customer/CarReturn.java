package customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnect;
import database.DBQuery;

public class CarReturn {


    public static boolean returnCar(String cnic, String carName, String model, String engineNo) throws SQLException {

        String Sql = "SELECT r.reg_id FROM rented_cars r " + "JOIN cars c ON r.reg_id = c.reg_id "
                + "WHERE r.user_cnic = '" + cnic + "' " + "AND c.car_name = '" + carName + "' "
                + "AND c.car_model = '" + model + "' " + "AND c.car_engine_no = '" + engineNo + "' "
                + "AND r.return_date IS NULL";

        ResultSet rs = DBQuery.fetch(Sql);

        if (rs.next()) {
            int regId = rs.getInt("reg_id");

            String updateRentSql = "UPDATE rented_cars SET return_date = NOW() " + "WHERE user_cnic = '" + cnic
                    + "' AND reg_id = " + regId + " AND return_date IS NULL";

            String updateCarSql = "UPDATE cars SET car_status = 'available' WHERE reg_id = " + regId;

            return DBConnect.executeTransaction(updateRentSql, updateCarSql);
        }

        return false;
    }
}
