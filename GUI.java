import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final TablePanel tablePanel;
    private final CollegeTablePanel collegeTablePanel;
    private final ProgramTablePanel programTablePanel;
    
    public GUI() {
        // Frame settings
        setTitle("Student Vault");
        setSize(1440, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set icon
        ImageIcon studentVaultLogo = new ImageIcon("Assets/StudentVaultLogo.png");
        setIconImage(studentVaultLogo.getImage());
        
        // Main background panel
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(0x5C2434));
        
        // Card layout setup
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(0xE7E7E7));
        
        // Create table panels
        tablePanel = new TablePanel();
        collegeTablePanel = new CollegeTablePanel();
        programTablePanel = new ProgramTablePanel();
        
        // Create the main table view with search panel
        JPanel tableView = new JPanel(new GridBagLayout());
        tableView.setBackground(new Color(0xE7E7E7));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        
        // Add search panel
        SearchPanel searchPanel = new SearchPanel();
        searchPanel.setTablePanel(tablePanel);
        
        gbc.gridy = 0;
        gbc.weighty = 0.02;
        tableView.add(searchPanel, gbc);
        
        // Add table with scroll pane
        JScrollPane tableScrollPane = new JScrollPane(tablePanel);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        tableScrollPane.getViewport().setBackground(new Color(0xE7E7E7));
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        gbc.gridy = 1;
        gbc.weighty = 0.98;
        tableView.add(tableScrollPane, gbc);
        
        // Create add student panel
        JPanel addStudentPanel = new AddStudent(tablePanel);
        
        // Add all panels to card layout
        cardPanel.add(tableView, "TABLE");
        cardPanel.add(addStudentPanel, "ADD_STUDENT");
        cardPanel.add(collegeTablePanel, "COLLEGETABLEPANEL");
        cardPanel.add(programTablePanel, "PROGRAMTABLEPANEL");
        
        // Create side panel
        SidePanel sidePanel = new SidePanel(this);
        
        // Layout setup
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.weighty = 1.0;
        
        mainGbc.gridx = 0;
        mainGbc.weightx = 0.05;
        background.add(sidePanel, mainGbc);
        
        mainGbc.gridx = 1;
        mainGbc.weightx = 0.95;
        background.add(cardPanel, mainGbc);
        
        add(background);
        setVisible(true);
    }
    
    public void switchPanel(String panelName) {
        // Refresh data before showing the panel if needed
        switch (panelName) {
            case "TABLE":
                tablePanel.refreshTable();
                break;
            case "COLLEGETABLEPANEL":
                collegeTablePanel.refreshCollegeTable();
                break;
            case "PROGRAMTABLEPANEL":
                programTablePanel.refreshProgramTable();
                // Refresh the filter dialog when switching to program panel
                ProgramsFilterDialog filterDialog = programTablePanel.getFilterDialog();
                if (filterDialog != null && filterDialog.isVisible()) {
                    filterDialog.refreshCollegeList();
                }
                break;
        }
        cardLayout.show(cardPanel, panelName);
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GUI());
    }
    
    // Getter methods for accessing panels if needed
    public CollegeTablePanel getCollegeTablePanel() {
        return collegeTablePanel;
    }
    
    public ProgramTablePanel getProgramTablePanel() {
        return programTablePanel;
    }
    
    public TablePanel getTablePanel() {
        return tablePanel;
    }
    public ProgramsFilterDialog getProgramsFilterDialog() {
        if (programTablePanel != null) {
            return programTablePanel.getFilterDialog(); // You'll need to add this getter
        }
        return null;
    }
    
}