package customer;

import admin.AvailableCarsUI;
import ui.Dialogs;
import ui.MenuWindow;

@SuppressWarnings("serial")
public class CustomerMenuUI extends MenuWindow {

    public CustomerMenuUI() {
        super("Car Rental System - Customer", "Customer Menu", "What would you like to do?");

        addAction("Available Cars", () -> new AvailableCarsUI().setVisible(true));
        addAction("Rent a Car", () -> new CarRentUI().setVisible(true));
        addAction("My Rental History", this::showHistory);
        addAction("Return a Car", () -> new CarReturnDialog(this).setVisible(true));

        finishMenu();
    }

    private void showHistory() {
        String cnic = Dialogs.prompt(this, "My Rental History", "Enter your CNIC to see your rentals:",
                "e.g. 35202-1234567-1");
        if (cnic != null && !cnic.trim().isEmpty()) {
            new CarHistoryUI(cnic.trim()).setVisible(true);
        }
    }
}
