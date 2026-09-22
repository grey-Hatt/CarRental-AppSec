package ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicTextFieldUI;

/** A dark text field with a gold focus outline and optional placeholder text. */
@SuppressWarnings("serial")
public class ThemedTextField extends JTextField {

    private String placeholder;

    public ThemedTextField(int columns) {
        super(columns);
        setFont(Theme.font(java.awt.Font.PLAIN, 14));
        setForeground(Theme.TEXT);
        setBackground(Theme.SURFACE_2);
        setCaretColor(Theme.GOLD);
        setSelectionColor(Theme.GOLD_DARK);
        setSelectedTextColor(Color.WHITE);
        setBorder(fieldBorder(false));
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(fieldBorder(true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(fieldBorder(false));
            }
        });
    }

    /** Use the plain "basic" UI so the field looks identical on every platform. */
    @Override
    public void updateUI() {
        setUI(new BasicTextFieldUI());
    }

    public ThemedTextField placeholder(String text) {
        this.placeholder = text;
        return this;
    }

    private static Border fieldBorder(boolean focused) {
        Color line = focused ? Theme.GOLD : Theme.BORDER;
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(line, focused ? 2 : 1),
                BorderFactory.createEmptyBorder(focused ? 8 : 9, focused ? 11 : 12, focused ? 8 : 9, focused ? 11 : 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (placeholder != null && getText().isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(new Color(Theme.MUTED.getRed(), Theme.MUTED.getGreen(), Theme.MUTED.getBlue(), 140));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }
}
