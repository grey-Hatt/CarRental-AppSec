package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

/** A simple vertical form: each field gets a small label above it. */
@SuppressWarnings("serial")
public class FormPanel extends JPanel {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private int row = 0;

    public FormPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    /** Adds a label and the given field underneath it, across the full width of the form. */
    public <T extends JComponent> T addField(String label, T field) {
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.insets = new Insets(row == 1 ? 0 : 14, 0, 6, 0);
        add(Theme.fieldLabel(label), gbc);
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(field, gbc);
        return field;
    }

    /** Adds two labelled fields side by side (keeps long forms short). */
    public void addPair(String leftLabel, JComponent left, String rightLabel, JComponent right) {
        gbc.gridwidth = 1;
        int top = row == 0 ? 0 : 14;
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.insets = new Insets(top, 0, 6, 8);
        add(Theme.fieldLabel(leftLabel), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(top, 8, 6, 0);
        add(Theme.fieldLabel(rightLabel), gbc);
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 8);
        add(left, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 8, 0, 0);
        add(right, gbc);
    }
}
