package ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/** A dark drop-down list that matches {@link ThemedTextField}. */
@SuppressWarnings("serial")
public class ThemedComboBox<E> extends JComboBox<E> {

    public ThemedComboBox(E[] items) {
        super(items);
        setUI(new ThemedComboUI());
        setFont(Theme.font(Font.PLAIN, 14));
        setBackground(Theme.SURFACE_2);
        setForeground(Theme.TEXT);
        setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, false);
                setBackground(isSelected ? Theme.GOLD_DARK : Theme.SURFACE_2);
                setForeground(Theme.TEXT);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return this;
            }
        });
    }

    private static class ThemedComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            BasicArrowButton arrow = new BasicArrowButton(BasicArrowButton.SOUTH, Theme.SURFACE_2, Theme.SURFACE_2,
                    Theme.GOLD, Theme.SURFACE_2);
            arrow.setBorder(BorderFactory.createEmptyBorder());
            return arrow;
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox);
            popup.getList().setBackground(Theme.SURFACE_2);
            popup.getList().setForeground(Theme.TEXT);
            popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
            return popup;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, java.awt.Rectangle bounds, boolean hasFocus) {
            g.setColor(Theme.SURFACE_2);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        public void paintCurrentValue(Graphics g, java.awt.Rectangle bounds, boolean hasFocus) {
            // draw the selected value with the same renderer, but never with the blue "focus" colour
            javax.swing.ListCellRenderer<Object> r = comboBox.getRenderer();
            Component c = r.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
            c.setFont(comboBox.getFont());
            c.setForeground(Theme.TEXT);
            c.setBackground(Theme.SURFACE_2);
            currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, c instanceof JComponent);
        }
    }

    /** Small helper so callers do not need to cast. */
    public String selectedText() {
        Object item = getSelectedItem();
        return item == null ? "" : item.toString();
    }
}
