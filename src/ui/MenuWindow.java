package ui;

import java.awt.Dimension;

/** A small window with a heading and a column of big buttons (admin menu, customer menu). */
@SuppressWarnings("serial")
public class MenuWindow extends CardWindow {

    protected MenuWindow(String windowTitle, String heading, String subtitle) {
        super(windowTitle, false, false, 340);
        addHeader(heading, subtitle);
    }

    protected ThemedButton addAction(String text, Runnable action) {
        ThemedButton button = Theme.button(text);
        button.setPreferredSize(new Dimension(340, 46));
        button.fullWidth();
        button.addActionListener(e -> action.run());
        addToCard(button);
        gap(12);
        return button;
    }

    /** Call after adding all actions. */
    protected void finishMenu() {
        packAndCentre();
    }
}
