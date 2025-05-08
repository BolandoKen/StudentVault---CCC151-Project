import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public final class ProgramTablePanel extends JPanel {
    private JTable programTable;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private CollegeTablePanel collegeTablePanel;

    public ProgramTablePanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

        SearchPanel searchPanel = new SearchPanel();
        searchPanel.setProgramTablePanel(this);

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

        JButton addProgramButton = new JButton("+");
        addProgramButton.setBackground(new Color(0xE7E7E7));
        setForeground(Color.BLACK);
        addProgramButton.setPreferredSize(new Dimension(80, 40));

        addProgramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Find the parent GUI frame to get access to both panels
                GUI parentGUI = findParentGUI();
                if (parentGUI != null) {
                    collegeTablePanel = parentGUI.getCollegeTablePanel();
                }
                
                // If still null, try to find it using the old method
                if (collegeTablePanel == null) {
                    collegeTablePanel = findCollegeTablePanel();
                }
                
                // Get the parent frame and pass both table panel instances
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ProgramTablePanel.this);
                CollegeAdminDialog adminDialog = new CollegeAdminDialog(frame, null, collegeTablePanel, ProgramTablePanel.this);
                adminDialog.setVisible(true);
                
                // Refresh both tables after closing the dialog
                refreshProgramTable();
                
                if (collegeTablePanel != null) {
                    collegeTablePanel.refreshCollegeTable();
                }
            }
        });

        buttonsPanel.add(addProgramButton);

        JLabel programsVaultText = new JLabel("Programs");
        programsVaultText.setFont(new Font("Helvetica", Font.BOLD, 32));
        leftPanel.add(programsVaultText, BorderLayout.CENTER);
        
        JLabel sortbytext = new JLabel("Sort by: ");
        sortbytext.setFont(new Font("Helvetica", Font.PLAIN, 16));
        sortbytext.setForeground(new Color(0x7E7E7E));
        sortPanel.add(sortbytext);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false); // Changed from RED to transparent
        gbc.gridy = 2;
        gbc.weighty = 0.8;
        this.add(bottomRow, gbc);

        programTable = new JTable();
        programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Initialize the table with data
        ProgramTableUtility.populateProgramTable(programTable);

        JScrollPane scrollPane = new JScrollPane(programTable);
        bottomRow.add(scrollPane, BorderLayout.CENTER);
    }
    
    // Helper method to find the parent GUI to access other panels
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
    
    // Helper method to find the CollegeTablePanel in the same application (legacy method)
    private CollegeTablePanel findCollegeTablePanel() {
        // Get the root container (JFrame or similar)
        Container root = SwingUtilities.getWindowAncestor(this);
        if (root != null) {
            // Search through all components recursively
            return findCollegeTablePanelInContainer(root);
        }
        return null;
    }
    
    private CollegeTablePanel findCollegeTablePanelInContainer(Container container) {
        // Check all components in this container
        for (Component comp : container.getComponents()) {
            // If this is a CollegeTablePanel, return it
            if (comp instanceof CollegeTablePanel) {
                return (CollegeTablePanel) comp;
            }
            
            // If this is another container, search inside it
            if (comp instanceof Container) {
                CollegeTablePanel found = findCollegeTablePanelInContainer((Container) comp);
                if (found != null) {
                    return found;
                }
            }
        }
        
        // If no CollegeTablePanel is found in this container
        return null;
    }
    
    // Method to refresh the program table
    public void refreshProgramTable() {
        ProgramTableUtility.populateProgramTable(programTable);
        programTable.revalidate();
        programTable.repaint();
        System.out.println("Program table refreshed");
    }
    
    // Method to set the reference to the CollegeTablePanel
    public void setCollegeTablePanel(CollegeTablePanel collegeTablePanel) {
        this.collegeTablePanel = collegeTablePanel;
    }
    
    // Method to notify CollegeTablePanel of changes
    public void notifyCollegeTableOfChanges() {
        if (collegeTablePanel != null) {
            collegeTablePanel.refreshCollegeTable();
        } else {
            // Try to find the panel if not already set
            GUI parentGUI = findParentGUI();
            if (parentGUI != null) {
                collegeTablePanel = parentGUI.getCollegeTablePanel();
                if (collegeTablePanel != null) {
                    collegeTablePanel.refreshCollegeTable();
                }
            }
        }
    }
}