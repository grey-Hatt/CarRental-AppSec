package customer;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedTextField;

@SuppressWarnings("serial")
public class CarReturnDialog extends JDialog {

    private final ThemedTextField cnicField = new ThemedTextField(20).placeholder("e.g. 35202-1234567-1");
    private final ThemedTextField nameField = new ThemedTextField(20).placeholder("e.g. Corolla Altis");
    private final ThemedTextField modelField = new ThemedTextField(20).placeholder("e.g. 2022");
    private final ThemedTextField engineField = new ThemedTextField(20).placeholder("e.g. 2ZR-1001");

    public CarReturnDialog(Window parent) {
        super(parent, "Return a Car", ModalityType.APPLICATION_MODAL);

        JPanel root = new JPanel();
        root.setLayout(new java.awt.BorderLayout(0, 20));
        root.setBackground(Theme.SURFACE);
        root.setBorder(BorderFactory.createEmptyBorder(26, 32, 26, 32));

        JPanel top = new JPanel(new java.awt.GridLayout(0, 1, 0, 4));
        top.setOpaque(false);
        top.add(Theme.heading("Return a Car"));
        top.add(Theme.muted("Enter your CNIC and the details of the car"));
        root.add(top, java.awt.BorderLayout.NORTH);

        FormPanel form = new FormPanel();
        form.addField("Your CNIC", cnicField);
        form.addField("Car Name", nameField);
        form.addField("Car Model", modelField);
        form.addField("Engine No", engineField);
        root.add(form, java.awt.BorderLayout.CENTER);

        ThemedButton returnButton = Theme.button("Return Car");
        returnButton.setPreferredSize(new Dimension(150, 44));
        ThemedButton cancel = Theme.secondaryButton("Cancel");
        cancel.setPreferredSize(new Dimension(110, 44));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(cancel);
        buttons.add(returnButton);
        root.add(buttons, java.awt.BorderLayout.SOUTH);

        returnButton.addActionListener(e -> returnCar());
        cancel.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(returnButton);

        setContentPane(root);
        setResizable(false);
        setMinimumSize(new Dimension(440, 0));
        pack();
        setLocationRelativeTo(parent);
    }

    private void returnCar() {
        String cnic = cnicField.getText().trim();
        String name = nameField.getText().trim();
        String model = modelField.getText().trim();
        String engine = engineField.getText().trim();

        if (cnic.isEmpty() || name.isEmpty() || model.isEmpty() || engine.isEmpty()) {
            Dialogs.warning(this, "Please fill all fields.");
            return;
        }

        try {
            if (CarReturn.returnCar(cnic, name, model, engine)) {
                Dialogs.success(this, "Car returned successfully!");
                RentPrice.showPaymentDialog(this, cnic, name, model, engine);
                dispose();
            } else {
                Dialogs.warning(this, "No matching rented car found or already returned.");
            }
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }
}
