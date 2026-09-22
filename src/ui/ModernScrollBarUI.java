package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/** A slim dark scroll bar with a gold thumb and no arrow buttons. */
public class ModernScrollBarUI extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        thumbColor = Theme.GOLD_DARK;
        trackColor = Theme.SURFACE;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return emptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return emptyButton();
    }

    private JButton emptyButton() {
        JButton button = new JButton();
        Dimension zero = new Dimension(0, 0);
        button.setPreferredSize(zero);
        button.setMinimumSize(zero);
        button.setMaximumSize(zero);
        return button;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(12, 12);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
        g.setColor(Theme.SURFACE);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle bounds) {
        if (bounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isThumbRollover() || isDragging ? Theme.GOLD : Theme.GOLD_DARK);
        g2.fillRoundRect(bounds.x + 2, bounds.y + 2, bounds.width - 4, bounds.height - 4, 8, 8);
        g2.dispose();
    }
}
