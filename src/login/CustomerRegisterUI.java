package login;

import ui.CardWindow;
import ui.Dialogs;
import ui.FormPanel;
import ui.Theme;
import ui.ThemedButton;
import ui.ThemedTextField;

/** Customer registration screen. */
@SuppressWarnings("serial")
public class CustomerRegisterUI extends CardWindow {

    private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    public CustomerRegisterUI() {
        super("Car Rental System - Customer Registration", true, true, 380);
        addHeader("Create an Account", "Register to start renting cars");

        FormPanel form = new FormPanel();
        ThemedTextField userField = form.addField("Username", new ThemedTextField(20));
        ThemedTextField emailField = form.addField("Your Email", new ThemedTextField(20));
        ThemedTextField passField = form.addField("Password", new ThemedTextField(20));
        addToCard(form);
        gap(26);

        ThemedButton registerButton = Theme.button("Register");
        ThemedButton backButton = Theme.secondaryButton("Back to Main Menu");
        addToCard(registerButton.fullWidth());
        gap(10);
        addToCard(backButton.fullWidth());
        getRootPane().setDefaultButton(registerButton);

        registerButton.addActionListener(e -> {

            System.out.println("Button pressed");

            String username = userField.getText().trim();
            System.out.println(username);
            String email = emailField.getText().trim();
            System.out.println(email);
            String password = new String(passField.getText());
            System.out.println(password);

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Dialogs.warning(this, "Please fill in all fields.");
                return;
            }
            if (!email.matches(EMAIL_PATTERN)) {
                Dialogs.warning(this, "Please enter a valid email address.");
                return;
            }
            if (CustomerExists.isExists(username)) {
                Dialogs.warning(this, "That username is already taken. Please choose another one.");
                return;
            }

            if (CustomerRegistration.register(username, email, password)) {
                Dialogs.success(this, "Customer Registration successful! You can now log in.");
                userField.setText("");
                emailField.setText("");
                passField.setText("");
            } else {
                Dialogs.error(this, "Registration failed. Please try again.");
            }
        });

        backButton.addActionListener(e -> {
            new MainMenuUI().setVisible(true);
            dispose();
        });
    }
}
