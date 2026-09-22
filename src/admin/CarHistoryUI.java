package admin;

import ui.TableFrame;

@SuppressWarnings("serial")
public class CarHistoryUI extends TableFrame {

    public CarHistoryUI() {
        super("Rental History", CarHistory.COLUMNS, CarHistory::fetchAllRentals, "No rentals have been made yet.");
    }
}
