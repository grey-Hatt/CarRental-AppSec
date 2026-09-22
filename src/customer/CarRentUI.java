package customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ui.AppFrame;
import ui.Dialogs;
import ui.ImagePanel;
import ui.NavbarPanel;
import ui.RoundedPanel;
import ui.Theme;
import ui.ThemedButton;

@SuppressWarnings("serial")
public class CarRentUI extends AppFrame {

    private final CardGrid grid = new CardGrid();
    private final JLabel summary = Theme.muted("");
    private List<Car> cars = new ArrayList<>();
    private int columns = 3;

    public CarRentUI() {
        super("Car Rental System - Rent a Car", false);
        maximize();
        setLayout(new BorderLayout());
        add(NavbarPanel.forPage("Rent a Car"), BorderLayout.NORTH);

        ImagePanel background = new ImagePanel(Theme.loadImage("/customer/Rent.png"), new Color(0, 0, 0, 185));
        background.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(26, 40, 6, 40));
        header.add(Theme.label("Choose your car", Font.BOLD, 26, Color.WHITE), BorderLayout.WEST);
        header.add(summary, BorderLayout.EAST);
        background.add(header, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE); // no ghosting over the picture
        scroll.getVerticalScrollBar().setUI(new ui.ModernScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        background.add(scroll, BorderLayout.CENTER);
        add(background, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                loadCars();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int wanted = Math.max(1, Math.min(4, (getWidth() - 80) / 340));
                if (wanted != columns) {
                    columns = wanted;
                    showCars();
                }
            }
        });
    }

    private void loadCars() {
        try {
            cars = CarCatalog.fetchAvailable();
        } catch (SQLException ex) {
            cars = new ArrayList<>();
            Dialogs.dbError(this, ex);
        }
        showCars();
    }

    private void showCars() {
        grid.removeAll();
        summary.setText(cars.size() + (cars.size() == 1 ? " car available" : " cars available"));
        if (cars.isEmpty()) {
            grid.setLayout(new BorderLayout());
            JLabel none = Theme.label("No cars are available right now. Please check back later.", Font.PLAIN, 18,
                    Theme.MUTED);
            none.setHorizontalAlignment(SwingConstants.CENTER);
            none.setBorder(new EmptyBorder(80, 0, 0, 0));
            grid.add(none, BorderLayout.CENTER);
        } else {
            grid.setLayout(new GridLayout(0, columns, 24, 24));
            for (Car car : cars) {
                grid.add(buildCard(car));
            }
        }
        grid.revalidate();
        grid.repaint();
    }

    private JComponent buildCard(Car car) {
        RoundedPanel card = new RoundedPanel(new Color(20, 20, 23, 240), Theme.BORDER, 20);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel name = Theme.label(car.getName(), Font.BOLD, 21, Color.WHITE);
        JLabel meta = Theme.label(car.getBrand().toUpperCase() + "  \u00b7  " + car.getModel(), Font.BOLD, 12,
                Theme.GOLD);

        JPanel divider = new JPanel();
        divider.setBackground(Theme.BORDER);
        divider.setPreferredSize(new Dimension(10, 1));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel price = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        price.setOpaque(false);
        price.add(Theme.label(Theme.money(car.getPrice()), Font.BOLD, 26, Theme.GOLD));
        JLabel perDay = Theme.label("  / day", Font.PLAIN, 14, Theme.MUTED);
        price.add(perDay);

        ThemedButton rent = Theme.button("Rent Now");
        rent.setPreferredSize(new Dimension(100, 44));
        rent.fullWidth();
        rent.addActionListener(e -> new RentDialog(this, car, this::loadCars).setVisible(true));

        for (JComponent c : new JComponent[] { name, meta, divider, price }) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        card.add(name);
        card.add(Box.createVerticalStrut(4));
        card.add(meta);
        card.add(Box.createVerticalStrut(14));
        card.add(divider);
        card.add(Box.createVerticalStrut(14));
        card.add(detail("Engine No", car.getEngineNo()));
        card.add(Box.createVerticalStrut(6));
        card.add(detail("Chassis No", car.getChassisNo()));
        card.add(Box.createVerticalStrut(18));
        card.add(price);
        card.add(Box.createVerticalStrut(18));
        rent.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(rent);
        return card;
    }

    private JComponent detail(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(Theme.label(label, Font.PLAIN, 13, Theme.MUTED), BorderLayout.WEST);
        row.add(Theme.label(value, Font.PLAIN, 13, Theme.TEXT), BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return row;
    }

    private static class CardGrid extends JPanel implements Scrollable {
        CardGrid() {
            setOpaque(false);
            setBorder(new EmptyBorder(16, 40, 40, 40));
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
            return 24;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
            return visible.height - 40;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
