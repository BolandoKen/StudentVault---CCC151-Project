import java.awt.*;
import javax.swing.*;

public final class PProgramTablePanel extends JPanel {
    private CProgramTable programTable;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private JButton editButton;

    public PProgramTablePanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

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

        JButton addProgramButton = new JButton(new ImageIcon("Assets/PlusIcon.png"));
        addProgramButton.setBorderPainted(false);
        addProgramButton.setFocusPainted(false);
        addProgramButton.setContentAreaFilled(false);
        addProgramButton.setBackground(new Color(0xE7E7E7));

        addProgramButton.addActionListener(e -> {
            Dialogs.addProgramDialog(programTable);
        });
 
        deleteButton = new JButton(new ImageIcon("Assets/DeleteIcon.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);

        deleteButton.addActionListener(e -> {
            JTable table = programTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 1);
                if (idValue != null) {
                    String programCode = idValue.toString();
                    Dialogs.deleteProgramDialog(programCode, programTable); 
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a college to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        editButton = new JButton(new ImageIcon("Assets/EditIcon.png"));
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setContentAreaFilled(false);
       editButton.addActionListener(e -> {
            JTable table = programTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 1);
                if (idValue != null) {
                    String programCode = idValue.toString();
                    Dialogs.editProgramDialog(programCode, programTable); 
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a college to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonsPanel.add(addProgramButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);

        JLabel collegeText = new JLabel("Programs");
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

        programTable = new CProgramTable();
        
        // Initialize the table with data
        //CollegeTableUtility.populateCollegeTable(collegeTable);

        JScrollPane scrollPane = new JScrollPane(programTable);
        bottomRow.add(scrollPane, BorderLayout.CENTER);
    }
   /**
 * Returns the CProgramTable associated with this panel
 * @return The CProgramTable instance
 */
public CProgramTable getProgramTable() {
    return programTable;
}
}
