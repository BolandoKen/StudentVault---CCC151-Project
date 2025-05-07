import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public GUI() {
        this.setSize(1440, 1024);
        this.setTitle("StudentVault");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon studentVaultLogo = new ImageIcon("Assets/StudentVaultLogo.png");
        this.setIconImage(studentVaultLogo.getImage());

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(0x5C2434));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.05;
        SidePanel sidePanel = new SidePanel(this);
        background.add(sidePanel, gbc);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        contentPanel.setBackground(new Color(0xE7E7E7));

        JPanel tableView = new JPanel(new GridBagLayout());
        tableView.setBackground(new Color(0xE7E7E7));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;

        TablePanel tablePanel = new TablePanel();
        
        SearchPanel searchPanel = new SearchPanel();
        searchPanel.setTablePanel(tablePanel);
        
        gbc2.gridy = 0;
        gbc2.weighty = 0.02;
        tableView.add(searchPanel, gbc2);

        JScrollPane tableScrollPane = new JScrollPane(tablePanel);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        tableScrollPane.getViewport().setBackground(new Color(0xE7E7E7));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        gbc2.gridy = 1;
        gbc2.weighty = 0.98;
        tableView.add(tablePanel, gbc2);

        JPanel addStudentPanel = new AddStudent(tablePanel);
        JPanel collegeTablePanel = new CollegeTablePanel();
        JPanel programTablePanel = new ProgramTablePanel();

        contentPanel.add(tableView, "TABLE");
        contentPanel.add(addStudentPanel, "ADD_STUDENT");
        contentPanel.add(collegeTablePanel, "COLLEGETABLEPANEL");
        contentPanel.add(programTablePanel, "PROGRAMTABLEPANEL");

        gbc.gridx = 1;
        gbc.weightx = 0.95;
        background.add(contentPanel, gbc);

        this.add(background);
        this.setVisible(true);
    }

    public void switchPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GUI());
    }
}