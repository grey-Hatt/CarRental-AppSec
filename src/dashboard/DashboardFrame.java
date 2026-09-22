package dashboard;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JFrame;

import admin.AdminMenuUI;
import customer.CustomerMenuUI;
import login.MainMenuUI;
import ui.AppFrame;
import ui.NavbarPanel;

@SuppressWarnings("serial")
public class DashboardFrame extends AppFrame {

    private final String userType;
    private JFrame menu; 

    public DashboardFrame(String userType) {
        super("Car Rental System", true);
        this.userType = userType;
        maximize();
        setLayout(new BorderLayout());

        add(NavbarPanel.forDashboard(userType, this::logout), BorderLayout.NORTH);
        add(new HeroPanel(userType, this::openMenu), BorderLayout.CENTER);
    }

    private void openMenu() {
        if (menu != null && menu.isDisplayable()) {
            menu.toFront();
            return;
        }
        menu = "admin".equalsIgnoreCase(userType) ? new AdminMenuUI() : new CustomerMenuUI();
        menu.setVisible(true);
    }

    private void logout() {
        MainMenuUI next = new MainMenuUI();
        next.setVisible(true);
        for (Window window : Window.getWindows()) {
            if (window != next) {
                window.dispose();
            }
        }
    }
}
