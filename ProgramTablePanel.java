import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public final class ProgramTablePanel extends JPanel {
    private JTable programTable;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private JButton editbutton;
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
        searchPanel.setFilterButtonAction(e -> {
            ProgramsFilterDialog dialog = new ProgramsFilterDialog(
                SwingUtilities.getWindowAncestor(ProgramTablePanel.this), 
                ProgramTablePanel.this
            );
            dialog.setVisible(true);
        });
        

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
        
        deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(0xE7E7E7));
        deleteButton.setForeground(Color.BLACK);
        deleteButton.addActionListener(e -> removeProgram());

        editbutton = new JButton("Edit");
        editbutton.setBackground(new Color(0xE7E7E7));
        editbutton.setForeground(Color.BLACK);
        editbutton.addActionListener(e -> editProgram());
        buttonsPanel.add(addProgramButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editbutton);

        JLabel programsVaultText = new JLabel("Programs");
        programsVaultText.setFont(new Font("Helvetica", Font.BOLD, 32));
        leftPanel.add(programsVaultText, BorderLayout.CENTER);
        
        JLabel sortbytext = new JLabel("");
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
    public JTable getProgramTable() {
        return programTable;
    }
    private void removeProgram() {
    int selectedRow = programTable.getSelectedRow();
    
    if (selectedRow >= 0) {
        // Convert view row to model row (important for sorted/filtered tables)
        int modelRow = programTable.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) programTable.getModel();
        
        // Get data from selected row
        String collegeCode = (String) model.getValueAt(modelRow, 0);
        String programName = (String) model.getValueAt(modelRow, 1);
        String programCode = (String) model.getValueAt(modelRow, 2);
        
        // Find full college name by abbreviation
        String collegeName = CollegeManager.getCollegeNameByAbbreviation(collegeCode);
        
        if (collegeName != null) {
            // Confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html>Confirm removal of:<br><b>" + programName + "</b> (" + programCode + ")<br>" +
                "from <b>" + collegeName + "</b> (" + collegeCode + ")?</html>",
                "Confirm Program Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Attempt removal
                    if (CollegeManager.removeProgram(collegeName, programName)) {
                        // Remove from table model
                        model.removeRow(modelRow);
                        
                        // Notify other components
                        notifyCollegeTableOfChanges();
                        
                        // Success message
                        JOptionPane.showMessageDialog(
                            this,
                            "Program removed successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        throw new Exception("CollegeManager failed to remove the program");
                    }
                } catch (Exception e) {
                    // Error message
                    JOptionPane.showMessageDialog(
                        this,
                        "Error removing program: " + e.getMessage(),
                        "Removal Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    // Fallback refresh
                    refreshProgramTable();
                }
            }
        } else {
            // College not found error
            JOptionPane.showMessageDialog(
                this,
                "Could not find college with code: " + collegeCode,
                "Invalid College",
                JOptionPane.ERROR_MESSAGE
            );
        }
    } else {
        // No selection warning
        JOptionPane.showMessageDialog(
            this,
            "Please select a program to remove first",
            "No Selection",
            JOptionPane.WARNING_MESSAGE
        );
    }
}

private void editProgram() {
    int selectedRow = programTable.getSelectedRow();
    
    if (selectedRow >= 0) {
        int modelRow = programTable.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) programTable.getModel();
        
        // Get current values
        String collegeCode = (String) model.getValueAt(modelRow, 0);
        String currentProgramName = (String) model.getValueAt(modelRow, 1);
        String currentProgramCode = (String) model.getValueAt(modelRow, 2);
        
        // Find full college name by abbreviation
        String collegeName = CollegeManager.getCollegeNameByAbbreviation(collegeCode);
        
        if (collegeName != null) {
            // Create edit dialog
            JPanel editPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            JTextField programNameField = new JTextField(currentProgramName);
            JTextField programCodeField = new JTextField(currentProgramCode);
            
            editPanel.add(new JLabel("Program Name:"));
            editPanel.add(programNameField);
            editPanel.add(new JLabel("Program Abbreviation:"));
            editPanel.add(programCodeField);
            editPanel.add(new JLabel("College:"));
            editPanel.add(new JLabel(collegeName + " (" + collegeCode + ")"));
            
            int result = JOptionPane.showConfirmDialog(
                this,
                editPanel,
                "Edit Program",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String newProgramName = programNameField.getText().trim();
                String newProgramCode = programCodeField.getText().trim();
                
                // Validate input
                if (newProgramName.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Program name cannot be empty",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                try {
                    // Attempt update
                    if (CollegeManager.updateProgramName(
                        collegeName,
                        currentProgramName,
                        newProgramName,
                        newProgramCode
                    )) {
                        // Update table model
                        model.setValueAt(newProgramName, modelRow, 1);
                        model.setValueAt(newProgramCode, modelRow, 2);
                        
                        // Notify other components
                        notifyCollegeTableOfChanges();
                        
                        // Success message
                        JOptionPane.showMessageDialog(
                            this,
                            "Program updated successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        throw new Exception("Failed to update program (possibly duplicate name)");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error updating program: " + e.getMessage(),
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    // Fallback refresh
                    refreshProgramTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Could not find college with code: " + collegeCode,
                "Invalid College",
                JOptionPane.ERROR_MESSAGE
            );
        }
    } else {
        JOptionPane.showMessageDialog(
            this,
            "Please select a program to edit first",
            "No Selection",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
}