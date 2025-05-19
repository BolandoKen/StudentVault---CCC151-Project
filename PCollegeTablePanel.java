import java.awt.*;
import javax.swing.*;

public final class PCollegeTablePanel extends JPanel {
    private CCollegeTable collegeTable;
    private boolean selectionMode = false;
    private final JButton deleteButton;
    private final JPanel buttonsPanel;    
    private final JButton editButton;
    private JButton sortButton;
    private final CSearchPanels.CollegeSearchPanel searchPanel;

    public PCollegeTablePanel() {
        collegeTable = new CCollegeTable();
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

        searchPanel = new CSearchPanels.CollegeSearchPanel(params -> {
            String searchText = params[0];
            String columnName = params[1];
            
            if (searchText.isEmpty()) {
                collegeTable.clearSearch();
                return;
            }
            
            if (columnName.equals("All")) {
                collegeTable.searchAllColumns(searchText);
            } else {
                collegeTable.searchByColumn(searchText, columnName);
            }
        });

        searchPanelContainer.add(searchPanel, BorderLayout.NORTH);
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

        addCollegeButton.addActionListener(e -> {
            Dialogs.addCollegeDialog(collegeTable);
        });
 
        deleteButton = new JButton(new ImageIcon("Assets/DeleteIcon.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);

        deleteButton.addActionListener(e -> {
            JTable table = collegeTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 0);
                if (idValue != null) {
                    String collegeCode = idValue.toString();
                    Dialogs.deleteCollegeDialog(collegeCode, collegeTable); 
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
            JTable table = collegeTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 0);
                if (idValue != null) {
                    String collegeCode = idValue.toString();
                    Dialogs.editCollegeDialog(collegeCode, collegeTable); 
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a college to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        buttonsPanel.add(addCollegeButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);

        JLabel collegeText = new JLabel("Colleges");
        collegeText.setFont(new Font("Helvetica", Font.BOLD, 32));
        JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textContainer.setOpaque(false);
        textContainer.add(collegeText);

        sortButton = new JButton(new ImageIcon("Assets/DecendingIcon.png"));
        sortButton.setBorderPainted(false);
        sortButton.setFocusPainted(false);
        sortButton.setContentAreaFilled(false);

        // Change your sort button action listener to:
sortButton.addActionListener(e -> {
    collegeTable.toggleSorting();
    
    // Update button icon based on sort state
    if (collegeTable.getTable().getRowSorter() == null) {
        sortButton.setIcon(new ImageIcon("Assets/SortDisabledIcon.png"));
    } else {
        sortButton.setIcon(new ImageIcon(
            collegeTable.getCurrentSortOrder() == SortOrder.ASCENDING 
            ? "Assets/AscendingIcon.png" 
            : "Assets/DecendingIcon.png"
        ));
    }
});
        
        leftPanel.add(textContainer, BorderLayout.SOUTH);
        
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false); 
        gbc.gridy = 2;
        gbc.weighty = 0.9;
        this.add(bottomRow, gbc);

       
        JScrollPane scrollPane = new JScrollPane(collegeTable);
        bottomRow.add(scrollPane, BorderLayout.CENTER);
    }
    // In PCollegeTablePanel.java
public CCollegeTable getCollegeTableComponent() {
    return collegeTable;
}
    
  
}
