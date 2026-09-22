package customer;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;

import database.DBQuery;
import ui.Dialogs;
import ui.Theme;


public class RentPrice {

    public static void showPaymentDialog(Component parent, String cnic, String name, String model, String engine) {
        try {

            String sql = "SELECT r.rent_date, r.return_date, c.car_price "
                    + "FROM rented_cars r JOIN cars c ON r.reg_id = c.reg_id " + "WHERE r.user_cnic = '" + cnic
                    + "' AND c.car_name = '" + name + "' AND c.car_model = '" + model + "' AND c.car_engine_no = '"
                    + engine + "' " + "ORDER BY r.rent_date DESC LIMIT 1";

            ResultSet rs = DBQuery.fetch(sql);

            if (rs.next() && rs.getTimestamp("return_date") != null) {

                Timestamp rentTS = rs.getTimestamp("rent_date");
                Timestamp returnTS = rs.getTimestamp("return_date");
                double pricePerDay = rs.getDouble("car_price");

                long minutes = Duration.between(rentTS.toLocalDateTime(), returnTS.toLocalDateTime()).toMinutes();
                long days = Math.max(1, (long) Math.ceil(minutes / (24.0 * 60.0)));

                double total = days * pricePerDay;

                Dialogs.show(parent, "Payment Due",
                        "Days rented: " + days + "\nPrice per day: " + Theme.money(pricePerDay)
                                + "\nTotal payment: " + Theme.money(total)
                                + "\n\nPayment is to be made at the shop when returning the car.",
                        Dialogs.Kind.INFO);
            } else {
                Dialogs.warning(parent, "No matching rental record found.");
            }

        } catch (SQLException ex) {
            Dialogs.dbError(parent, ex);
        }
    }

}
