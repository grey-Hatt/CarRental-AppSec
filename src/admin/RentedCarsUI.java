package admin;

import ui.TableFrame;

@SuppressWarnings("serial")
public class RentedCarsUI extends TableFrame {

    public RentedCarsUI() {
        super("Rented Cars", CarInventory.COLUMNS, CarInventory::fetchRented, "No cars are rented out at the moment.");
    }
}
