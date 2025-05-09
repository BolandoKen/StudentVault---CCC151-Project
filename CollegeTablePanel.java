import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public final class CollegeTablePanel extends JPanel {
    private JTable collegeTable;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private JButton editButton;
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
        searchPanel.setFilterButtonAction(e -> {
            JOptionPane.showMessageDialog(this, "No available filters", "Filter Info", JOptionPane.INFORMATION_MESSAGE);
        });

        searchPanelContainer.add(searchPanel, BorderLayout.CENTER);
        this.add(searchPanelContainer, gbc);

        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(1, 100));
        gbc.gridy = 1;
        gbc.weighty = 0.1;
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
        leftPanel.setOpaque(false); 
        topRow.add(leftPanel, gbcTopRow);

        gbcTopRow.gridx = 1;
        gbcTopRow.weightx = 0.5; 
        gbcTopRow.anchor = GridBagConstraints.EAST; 

        //setup TopRightPanel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        topRow.add(rightPanel, gbcTopRow);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        buttonsPanel.setOpaque(false);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JButton addCollegeButton = new JButton(new ImageIcon("Assets/PlusIcon.png"));
        addCollegeButton.setBorderPainted(false);
        addCollegeButton.setFocusPainted(false);
        addCollegeButton.setContentAreaFilled(false);
        addCollegeButton.setBackground(new Color(0xE7E7E7));

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
                CollegeAdminDialog adminDialog = new CollegeAdminDialog(frame, null, CollegeTablePanel.this, programTablePanel, null);
                adminDialog.setVisible(true);
                
                // Refresh the table after closing the dialog
                refreshCollegeTable();
                
                // Also refresh program table if changes affect it
                if (programTablePanel != null) {
                    programTablePanel.refreshProgramTable();
                }
            }
        });
 
        deleteButton = new JButton(new ImageIcon("Assets/DeleteIcon.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.addActionListener(e -> removeCollege());

        editButton = new JButton(new ImageIcon("Assets/EditIcon.png"));
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.addActionListener(e -> editCollege());
        buttonsPanel.add(addCollegeButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);

        JLabel collegeText = new JLabel("Colleges");
        collegeText.setFont(new Font("Helvetica", Font.BOLD, 32));
        JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textContainer.setOpaque(false);

        textContainer.add(collegeText);
        leftPanel.add(textContainer, BorderLayout.SOUTH);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false); // Changed from RED to transparent
        gbc.gridy = 2;
        gbc.weighty = 0.9;
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
    private void removeCollege() {
        int selectedRow = collegeTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            // Convert view row to model row (important for sorted/filtered tables)
            int modelRow = collegeTable.convertRowIndexToModel(selectedRow);
            DefaultTableModel model = (DefaultTableModel) collegeTable.getModel();
            
            // Get data from selected row
            String collegeName = (String) model.getValueAt(modelRow, 0);
            String collegeCode = (String) model.getValueAt(modelRow, 1);
            
            // Confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html>Confirm removal of college:<br><b>" + collegeName + "</b> (" + collegeCode + ")?<br>" +
                "This will also remove all its programs.</html>",
                "Confirm College Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Attempt removal
                    if (CollegeManager.removeCollege(collegeName)) {
                        // Remove from table model
                        model.removeRow(modelRow);
                        
                        // Notify program table panel of changes
                        notifyProgramTableOfChanges();
                        
                        // Success message
                        JOptionPane.showMessageDialog(
                            this,
                            "College removed successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        throw new Exception("CollegeManager failed to remove the college");
                    }
                } catch (Exception e) {
                    // Error message
                    JOptionPane.showMessageDialog(
                        this,
                        "Error removing college: " + e.getMessage(),
                        "Removal Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    // Fallback refresh
                    refreshCollegeTable();
                }
            }
        } else {
            // No selection warning
            JOptionPane.showMessageDialog(
                this,
                "Please select a college to remove first",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void editCollege() {
        int selectedRow = collegeTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int modelRow = collegeTable.convertRowIndexToModel(selectedRow);
            DefaultTableModel model = (DefaultTableModel) collegeTable.getModel();
            
            // Get current values
            String currentCollegeName = (String) model.getValueAt(modelRow, 0);
            String currentCollegeCode = (String) model.getValueAt(modelRow, 1);
            
            // Create edit dialog
            JPanel editPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField collegeNameField = new JTextField(currentCollegeName);
            JTextField collegeCodeField = new JTextField(currentCollegeCode);
            
            editPanel.add(new JLabel("College Name:"));
            editPanel.add(collegeNameField);
            editPanel.add(new JLabel("College Abbreviation:"));
            editPanel.add(collegeCodeField);
            
            int result = JOptionPane.showConfirmDialog(
                this,
                editPanel,
                "Edit College",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String newCollegeName = collegeNameField.getText().trim();
                String newCollegeCode = collegeCodeField.getText().trim();
                
                // Validate input
                if (newCollegeName.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "College name cannot be empty",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                try {
                    // Attempt update
                    if (CollegeManager.updateCollegeName(
                        currentCollegeName,
                        newCollegeName,
                        newCollegeCode
                    )) {
                        // Update table model
                        model.setValueAt(newCollegeName, modelRow, 0);
                        model.setValueAt(newCollegeCode, modelRow, 1);
                        
                        // Notify program table panel of changes
                        notifyProgramTableOfChanges();
                        
                        // Success message
                        JOptionPane.showMessageDialog(
                            this,
                            "College updated successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        throw new Exception("Failed to update college (possibly duplicate name)");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error updating college: " + e.getMessage(),
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    // Fallback refresh
                    refreshCollegeTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Please select a college to edit first",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
