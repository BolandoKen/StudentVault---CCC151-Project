import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public final class CollegeTablePanel extends JPanel {
    private JTable collegeTable;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private ProgramTablePanel programTablePanel;

    public CollegeTablePanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

        SearchPanel searchPanel = new SearchPanel();
        searchPanel.setCollegeTablePanel(this);

        searchPanelContainer.add(searchPanel, BorderLayout.CENTER);
        this.add(searchPanelContainer, gbc);

        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(1, 100));
        gbc.gridy = 1;
        gbc.weighty = 0.2;
        this.add(topRow, gbc);

        GridBagConstraints gbcTopRow = new GridBagConstraints();
        gbcTopRow.fill = GridBagConstraints.BOTH;
        gbcTopRow.gridy = 0; 
        gbcTopRow.weightx = 0.5;
        gbcTopRow.weighty = 1.0; 
        gbcTopRow.anchor = GridBagConstraints.SOUTH; 

        gbcTopRow.gridx = 0;
        gbcTopRow.weightx = 0.5;
        gbcTopRow.anchor = GridBagConstraints.WEST; 

        //setup TopLeftPanel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false); // Changed from GREEN to transparent
        topRow.add(leftPanel, gbcTopRow);

        final JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sortPanel.setOpaque(false); // Changed from BLUE to transparent
        leftPanel.add(sortPanel, BorderLayout.SOUTH);

        gbcTopRow.gridx = 1;
        gbcTopRow.weightx = 0.5; 
        gbcTopRow.anchor = GridBagConstraints.EAST; 

        //setup TopRightPanel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false); // Changed from BLUE to transparent
        topRow.add(rightPanel, gbcTopRow);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, -5));
        buttonsPanel.setOpaque(false);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JButton addCollegeButton = new JButton("+");
        addCollegeButton.setBackground(new Color(0xE7E7E7));
        setForeground(Color.BLACK);
        addCollegeButton.setPreferredSize(new Dimension(80, 40));

        addCollegeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Find the parent GUI frame to get access to both panels
                GUI parentGUI = findParentGUI();
                if (parentGUI != null) {
                    programTablePanel = parentGUI.getProgramTablePanel();
                }
                
                // Get the parent frame and pass the CollegeTablePanel instance
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(CollegeTablePanel.this);
                CollegeAdminDialog adminDialog = new CollegeAdminDialog(frame, null, CollegeTablePanel.this, programTablePanel);
                adminDialog.setVisible(true);
                
                // Refresh the table after closing the dialog
                refreshCollegeTable();
                
                // Also refresh program table if changes affect it
                if (programTablePanel != null) {
                    programTablePanel.refreshProgramTable();
                }
            }
        });

        buttonsPanel.add(addCollegeButton);

        JLabel collegeVaultText = new JLabel("Colleges");
        collegeVaultText.setFont(new Font("Helvetica", Font.BOLD, 32));
        leftPanel.add(collegeVaultText, BorderLayout.CENTER);

        JLabel sortbytext = new JLabel("Sort by: ");
        sortbytext.setFont(new Font("Helvetica", Font.PLAIN, 16));
        sortbytext.setForeground(new Color(0x7E7E7E));
        sortPanel.add(sortbytext);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false); // Changed from RED to transparent
        gbc.gridy = 2;
        gbc.weighty = 0.8;
        this.add(bottomRow, gbc);

        collegeTable = new JTable();
        collegeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Initialize the table with data
        CollegeTableUtility.populateCollegeTable(collegeTable);

        JScrollPane scrollPane = new JScrollPane(collegeTable);
        bottomRow.add(scrollPane, BorderLayout.CENTER);
    }
    
    public void refreshCollegeTable() {
        CollegeTableUtility.populateCollegeTable(collegeTable);
        collegeTable.revalidate();
        collegeTable.repaint();
        System.out.println("College table refreshed");
    }
    
    // Method to find the parent GUI frame to access other panels
    private GUI findParentGUI() {
        Container parent = this.getParent();
        while (parent != null) {
            if (parent instanceof GUI) {
                return (GUI) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
    
    // Method to set the reference to the ProgramTablePanel
    public void setProgramTablePanel(ProgramTablePanel programTablePanel) {
        this.programTablePanel = programTablePanel;
    }
    
    // Method to notify ProgramTablePanel of changes
    public void notifyProgramTableOfChanges() {
        if (programTablePanel != null) {
            programTablePanel.refreshProgramTable();
        } else {
            // Try to find the panel if not already set
            GUI parentGUI = findParentGUI();
            if (parentGUI != null) {
                programTablePanel = parentGUI.getProgramTablePanel();
                if (programTablePanel != null) {
                    programTablePanel.refreshProgramTable();
                }
            }
        }
    }
    public JTable getCollegeTable() {
        return collegeTable;
    }
}