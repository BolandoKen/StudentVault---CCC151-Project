import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

class RoundedTextField extends JTextField {
    private int radius;
    private String placeholder;
    private boolean showingPlaceholder;
    private boolean hasBorder;
    private Color backgroundColor;

    // Constructor with Font, Hex Color, and Other Customizations
    public RoundedTextField(int columns, int radius, String placeholder, boolean hasBorder, String hexColor, String fontName, int fontStyle, int fontSize) {
        super(columns);
        this.radius = radius;
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        this.hasBorder = hasBorder;
        this.backgroundColor = Color.decode(hexColor); // Convert HEX to Color

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        setForeground(Color.GRAY);
        setText(placeholder);
        setFont(new Font(fontName, fontStyle, fontSize)); // Set custom font

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

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (hasBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
        }
    }
}
