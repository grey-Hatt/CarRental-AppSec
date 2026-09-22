package admin;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import ui.CardWindow;
import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedComboBox;
import ui.ThemedTextField;


@SuppressWarnings("serial")
abstract class CarFormWindow extends CardWindow {

    protected static class CarInput {
        final String reg;
        final String name;
        final String brand;
        final String model;
        final String engine;
        final String chassis;
        final String status;
        final double price;

        CarInput(String reg, String name, String brand, String model, String engine, String chassis, String status,
                double price) {
            this.reg = reg;
            this.name = name;
            this.brand = brand;
            this.model = model;
            this.engine = engine;
            this.chassis = chassis;
            this.status = status;
            this.price = price;
        }
    }

    protected final ThemedTextField regField = new ThemedTextField(15).placeholder("e.g. 104");
    protected final ThemedTextField nameField = new ThemedTextField(15).placeholder("e.g. Corolla Altis");
    protected final ThemedTextField brandField = new ThemedTextField(15).placeholder("e.g. Toyota");
    protected final ThemedTextField modelField = new ThemedTextField(15).placeholder("e.g. 2022");
    protected final ThemedTextField engineField = new ThemedTextField(15).placeholder("e.g. 2ZR-1001");
    protected final ThemedTextField chassisField = new ThemedTextField(15).placeholder("e.g. JTD-5001");
    protected final ThemedTextField priceField = new ThemedTextField(15).placeholder("e.g. 6500");
    protected final ThemedComboBox<String> statusBox = new ThemedComboBox<>(new String[] { "Available", "Rented" });

    protected CarFormWindow(String windowTitle, String heading, String subtitle, String buttonText) {
        super("Car Rental System - " + windowTitle, false, false, 520);
        addHeader(heading, subtitle);

        FormPanel form = new FormPanel();
        form.addPair("Reg ID", regField, "Name", nameField);
        form.addPair("Brand", brandField, "Model", modelField);
        form.addPair("Engine No", engineField, "Chassis No", chassisField);
        form.addPair("Availability", statusBox, "Rent per Day (Rs.)", priceField);
        addToCard(form);
        gap(28);

        ThemedButton submit = Theme.button(buttonText);
        submit.setPreferredSize(new Dimension(170, 44));
        ThemedButton close = Theme.secondaryButton("Close");
        close.setPreferredSize(new Dimension(110, 44));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setOpaque(false);
        buttons.add(submit);
        buttons.add(close);
        addToCard(buttons);

        submit.addActionListener(e -> onSubmit());
        close.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(submit);
        packAndCentre();
    }

    protected abstract void onSubmit();

    protected CarInput readInput() {
        String reg = regField.getText().trim();
        String name = nameField.getText().trim();
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String engine = engineField.getText().trim();
        String chassis = chassisField.getText().trim();
        String priceText = priceField.getText().trim();

        if (reg.isEmpty() || name.isEmpty() || brand.isEmpty() || model.isEmpty() || engine.isEmpty()
                || chassis.isEmpty() || priceText.isEmpty()) {
            Dialogs.warning(this, "Please fill in all fields.");
            return null;
        }
        try {
            Integer.parseInt(reg);
        } catch (NumberFormatException ex) {
            Dialogs.warning(this, "Reg ID must be a whole number.");
            return null;
        }
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            price = -1;
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price <= 0) {
            Dialogs.warning(this, "Rent per Day must be a number greater than 0.");
            return null;
        }
        return new CarInput(reg, name, brand, model, engine, chassis, statusBox.selectedText().toLowerCase(), price);
    }

    protected void clearForm() {
        regField.setText("");
        nameField.setText("");
        brandField.setText("");
        modelField.setText("");
        engineField.setText("");
        chassisField.setText("");
        priceField.setText("");
        statusBox.setSelectedIndex(0);
        regField.requestFocusInWindow();
    }
}
