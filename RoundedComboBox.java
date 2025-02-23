import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class RoundedComboBox extends JComboBox<String> {
    private int cornerRadius;
    private Dimension size;
    private boolean showingPlaceholder = true;
    private String placeholder;
    private Color selectionColor;

    public RoundedComboBox(String[] items, Color backgroundColor, Color textColor, Font font, int cornerRadius, Dimension size, String placeholder, Color selectionColor) {
        super();

        this.cornerRadius = cornerRadius;
        this.size = size;
        this.placeholder = placeholder;
        this.selectionColor = selectionColor;

        setUI(new RoundedComboBoxUI(backgroundColor, cornerRadius));
        setBackground(backgroundColor);
        setForeground(textColor);
        setFont(font);
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setPreferredSize(size);

        // Add placeholder as the first item
        addItem(placeholder);
        for (String item : items) {
            addItem(item);
        }
        setSelectedIndex(0); // Set placeholder initially

        // Set custom renderer for selection color
        setRenderer(new PlaceholderRenderer(textColor, selectionColor));
        setEditor(new CustomComboBoxEditor(textColor, backgroundColor));

        setEditable(true); // Enable custom editor for display formatting

        // Remove placeholder when selecting a real value
        addActionListener(e -> {
            if (showingPlaceholder && getSelectedIndex() > 0) {
                showingPlaceholder = false;
                removeItemAt(0); // Remove placeholder
                setEditable(false); // Disable editing after selection
            }
        });
    }

    // Custom renderer to apply selection color
    private class PlaceholderRenderer extends DefaultListCellRenderer {
        private Color selectionColor;
        private Color textColor;

        public PlaceholderRenderer(Color textColor, Color selectionColor) {
            this.textColor = textColor;
            this.selectionColor = selectionColor;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (showingPlaceholder && index == 0) {
                setForeground(Color.GRAY); // Placeholder text color
            } else {
                setForeground(textColor);
            }

            if (isSelected) {
                setBackground(selectionColor); // Custom selection color
                setForeground(Color.WHITE); // White text for contrast
            } else {
                setBackground(Color.WHITE);
            }

            setOpaque(true);
            return this;
        }
    }

    // Custom editor to prevent default blue highlight inside combo box
    private static class CustomComboBoxEditor extends BasicComboBoxEditor {
        private final JTextField editor;

        public CustomComboBoxEditor(Color textColor, Color backgroundColor) {
            editor = new JTextField();
            editor.setBorder(null);
            editor.setForeground(textColor);
            editor.setBackground(backgroundColor);
            editor.setEditable(false); // Prevent text editing but allow selection
        }

        @Override
        public Component getEditorComponent() {
            return editor;
        }

        @Override
        public void setItem(Object item) {
            if (item != null) {
                editor.setText(item.toString());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background with rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}

class RoundedComboBoxUI extends BasicComboBoxUI {
    private final Color backgroundColor;
    private final int cornerRadius;

    public RoundedComboBoxUI(Color backgroundColor, int cornerRadius) {
        this.backgroundColor = backgroundColor;
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton("▼");
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        return button;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background with rounded corners
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), cornerRadius, cornerRadius);

        g2.dispose();
        super.paint(g, c);
    }
}
