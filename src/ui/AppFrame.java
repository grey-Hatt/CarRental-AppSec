package ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/** Base class for every window: title, icon, dark background and close behaviour in one place. */
@SuppressWarnings("serial")
public class AppFrame extends JFrame {

    /**
     * @param title      window title
     * @param mainWindow true for the windows that represent "the application"
     *                   (closing them exits); false for secondary windows that
     *                   just close themselves
     */
    public AppFrame(String title, boolean mainWindow) {
        super(title);
        setDefaultCloseOperation(mainWindow ? EXIT_ON_CLOSE : DISPOSE_ON_CLOSE);
        BufferedImage icon = Theme.loadImage("/dashboard/logo.png");
        if (icon != null) {
            setIconImage(icon);
        }
        getContentPane().setBackground(Theme.BG);
        setMinimumSize(new Dimension(420, 360));
    }

    public void maximize() {
        setExtendedState(MAXIMIZED_BOTH);
    }
}
