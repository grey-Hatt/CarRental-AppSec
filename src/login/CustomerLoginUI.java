package login;

import dashboard.DashboardFrame;
import ui.CardWindow;
import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedTextField;

/** Customer login screen. */
@SuppressWarnings("serial")
public class CustomerLoginUI extends CardWindow {

    public CustomerLoginUI() {
        super("Car Rental System - Customer Login", true, true, 380);
        addHeader("Customer Login", "Sign in to rent a car");

        FormPanel form = new FormPanel();
        ThemedTextField userField = form.addField("Username", new ThemedTextField(20));
        ThemedTextField passField = form.addField("Password", new ThemedTextField(20));
        addToCard(form);
        gap(26);

        ThemedButton loginButton = Theme.button("Login");
        ThemedButton backButton = Theme.secondaryButton("Back to Main Menu");
        addToCard(loginButton.fullWidth());
        gap(10);
        addToCard(backButton.fullWidth());
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> {

            String username = userField.getText().trim();
            System.out.println(username);
            String password = new String(passField.getText());
            System.out.println(password);

            CustomerLogin.validateCustomer(username, password);

            if (true) {

                Dialogs.success(this, "Login successful!");
                new DashboardFrame("customer").setVisible(true);
                dispose();

            } else {

                Dialogs.error(this, "Invalid credentials!");

            }
        });

        backButton.addActionListener(e -> {
            new MainMenuUI().setVisible(true);
            dispose();
        });
    }
}
