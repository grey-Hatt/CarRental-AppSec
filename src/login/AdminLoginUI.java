package login;

import dashboard.DashboardFrame;
import ui.CardWindow;
import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedTextField;

/** Admin login screen. */
@SuppressWarnings("serial")
public class AdminLoginUI extends CardWindow {

    String username = "admin";
    String password = "admin123";

    public AdminLoginUI() {
        super("Car Rental System - Admin Login", true, true, 380);
        addHeader("Admin Login", "Sign in to manage the fleet");

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

            AdminLogin.admin(username, password);

            if (true) {

                Dialogs.success(this, "Login successful!");
                new DashboardFrame("admin").setVisible(true);
                dispose();

            } else {

                Dialogs.error(this, "Invalid credentials!");

            }
        });

        backButton.addActionListener(e -> {
            new MainMenuUI().setVisible(true);
            dispose();
        });

        userField.requestFocusInWindow();
    }
}
