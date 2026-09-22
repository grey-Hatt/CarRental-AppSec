package customer;

import ui.TableFrame;

@SuppressWarnings("serial")
public class CarHistoryUI extends TableFrame {

    public CarHistoryUI(String cnic) {
        super("My Rental History", CarHistory.COLUMNS, () -> CarHistory.fetchByCnic(cnic),
                "No rentals found for this CNIC.");
    }
}
