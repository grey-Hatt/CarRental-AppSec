package admin;

import ui.TableFrame;

@SuppressWarnings("serial")
public class AvailableCarsUI extends TableFrame {

    public AvailableCarsUI() {
        super("Available Cars", CarInventory.COLUMNS, CarInventory::fetchAvailable, "No cars are available right now.");
    }
}
