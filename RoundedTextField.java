import java.awt.*;
import javax.swing.*;

class RoundedTextField extends JTextField {
    private int radius;

    public RoundedTextField(int columns, int radius) {
        super(columns);
        this.radius = radius;
        setOpaque(false); // Make background transparent
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g); // Draw text
        g2.dispose();
    }
}
