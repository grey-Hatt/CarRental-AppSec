package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Central place for the look of the application: colours (black and gold, to
 * match the Golden Auto Store logo), fonts, small component factories and
 * table styling. Every screen uses this class so the UI stays consistent.
 */
public final class Theme {

    private Theme() {
    }

    // ---------------------------------------------------------------- palette
    public static final Color BG = new Color(0x0B0B0C);
    public static final Color SURFACE = new Color(0x151517);
    public static final Color SURFACE_2 = new Color(0x1F1F23);
    public static final Color BORDER = new Color(0x2F2F35);
    public static final Color GOLD = new Color(0xD4AF37);
    public static final Color GOLD_LIGHT = new Color(0xE8C75A);
    public static final Color GOLD_DARK = new Color(0x9C7D1C);
    public static final Color TEXT = new Color(0xF4F4F5);
    public static final Color MUTED = new Color(0xA1A1AA);
    public static final Color DANGER = new Color(0xE5484D);
    public static final Color SUCCESS = new Color(0x3FB950);

    // ------------------------------------------------------------------ fonts
    private static final String FONT_FAMILY = detectFontFamily();

    private static String detectFontFamily() {
        Font segoe = new Font("Segoe UI", Font.PLAIN, 12);
        return "Segoe UI".equals(segoe.getFamily()) ? "Segoe UI" : Font.SANS_SERIF;
    }

    public static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    // ------------------------------------------------------- one-time set-up
    /** Call once, before any window is created. */
    public static void install() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // the default look and feel is fine as a fallback
        }
        UIManager.put("ToolTip.background", SURFACE_2);
        UIManager.put("ToolTip.foreground", TEXT);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(BORDER));
    }

    // ------------------------------------------------------------- resources
    private static final Map<String, BufferedImage> IMAGE_CACHE = new HashMap<>();

    /** Loads an image from the classpath (e.g. "/dashboard/logo.png"); returns null if missing. */
    public static BufferedImage loadImage(String path) {
        if (IMAGE_CACHE.containsKey(path)) {
            return IMAGE_CACHE.get(path);
        }
        BufferedImage image = null;
        try (InputStream in = Theme.class.getResourceAsStream(path)) {
            if (in != null) {
                image = ImageIO.read(in);
            }
        } catch (IOException e) {
            System.err.println("Could not load image " + path + ": " + e.getMessage());
        }
        IMAGE_CACHE.put(path, image);
        return image;
    }

    private static BufferedImage croppedLogo;

    /** The logo without its empty transparent margin (the PNG has a lot of it). */
    private static BufferedImage croppedLogo() {
        if (croppedLogo == null) {
            BufferedImage img = loadImage("/dashboard/logo.png");
            if (img == null) {
                return null;
            }
            int minX = img.getWidth(), minY = img.getHeight(), maxX = -1, maxY = -1;
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    if ((img.getRGB(x, y) >>> 24) > 20) {
                        minX = Math.min(minX, x);
                        maxX = Math.max(maxX, x);
                        minY = Math.min(minY, y);
                        maxY = Math.max(maxY, y);
                    }
                }
            }
            croppedLogo = maxX < 0 ? img : img.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
        return croppedLogo;
    }

    /** The shop logo scaled to the given height, keeping its aspect ratio. */
    public static ImageIcon logo(int height) {
        BufferedImage img = croppedLogo();
        if (img == null) {
            return null;
        }
        int width = Math.round(img.getWidth() * (height / (float) img.getHeight()));
        return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    // ----------------------------------------------------------------- text
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.##", DecimalFormatSymbols.getInstance(Locale.US));

    /** Formats an amount as Pakistani rupees, e.g. "Rs. 6,500". */
    public static String money(double amount) {
        return "Rs. " + MONEY.format(amount);
    }

    public static JLabel label(String text, int style, int size, Color colour) {
        JLabel label = new JLabel(text);
        label.setFont(font(style, size));
        label.setForeground(colour);
        return label;
    }

    public static JLabel heading(String text) {
        return label(text, Font.BOLD, 24, TEXT);
    }

    public static JLabel muted(String text) {
        return label(text, Font.PLAIN, 14, MUTED);
    }

    public static JLabel fieldLabel(String text) {
        return label(text, Font.BOLD, 13, MUTED);
    }

    // ------------------------------------------------------------ components
    public static ThemedButton button(String text) {
        return new ThemedButton(text, ThemedButton.Kind.PRIMARY);
    }

    public static ThemedButton secondaryButton(String text) {
        return new ThemedButton(text, ThemedButton.Kind.SECONDARY);
    }

    public static ThemedButton dangerButton(String text) {
        return new ThemedButton(text, ThemedButton.Kind.DANGER);
    }

    public static ThemedButton linkButton(String text) {
        return new ThemedButton(text, ThemedButton.Kind.LINK);
    }

    public static ThemedTextField textField() {
        return new ThemedTextField(20);
    }

    /** A scroll pane with the dark theme and slim scroll bars. */
    public static JScrollPane scrollPane(Component view) {
        JScrollPane scroll = new JScrollPane(view);
        scroll.setBorder(BorderFactory.createEmptyBorder()); // not null: JTable would install a bevel border
        scroll.getViewport().setBackground(SURFACE);
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel corner = new JPanel();
        corner.setBackground(SURFACE_2);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corner);
        return scroll;
    }

    // ---------------------------------------------------------------- tables
    /** A read-only table model (cells cannot be edited by double clicking). */
    public static DefaultTableModel readOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /** Creates a styled table for the given model. */
    public static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(GOLD_DARK);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? SURFACE : SURFACE_2);
                    c.setForeground(TEXT);
                }
                return c;
            }
        };
        table.setFont(font(Font.PLAIN, 14));
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(SURFACE);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setDefaultRenderer(Object.class, new CellRenderer());

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(100, 42));
        return table;
    }

    /** Sizes every column to fit its header and contents (capped so nothing gets huge). */
    public static void fitColumns(JTable table) {
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn column = table.getColumnModel().getColumn(col);
            Component header = table.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, col);
            int width = header.getPreferredSize().width;
            for (int row = 0; row < table.getRowCount(); row++) {
                Component cell = table.prepareRenderer(table.getCellRenderer(row, col), row, col);
                width = Math.max(width, cell.getPreferredSize().width);
            }
            column.setPreferredWidth(Math.min(width + 8, 320));
        }
    }

    private static boolean isAmountColumn(Object headerText) {
        return "Price".equals(headerText) || "Rent / Day".equals(headerText);
    }

    /** Body cells: padding, and prices (Double values) formatted as money and right aligned. */
    private static class CellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            if (value instanceof Double) {
                cell.setText(money((Double) value));
                cell.setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                cell.setHorizontalAlignment(SwingConstants.LEFT);
            }
            cell.setBorder(new EmptyBorder(0, 14, 0, 14));
            return cell;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel header = (JLabel) super.getTableCellRendererComponent(table, value, false, false, row, column);
            header.setOpaque(true);
            header.setBackground(new Color(0x26262B));
            header.setForeground(GOLD);
            header.setFont(font(Font.BOLD, 13));
            header.setHorizontalAlignment(isAmountColumn(value) ? SwingConstants.RIGHT : SwingConstants.LEFT);
            header.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 2, 0, GOLD_DARK),
                    new EmptyBorder(0, 14, 0, 14)));
            return header;
        }
    }
}
