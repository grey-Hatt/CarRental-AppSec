package login;

import ui.CardWindow;
import ui.Theme;
import ui.ThemedButton;

/** First screen: choose admin login, customer login or registration. */
@SuppressWarnings("serial")
public class MainMenuUI extends CardWindow {

    public MainMenuUI() {
        super("Car Rental System", true, true, 400);
        addHeader("Welcome to Car Rental System", "Please choose how you would like to continue");

        ThemedButton adminButton = Theme.button("Login as Admin");
        ThemedButton customerButton = Theme.button("Login as Customer");
        ThemedButton registerButton = Theme.secondaryButton("Customer Registration");

        addToCard(adminButton.fullWidth());
        gap(14);
        addToCard(customerButton.fullWidth());
        gap(14);
        addToCard(registerButton.fullWidth());

        adminButton.addActionListener(e -> {
            new AdminLoginUI().setVisible(true);
            dispose();
        });
        customerButton.addActionListener(e -> {
            new CustomerLoginUI().setVisible(true);
            dispose();
        });
        registerButton.addActionListener(e -> {
            new CustomerRegisterUI().setVisible(true);
            dispose();
        });
    }
}
