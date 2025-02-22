import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

class RoundedTextField extends JTextField {
    private int radius;
    private String placeholder;
    private boolean showingPlaceholder;
    private boolean hasBorder; // New flag to enable/disable border

    public RoundedTextField(int columns, int radius, String placeholder, boolean hasBorder) {
        super(columns);
        this.radius = radius;
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        this.hasBorder = hasBorder;

        setOpaque(false); // Make background transparent
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        setForeground(Color.GRAY);
        setText(placeholder);

        // Placeholder logic using FocusListener
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(Color.BLACK);
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    showingPlaceholder = true;
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color with rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g); // Draw text
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (hasBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.LIGHT_GRAY); // Border color
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
        }
    }
}
