package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * A window that shows one centred "card" (login form, menu, add-car form, ...).
 * Full screen windows get the photo background; small windows a plain dark one.
 * The card stays centred whatever the window size, so nothing depends on the
 * screen resolution.
 */
@SuppressWarnings("serial")
public class CardWindow extends AppFrame {

    protected final RoundedPanel card;

    protected CardWindow(String title, boolean mainWindow, boolean fullScreen, int cardWidth) {
        super(title, mainWindow);
        ImagePanel root = new ImagePanel(fullScreen ? Theme.loadImage("/login/main.jpg") : null,
                fullScreen ? new Color(0, 0, 0, 120) : null);
        root.setLayout(new GridBagLayout());

        card = new RoundedPanel(new Color(14, 14, 16, fullScreen ? 235 : 255), Theme.BORDER, 26);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 34, 40));
        card.add(Box.createRigidArea(new Dimension(cardWidth, 0)));
        root.add(card, new GridBagConstraints());

        setContentPane(root);
        if (fullScreen) {
            maximize();
        }
    }

    /** Logo, heading and (optional) sub-heading at the top of the card. */
    protected void addHeader(String heading, String subtitle) {
        if (Theme.logo(72) != null) {
            JLabel logo = new JLabel(Theme.logo(72));
            logo.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(logo);
            card.add(Box.createVerticalStrut(8));
        }
        JLabel title = Theme.heading(heading);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        if (subtitle != null) {
            card.add(Box.createVerticalStrut(6));
            JLabel sub = Theme.muted(subtitle);
            sub.setHorizontalAlignment(SwingConstants.CENTER);
            sub.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(sub);
        }
        card.add(Box.createVerticalStrut(24));
    }

    /** Adds a component to the card, stretched to the card width. */
    protected <T extends JComponent> T addToCard(T component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(component);
        return component;
    }

    protected void gap(int pixels) {
        card.add(Box.createVerticalStrut(pixels));
    }

    /** Sizes the window to its content and centres it on screen (for small windows). */
    protected void packAndCentre() {
        pack();
        setLocationRelativeTo(null);
    }
}
