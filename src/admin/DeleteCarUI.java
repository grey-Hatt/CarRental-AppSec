package admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.JPanel;

import database.DBConnect;
import ui.CardWindow;
import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedTextField;

@SuppressWarnings("serial")
public class DeleteCarUI extends CardWindow {

    private final ThemedTextField regField = new ThemedTextField(15).placeholder("e.g. 104");

    public DeleteCarUI() {
        super("Car Rental System - Delete Car", false, false, 340);
        addHeader("Delete a Car", "Enter the Reg ID of the car to remove");

        FormPanel form = new FormPanel();
        form.addField("Reg ID", regField);
        addToCard(form);
        gap(28);

        ThemedButton delete = Theme.dangerButton("Delete Car");
        delete.setPreferredSize(new Dimension(150, 44));
        ThemedButton close = Theme.secondaryButton("Close");
        close.setPreferredSize(new Dimension(110, 44));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        buttons.add(delete);
        buttons.add(close);
        addToCard(buttons);

        delete.addActionListener(e -> deleteCar());
        close.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(close);
        packAndCentre();
    }

    private void deleteCar() {

        String reg = regField.getText().trim();
        if (reg.isEmpty()) {
            Dialogs.warning(this, "Please enter the Reg ID of the car.");
            return;
        }
        if (!Dialogs.confirm(this, "Delete Car", "Delete the car with Reg ID " + reg + "? This cannot be undone.",
                "Delete", true)) {
            return;
        }

        String generatedQuery = String.format("DELETE FROM cars WHERE reg_id = '%s'", reg);

        try {

            int rec = DBConnect.executeStatementUpdate(generatedQuery);

            if (rec > 0) {
                Dialogs.success(this, "Car deleted successfully!");
                regField.setText("");
            } else {
                Dialogs.error(this, "Failed to delete car. No car with that Reg ID was found.");
            }

        } catch (SQLException ex) {

            Dialogs.dbError(this, ex);

        }
    }
}
