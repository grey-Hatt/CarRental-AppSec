package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * A full-screen window that shows database rows in a table. Subclasses (or
 * callers) only say which columns to show and how to load the rows, so the
 * available cars, rented cars and history screens all share this code.
 */
@SuppressWarnings("serial")
public class TableFrame extends AppFrame {

    /** Loads the rows to display. May throw SQLException; the frame reports it to the user. */
    public interface RowLoader {
        List<Object[]> load() throws SQLException;
    }

    private static final String TABLE = "table";
    private static final String EMPTY = "empty";

    private final DefaultTableModel model;
    private final JTable table;
    private final RowLoader loader;
    private final JLabel countLabel = Theme.muted("");
    private final CardLayout cards = new CardLayout();
    private final JPanel cardArea = new JPanel(cards);

    public TableFrame(String title, String[] columns, RowLoader loader, String emptyMessage) {
        super("Car Rental System - " + title, false);
        this.loader = loader;
        maximize();
        setLayout(new BorderLayout());
        add(NavbarPanel.forPage(title), BorderLayout.NORTH);

        model = Theme.readOnlyModel(columns);
        table = Theme.createTable(model);

        cardArea.setBackground(Theme.SURFACE);
        cardArea.add(Theme.scrollPane(table), TABLE);
        JLabel empty = Theme.label(emptyMessage, Font.PLAIN, 16, Theme.MUTED);
        empty.setHorizontalAlignment(SwingConstants.CENTER);
        cardArea.add(empty, EMPTY);

        RoundedPanel tableCard = new RoundedPanel(Theme.SURFACE, Theme.BORDER, 18);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(2, 2, 2, 2));
        tableCard.add(cardArea, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14, 4, 0, 4));
        footer.add(countLabel, BorderLayout.WEST);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        ThemedButton refresh = Theme.secondaryButton("Refresh");
        refresh.setPreferredSize(new Dimension(110, 38));
        refresh.addActionListener(e -> refresh());
        ThemedButton close = Theme.secondaryButton("Close");
        close.setPreferredSize(new Dimension(110, 38));
        close.addActionListener(e -> dispose());
        buttons.add(refresh);
        buttons.add(close);
        footer.add(buttons, BorderLayout.EAST);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(new Color(0x101012));
        body.setBorder(new EmptyBorder(24, 32, 20, 32));
        body.add(tableCard, BorderLayout.CENTER);
        body.add(footer, BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        // load the data once the window is on screen, so a database error dialog appears over a visible window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                refresh();
            }
        });
    }

    /** Reloads the rows from the database. */
    public void refresh() {
        try {
            List<Object[]> rows = loader.load();
            model.setRowCount(0);
            for (Object[] row : rows) {
                model.addRow(row);
            }
            Theme.fitColumns(table);
            countLabel.setText(rows.size() + (rows.size() == 1 ? " record" : " records"));
            cards.show(cardArea, rows.isEmpty() ? EMPTY : TABLE);
        } catch (SQLException ex) {
            model.setRowCount(0);
            countLabel.setText("Could not load data");
            cards.show(cardArea, EMPTY);
            Dialogs.dbError(this, ex);
        }
    }
}
