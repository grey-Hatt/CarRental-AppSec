package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A panel that paints an image as its background using "cover" scaling: the
 * image fills the whole panel, keeps its aspect ratio and is centred (so it is
 * never stretched or squashed). An optional colour overlay darkens it.
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

    private final BufferedImage image;
    private final Color overlay;

    public ImagePanel(BufferedImage image, Color overlay) {
        this.image = image;
        this.overlay = overlay;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();
        g2.setColor(Theme.BG);
        g2.fillRect(0, 0, w, h);
        if (image != null && w > 0 && h > 0) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            double scale = Math.max(w / (double) image.getWidth(), h / (double) image.getHeight());
            int dw = (int) Math.ceil(image.getWidth() * scale);
            int dh = (int) Math.ceil(image.getHeight() * scale);
            g2.drawImage(image, (w - dw) / 2, (h - dh) / 2, dw, dh, null);
        }
        if (overlay != null) {
            g2.setColor(overlay);
            g2.fillRect(0, 0, w, h);
        }
        paintOverlay(g2, w, h);
        g2.dispose();
    }

    /** Hook for subclasses that want to paint something (e.g. a gradient) above the image. */
    protected void paintOverlay(Graphics2D g2, int width, int height) {
    }
}
