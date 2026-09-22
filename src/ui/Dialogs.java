package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Themed replacements for JOptionPane, so message boxes match the rest of the
 * application instead of showing the default grey Swing look.
 */
public final class Dialogs {

    public enum Kind {
        INFO("i", Theme.GOLD), SUCCESS("\u2713", Theme.SUCCESS), WARNING("!", Theme.GOLD_LIGHT), ERROR("\u00d7", Theme.DANGER);

        private final String glyph;
        private final Color colour;

        Kind(String glyph, Color colour) {
            this.glyph = glyph;
            this.colour = colour;
        }
    }

    private static final String APP_TITLE = "Car Rental System";
    private static final int TEXT_WIDTH = 340;

    private Dialogs() {
    }

    // --------------------------------------------------------- simple messages
    public static void info(Component parent, String message) {
        show(parent, APP_TITLE, message, Kind.INFO);
    }

    public static void success(Component parent, String message) {
        show(parent, APP_TITLE, message, Kind.SUCCESS);
    }

    public static void warning(Component parent, String message) {
        show(parent, APP_TITLE, message, Kind.WARNING);
    }

    public static void error(Component parent, String message) {
        show(parent, APP_TITLE, message, Kind.ERROR);
    }

    /** Shows a friendly message for a database problem (details go to the console only). */
    public static void dbError(Component parent, SQLException ex) {
        ex.printStackTrace();
        String state = ex.getSQLState();
        if (state != null && state.startsWith("23")) {
            error(parent, "This change conflicts with existing data, for example a Reg ID that is already used "
                    + "or a car that already has rental history.");
        } else {
            error(parent, "Could not complete the database operation.\n\nMake sure MySQL is running, the "
                    + "'car_rental' database exists (see database/schema.sql) and the MySQL JDBC driver is in the lib folder.");
        }
    }

    public static void show(Component parent, String title, String message, Kind kind) {
        JDialog dialog = createDialog(parent, title);
        ThemedButton ok = Theme.button("OK");
        ok.addActionListener(e -> dialog.dispose());
        build(dialog, kind, message, null, new ThemedButton[] { ok });
        dialog.getRootPane().setDefaultButton(ok);
        display(dialog, parent);
    }

    // ------------------------------------------------------------ confirmation
    public static boolean confirm(Component parent, String title, String message, String confirmText, boolean dangerous) {
        final boolean[] result = { false };
        JDialog dialog = createDialog(parent, title);
        ThemedButton yes = dangerous ? Theme.dangerButton(confirmText) : Theme.button(confirmText);
        ThemedButton no = Theme.secondaryButton("Cancel");
        yes.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        no.addActionListener(e -> dialog.dispose());
        build(dialog, dangerous ? Kind.WARNING : Kind.INFO, message, null, new ThemedButton[] { no, yes });
        dialog.getRootPane().setDefaultButton(no);
        display(dialog, parent);
        return result[0];
    }

    // ------------------------------------------------------------------ prompt
    /** Asks for one line of text. Returns null if cancelled. */
    public static String prompt(Component parent, String title, String message, String placeholder) {
        final String[] result = { null };
        JDialog dialog = createDialog(parent, title);
        ThemedTextField field = new ThemedTextField(20).placeholder(placeholder);
        ThemedButton ok = Theme.button("OK");
        ThemedButton cancel = Theme.secondaryButton("Cancel");
        ok.addActionListener(e -> {
            result[0] = field.getText();
            dialog.dispose();
        });
        cancel.addActionListener(e -> dialog.dispose());
        build(dialog, Kind.INFO, message, field, new ThemedButton[] { cancel, ok });
        dialog.getRootPane().setDefaultButton(ok);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                field.requestFocusInWindow();
            }
        });
        display(dialog, parent);
        return result[0];
    }

    // --------------------------------------------------------------- internals
    private static JDialog createDialog(Component parent, String title) {
        Window owner = parent instanceof Window ? (Window) parent
                : parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Theme.SURFACE);
        dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        return dialog;
    }

    private static void build(JDialog dialog, Kind kind, String message, JComponent extra, ThemedButton[] buttons) {
        JPanel root = new JPanel(new BorderLayout(20, 0));
        root.setBackground(Theme.SURFACE);
        root.setBorder(new EmptyBorder(26, 28, 20, 28));

        root.add(new Badge(kind), BorderLayout.WEST);

        JPanel centre = new JPanel(new BorderLayout(0, 14));
        centre.setOpaque(false);
        centre.add(messageArea(message), BorderLayout.CENTER);
        if (extra != null) {
            centre.add(extra, BorderLayout.SOUTH);
        }
        root.add(centre, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setBorder(new EmptyBorder(22, 0, 0, 0));
        for (ThemedButton b : buttons) {
            b.setPreferredSize(new Dimension(Math.max(96, b.getPreferredSize().width), 40));
            buttonRow.add(b);
        }
        root.add(buttonRow, BorderLayout.SOUTH);

        dialog.setContentPane(root);
    }

    private static JTextArea messageArea(String message) {
        JTextArea area = new JTextArea(message);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(Theme.font(Font.PLAIN, 14));
        area.setForeground(Theme.TEXT);
        area.setBorder(null);
        area.setSize(new Dimension(TEXT_WIDTH, 1)); // lets the text area work out how tall the wrapped text is
        Dimension wrapped = area.getPreferredSize();
        area.setPreferredSize(new Dimension(TEXT_WIDTH, Math.max(wrapped.height, 44)));
        return area;
    }

    private static void display(JDialog dialog, Component parent) {
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /** The round coloured icon on the left of a dialog. */
    private static class Badge extends JComponent {
        private static final long serialVersionUID = 1L;
        private final Kind kind;

        Badge(Kind kind) {
            this.kind = kind;
            setPreferredSize(new Dimension(46, 46));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(new Color(kind.colour.getRed(), kind.colour.getGreen(), kind.colour.getBlue(), 45));
            g2.fillOval(0, 0, 45, 45);
            g2.setColor(kind.colour);
            g2.drawOval(0, 0, 45, 45);
            g2.setFont(Theme.font(Font.BOLD, 24));
            FontMetrics fm = g2.getFontMetrics();
            int x = (46 - fm.stringWidth(kind.glyph)) / 2;
            int y = (46 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(kind.glyph, x, y);
            g2.dispose();
        }
    }
}
