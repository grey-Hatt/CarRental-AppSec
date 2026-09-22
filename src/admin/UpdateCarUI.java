package admin;

import java.sql.SQLException;
import java.util.Locale;

import database.DBConnect;
import ui.Dialogs;

@SuppressWarnings("serial")
public class UpdateCarUI extends CarFormWindow {

    public UpdateCarUI() {
        super("Update Car", "Update a Car", "Enter the Reg ID and the new details", "Update Car");
    }

    @Override
    protected void onSubmit() {
        CarInput car = readInput();
        if (car == null) {
            return;
        }

        String generatedQuery = String.format(Locale.US,
                "UPDATE cars SET car_name='%s', car_brand='%s', car_model='%s', car_engine_no='%s', car_chassis_no='%s', car_status='%s', car_price=%f WHERE reg_id='%s'",
                car.name, car.brand, car.model, car.engine, car.chassis, car.status, car.price, car.reg);

        try {

            int rec = DBConnect.executeStatementUpdate(generatedQuery);

            if (rec > 0) {
                Dialogs.success(this, "Car updated successfully!");
            } else {
                Dialogs.error(this, "Failed to update car. No car with that Reg ID was found.");
            }

        } catch (SQLException ex) {

            Dialogs.dbError(this, ex);

        }
    }
}
