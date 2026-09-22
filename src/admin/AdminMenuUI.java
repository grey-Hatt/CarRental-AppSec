package admin;

import ui.MenuWindow;

@SuppressWarnings("serial")
public class AdminMenuUI extends MenuWindow {

    public AdminMenuUI() {
        super("Car Rental System - Admin", "Admin Panel", "Manage the rental fleet");

        addAction("Add Car", () -> new AddCarUI().setVisible(true));
        addAction("Update Car", () -> new UpdateCarUI().setVisible(true));
        addAction("Delete Car", () -> new DeleteCarUI().setVisible(true));
        addAction("Available Cars", () -> new AvailableCarsUI().setVisible(true));
        addAction("Rented Cars", () -> new RentedCarsUI().setVisible(true));
        addAction("Rental History", () -> new CarHistoryUI().setVisible(true));

        finishMenu();
    }
}
