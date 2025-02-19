import java.awt.*;
import javax.swing.*;

class RoundedScrollPane extends JScrollPane {
    private int cornerRadius;

    public RoundedScrollPane(JTable table, int radius) {
        super(table);
        this.cornerRadius = radius;
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
        getViewport().setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }
}
