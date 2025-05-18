import java.awt.*;
import javax.swing.*;

public class CRoundedPanel extends JPanel {
    private int cornerRadius;

    public CRoundedPanel(int radius) {
        this.cornerRadius = radius;
        setOpaque(false); // Ensures transparency outside the rounded area
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the background color of the rounded panel
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
    }
}
