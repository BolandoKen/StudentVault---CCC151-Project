import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    public GUI() {
        this.setSize(1280, 832);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(0x5C2434));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Side Panel
        gbc.gridx = 0;
        gbc.weightx = 0.05;
        background.add(new SidePanel(), gbc);

        // Main Content
        JPanel column2 = new JPanel(new GridBagLayout());
        column2.setBackground(new Color(0xE7E7E7));
        gbc.gridx = 1;
        gbc.weightx = 0.95;
        background.add(column2, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;

        // Search Panel
        gbc2.gridy = 0;
        gbc2.weighty = 0.02;
        column2.add(new SearchPanel(), gbc2);

        // Wrap row2 in a JScrollPane
        JScrollPane scrollPaneRow2 = new JScrollPane(new TablePanel());
        scrollPaneRow2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    // Always show vertical scrollbar
        scrollPaneRow2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        scrollPaneRow2.getViewport().setBackground(new Color(0xE7E7E7)); // Keep background consistent
        scrollPaneRow2.setBorder(BorderFactory.createEmptyBorder());

        // Table Panel (inside a scroll pane)
        gbc2.gridy = 1;
        gbc2.weighty = 0.98;
        column2.add(scrollPaneRow2, gbc2);

        this.add(background);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
