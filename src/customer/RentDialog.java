package customer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

import database.DBConnect;
import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedTextField;

@SuppressWarnings("serial")
public class RentDialog extends JDialog {

    private final ThemedTextField cnicField = new ThemedTextField(20).placeholder("e.g. 35202-1234567-1");
    private final Car car;
    private final Runnable onRented;

 
    public RentDialog(Window parent, Car car, Runnable onRented) {
        super(parent, "Rent Car: " + car.getName(), ModalityType.APPLICATION_MODAL);
        this.car = car;
        this.onRented = onRented;

        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(Theme.SURFACE);
        root.setBorder(BorderFactory.createEmptyBorder(26, 32, 26, 32));

        JPanel top = new JPanel(new java.awt.GridLayout(0, 1, 0, 4));
        top.setOpaque(false);
        top.add(Theme.heading(car.getName()));
        top.add(Theme.muted(car.getBrand() + " " + car.getModel() + "  \u00b7  " + Theme.money(car.getPrice()) + " per day"));
        root.add(top, BorderLayout.NORTH);

        FormPanel form = new FormPanel();
        form.addField("Your CNIC", cnicField);
        root.add(form, BorderLayout.CENTER);

        ThemedButton confirm = Theme.button("Confirm Rent");
        confirm.setPreferredSize(new Dimension(150, 44));
        ThemedButton cancel = Theme.secondaryButton("Cancel");
        cancel.setPreferredSize(new Dimension(110, 44));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(cancel);
        buttons.add(confirm);
        root.add(buttons, BorderLayout.SOUTH);

        confirm.addActionListener(e -> confirmRent());
        cancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(confirm);

        setContentPane(root);
        setResizable(false);
        setMinimumSize(new Dimension(440, 0));
        pack();
        setLocationRelativeTo(parent);
    }

    private void confirmRent() {
        String cnic = cnicField.getText().trim();
        if (cnic.isEmpty()) {
            Dialogs.warning(this, "Please enter your CNIC.");
            return;
        }
        try {
            if (rentCar(car.getRegId(), cnic)) {
                Dialogs.success(this, "Car rented successfully!");
                dispose();
                onRented.run();
            } else {
                Dialogs.warning(this, "Sorry, this car is no longer available.");
                dispose();
                onRented.run();
            }
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }


    private boolean rentCar(int regId, String cnic) throws SQLException {
        String updateQuery = "UPDATE cars SET car_status = 'rented' WHERE reg_id = " + regId
                + " AND car_status = 'available'";
        String insertQuery = "INSERT INTO rented_cars (reg_id, user_cnic) VALUES (" + regId + ", '" + cnic + "')";

        return DBConnect.executeTransaction(updateQuery, insertQuery);
    }
}
