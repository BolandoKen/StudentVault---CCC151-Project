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

        // 🔹 First column panel (Sidebar)
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(0x5C2434));
        gbc.gridx = 0;
        gbc.weightx = 0.05; // Narrow sidebar
        background.add(sidePanel, gbc);

        // 🔹 Second column (Main Content)
        JPanel column2 = new JPanel(new GridBagLayout());
        column2.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        background.add(column2, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;

        // 🔹 First row inside column2 (Search bar area)
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setBackground(new Color(0xFF5733));
        gbc2.gridy = 0;
        gbc2.weighty = 0.1;
        column2.add(row1, gbc2);

        // ✅ Improved JTextField with Proper Sizing
        JTextField searchField = new RoundedTextField(20, 25); // 20 columns, 25px corner radius
        searchField.setPreferredSize(new Dimension(400, 40)); // Width: 400px, Height: 40px
        searchField.setBackground(new Color(0xE7E7E7));
        searchField.setFont(new Font("Helvetica", Font.PLAIN, 18));
        
        // 🔹 Search Functionality on Enter Key
        searchField.addActionListener(e -> {
            String query = searchField.getText();
            System.out.println("Searching for: " + query);
        });

        row1.add(searchField); // Add text field to row1

        // 🔹 Second row inside column2 (Main content area)
        JPanel row2 = new JPanel();
        row2.setBackground(Color.WHITE);
        gbc2.gridy = 1;
        gbc2.weighty = 0.9;
        column2.add(row2, gbc2);

        this.add(background);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
