import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

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
        SidePanel sidePanel = new SidePanel(this); // Pass reference of GUI
        background.add(sidePanel, gbc);

        // Main Content (CardLayout Container)
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(new Color(0xE7E7E7));

        // === Table View (Search Panel + TablePanel) ===
        JPanel tableView = new JPanel(new GridBagLayout());
        tableView.setBackground(new Color(0xE7E7E7));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;

        // Search Panel (Stays on Top)
        gbc2.gridy = 0;
        gbc2.weighty = 0.02;
        tableView.add(new SearchPanel(), gbc2);

        // Table Panel (Inside a Scroll Pane)
        TablePanel tablePanel = new TablePanel();
        JScrollPane tableScrollPane = new JScrollPane(tablePanel);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(20); // Default speed
        tableScrollPane.getViewport().setBackground(new Color(0xE7E7E7));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        gbc2.gridy = 1;
        gbc2.weighty = 0.98;
        tableView.add(tableScrollPane, gbc2);

        // === Add Student Panel ===
        JPanel addStudentPanel = new AddStudent(tablePanel);

        // Add both panels to CardLayout
        contentPanel.add(tableView, "TABLE");
        contentPanel.add(addStudentPanel, "ADD_STUDENT");

        // Add contentPanel to GUI
        gbc.gridx = 1;
        gbc.weightx = 0.95;
        background.add(contentPanel, gbc);

        this.add(background);
        this.setVisible(true);
    }

    // Function to switch panels
    public void switchPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    public static void main(String[] args) {
    }
}
