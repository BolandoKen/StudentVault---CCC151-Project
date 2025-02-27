import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class RoundedButton extends JButton {
    private final int cornerRadius;
    private Color backgroundColor;
    private Color hoverColor;
    private Color originalColor;
    private Color textColor;
    private Font textFont;

    public RoundedButton(String text, Color backgroundColor, Color textColor, Font font, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        this.originalColor = backgroundColor;
        this.backgroundColor = new Color(backgroundColor.getRGB()); // Create a copy to prevent issues
        this.hoverColor = backgroundColor.darker();
        this.textColor = textColor;
        this.textFont = font;

        setFont(font);
        setForeground(textColor);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        // Add hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(originalColor);
            }
        });
    }

    public void setBackground(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }
}
