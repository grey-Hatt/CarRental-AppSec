package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/** A panel with rounded corners and an optional (semi-transparent) fill and border. */
@SuppressWarnings("serial")
public class RoundedPanel extends JPanel {

    private final Color fill;
    private final Color border;
    private final int arc;

    public RoundedPanel(Color fill, Color border, int arc) {
        this.fill = fill;
        this.border = border;
        this.arc = arc;
        setOpaque(false); // we paint our own (possibly translucent) background
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (fill != null) {
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }
        if (border != null) {
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
