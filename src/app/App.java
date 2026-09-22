package app;

import javax.swing.SwingUtilities;

import login.MainMenuUI;
import ui.Theme;

public class App {

    public static void main(String[] args) {
        Theme.install();
        // Swing windows must be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainMenuUI().setVisible(true));
    }
}
