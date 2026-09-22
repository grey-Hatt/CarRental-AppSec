package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * The black bar with the shop logo at the top of every window.
 * The dashboard version has links and a logout button; the page version just
 * shows the title of the page.
 */
@SuppressWarnings("serial")
public class NavbarPanel extends JPanel {

    private NavbarPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, Theme.GOLD_DARK),
                new EmptyBorder(8, 32, 8, 32)));
        JLabel logo = new JLabel(Theme.logo(64));
        add(logo, BorderLayout.WEST);
    }

    /** Navbar for a sub page: logo plus the page title. */
    public static NavbarPanel forPage(String pageTitle) {
        NavbarPanel bar = new NavbarPanel();
        JLabel title = Theme.label(pageTitle, Font.BOLD, 26, Theme.TEXT);
        title.setBorder(new EmptyBorder(0, 8, 0, 0));
        bar.add(title, BorderLayout.CENTER);
        return bar;
    }

    /** Navbar for the dashboard: links, a role badge and a logout button. */
    public static NavbarPanel forDashboard(String userType, Runnable onLogout) {
        NavbarPanel bar = new NavbarPanel();
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 12));
        right.setOpaque(false);

        ThemedButton home = link("HOME", true);
        ThemedButton about = link("ABOUT", false);
        ThemedButton services = link("SERVICES", false);
        ThemedButton contact = link("CONTACT", false);

        about.addActionListener(e -> Dialogs.show(SwingUtilities.getWindowAncestor(bar), "About Golden Auto Store",
                "Golden Auto Store is a car rental service with a range of well-maintained vehicles at clear daily "
                        + "rates. This application manages the fleet, rentals and returns.",
                Dialogs.Kind.INFO));
        services.addActionListener(e -> Dialogs.show(SwingUtilities.getWindowAncestor(bar), "Our Services",
                "\u2022 Daily car rental\n\u2022 Simple returns with an instant price summary\n"
                        + "\u2022 Clear per-day pricing\n\u2022 Fleet management for staff",
                Dialogs.Kind.INFO));
        contact.addActionListener(e -> Dialogs.show(SwingUtilities.getWindowAncestor(bar), "Contact Us",
                "Golden Auto Store\nEmail: support@goldenautostore.example\nPhone: +92 000 0000000",
                Dialogs.Kind.INFO));

        right.add(home);
        right.add(about);
        right.add(services);
        right.add(contact);

        String role = "admin".equalsIgnoreCase(userType) ? "ADMIN" : "CUSTOMER";
        right.add(new RoleBadge(role));

        ThemedButton logout = Theme.secondaryButton("Logout");
        logout.setPreferredSize(new java.awt.Dimension(96, 38));
        logout.addActionListener(e -> onLogout.run());
        right.add(logout);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private static ThemedButton link(String text, boolean active) {
        ThemedButton b = Theme.linkButton(text);
        b.setFont(Theme.font(Font.BOLD, 14));
        b.setActive(active);
        return b;
    }

    /** Small gold pill showing who is logged in. */
    private static class RoleBadge extends JLabel {
        RoleBadge(String text) {
            super(text, CENTER);
            setFont(Theme.font(Font.BOLD, 11));
            setForeground(Theme.GOLD);
            setBorder(new EmptyBorder(6, 14, 6, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(212, 175, 55, 35));
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.setColor(Theme.GOLD_DARK);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
