package admin;

import java.sql.SQLException;
import java.util.Locale;

import database.DBConnect;
import ui.Dialogs;

@SuppressWarnings("serial")
public class AddCarUI extends CarFormWindow {

    public AddCarUI() {
        super("Add Car", "Add a Car", "Enter the details of the new car", "Add Car");
    }

    @Override
    protected void onSubmit() {
        CarInput car = readInput();
        if (car == null) {
            return;
        }

        String generatedQuery = String.format(Locale.US,
                "INSERT INTO cars (reg_id, car_name, car_brand, car_model, car_engine_no, car_chassis_no, car_status, car_price) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %f)",
                car.reg, car.name, car.brand, car.model, car.engine, car.chassis, car.status, car.price);

        try {

            int rec = DBConnect.executeStatementUpdate(generatedQuery);

            if (rec > 0) {
                Dialogs.success(this, "Car added successfully!");
                clearForm();
            } else {
                Dialogs.error(this, "Failed to add car.");
            }

        } catch (SQLException ex) {

            Dialogs.dbError(this, ex);

        }
    }
}
