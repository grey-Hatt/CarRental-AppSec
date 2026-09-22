package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

/** A flat, rounded button that looks the same on every operating system. */
@SuppressWarnings("serial")
public class ThemedButton extends JButton {

    public enum Kind {
        PRIMARY, SECONDARY, DANGER, LINK
    }

    private static final int ARC = 14;

    private final Kind kind;
    private boolean hover;
    private boolean active;

    public ThemedButton(String text, Kind kind) {
        super(text);
        this.kind = kind;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setFont(Theme.font(Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    /** Makes the button stretch to the full width of a vertical BoxLayout. */
    public ThemedButton fullWidth() {
        Dimension pref = getPreferredSize();
        setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        return this;
    }

    /** For LINK buttons: marks the current page (gold text with an underline). */
    public ThemedButton setActive(boolean active) {
        this.active = active;
        repaint();
        return this;
    }

    // Swing would otherwise take these from the look and feel and ignore our preferred size
    @Override
    public Dimension getMaximumSize() {
        return isMaximumSizeSet() ? super.getMaximumSize() : getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return isMinimumSizeSet() ? super.getMinimumSize() : getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        FontMetrics fm = getFontMetrics(getFont());
        if (kind == Kind.LINK) {
            return new Dimension(fm.stringWidth(getText()) + 16, fm.getHeight() + 12);
        }
        return new Dimension(fm.stringWidth(getText()) + 48, Math.max(44, fm.getHeight() + 24));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        boolean pressed = getModel().isPressed();
        boolean over = hover && isEnabled();

        Color fill = null;
        Color line = null;
        Color text;
        switch (kind) {
            case PRIMARY:
                fill = pressed ? Theme.GOLD_DARK : over ? Theme.GOLD_LIGHT : Theme.GOLD;
                text = Color.BLACK;
                break;
            case DANGER:
                fill = pressed ? new Color(0xB13438) : over ? new Color(0xF0666A) : Theme.DANGER;
                text = Color.WHITE;
                break;
            case SECONDARY:
                fill = new Color(255, 255, 255, pressed ? 40 : over ? 28 : 10);
                line = new Color(255, 255, 255, over ? 110 : 60);
                text = Theme.TEXT;
                break;
            default: // LINK
                text = active ? Theme.GOLD : over ? Theme.GOLD_LIGHT : Theme.MUTED;
                break;
        }
        if (!isEnabled()) {
            text = new Color(text.getRed(), text.getGreen(), text.getBlue(), 110);
            if (fill != null) {
                fill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), Math.min(fill.getAlpha(), 90));
            }
        }

        if (fill != null) {
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 1, h - 1, ARC, ARC);
        }
        if (line != null) {
            g2.setColor(line);
            g2.drawRoundRect(0, 0, w - 1, h - 1, ARC, ARC);
        }
        if (isFocusOwner() && kind != Kind.LINK) {
            g2.setColor(new Color(Theme.GOLD.getRed(), Theme.GOLD.getGreen(), Theme.GOLD.getBlue(), 150));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, ARC, ARC);
        }

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(text);
        g2.drawString(getText(), tx, ty);
        if (kind == Kind.LINK && active) {
            g2.fillRect(tx, h - 5, fm.stringWidth(getText()), 2);
        }
        g2.dispose();
    }
}
