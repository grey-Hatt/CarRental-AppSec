package dashboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.ImagePanel;
import ui.Theme;
import ui.ThemedButton;

@SuppressWarnings("serial")
public class HeroPanel extends ImagePanel {

    public HeroPanel(String userType, Runnable onGetStarted) {
        super(Theme.loadImage("/dashboard/hero.jpg"), null);
        setLayout(new GridBagLayout());

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel tag = Theme.label("GOLDEN AUTO STORE", Font.BOLD, 15, Theme.GOLD);
        JLabel line1 = Theme.label("Welcome to", Font.BOLD, 52, Color.WHITE);
        JLabel line2 = Theme.label("Car Rental System", Font.BOLD, 52, Color.WHITE);
        String pitch = "admin".equalsIgnoreCase(userType)
                ? "Manage your fleet, rentals and returns in one place."
                : "Pick a car from our fleet and get on the road in minutes.";
        JLabel sub = Theme.label(pitch, Font.PLAIN, 18, new Color(0xE4E4E7));

        ThemedButton start = Theme.button("Get Started");
        start.setFont(Theme.font(Font.BOLD, 18));
        start.setPreferredSize(new Dimension(210, 56));
        start.addActionListener(e -> onGetStarted.run());

        for (Component c : new Component[] { tag, line1, line2, sub, start }) {
            ((javax.swing.JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        text.add(tag);
        text.add(Box.createVerticalStrut(14));
        text.add(line1);
        text.add(line2);
        text.add(Box.createVerticalStrut(16));
        text.add(sub);
        text.add(Box.createVerticalStrut(34));
        text.add(start);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 96, 40, 0);
        add(text, gbc);
    }

    @Override
    protected void paintOverlay(Graphics2D g2, int width, int height) {
        g2.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 225), width * 0.62f, 0, new Color(0, 0, 0, 0)));
        g2.fillRect(0, 0, width, height);
        g2.setPaint(new GradientPaint(0, height * 0.6f, new Color(0, 0, 0, 0), 0, height, new Color(0, 0, 0, 150)));
        g2.fillRect(0, 0, width, height);
    }
}
