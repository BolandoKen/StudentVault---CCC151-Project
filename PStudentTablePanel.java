import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class PStudentTablePanel extends JPanel{
    private CStudentTable studentTable;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private JButton editButton;
    private JButton sortButton;
    private JComboBox<String> sortByBox;
    private CSearchPanels.StudentSearchPanel searchPanel;
    public PStudentTablePanel() {
        studentTable = new CStudentTable();

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

        searchPanel = new CSearchPanels.StudentSearchPanel(searchParams -> {
    String searchText = searchParams[0].toLowerCase();
    String columnName = searchParams[1];

    // Get the existing sorter from CStudentTable instead of creating a new one
    TableRowSorter<DefaultTableModel> sorter;
    
    // If the table is already sorted, use that sorter, otherwise create a temporary one
    if (studentTable.isSorted()) {
        sorter = (TableRowSorter<DefaultTableModel>) studentTable.getTable().getRowSorter();
    } else {
        // Create a new sorter and apply it
        DefaultTableModel model = (DefaultTableModel) studentTable.getTable().getModel();
        sorter = new TableRowSorter<>(model);
        studentTable.getTable().setRowSorter(sorter);
    }

    // Apply the filter to the existing sorter
    if (searchText.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        if ("All".equals(columnName)) {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        } else {
            // Map column names to column indices
            int columnIndex = -1;
            switch (columnName) {
                case "ID Number": columnIndex = 0; break;
                case "First Name": columnIndex = 1; break;
                case "Last Name": columnIndex = 2; break;
                case "Gender": columnIndex = 3; break;
                case "Year Level": columnIndex = 4; break;
                case "Program Code": columnIndex = 5; break;
            }
            if (columnIndex >= 0) {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, columnIndex));
            }
        }
    }
    
    // Update sort button icon to reflect current state
    updateSortButtonIcon();
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

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sortPanel.setOpaque(false);

        // Create the sort by combobox
        JLabel sortByLabel = new JLabel("Sort by:");
        sortByBox = new JComboBox<>(new String[]{"ID-Number", "First Name", "Last Name", "Gender", "Year Level", "Program Code"});
        
        // Create the sort button
        sortButton = new JButton(new ImageIcon("Assets/DecendingIcon.png"));
        sortButton.setBorderPainted(false);
        sortButton.setFocusPainted(false);
        sortButton.setContentAreaFilled(false);

        sortButton.addActionListener(e -> {
            System.out.println("Sort button clicked");
            int columnIndex = sortByBox.getSelectedIndex();
            studentTable.toggleSorting(columnIndex);
            updateSortButtonIcon();
        });
        
        // Add sort components to panel
        sortPanel.add(sortByLabel);
        sortPanel.add(sortByBox);
        sortPanel.add(sortButton);

        JButton addCollegeButton = new JButton(new ImageIcon("Assets/PlusIcon.png"));
        addCollegeButton.setBorderPainted(false);
        addCollegeButton.setFocusPainted(false);
        addCollegeButton.setContentAreaFilled(false);
        addCollegeButton.setBackground(new Color(0xE7E7E7));

        addCollegeButton.addActionListener(e -> {
            Dialogs.addStudentDialog(studentTable);
        });
 
        deleteButton = new JButton(new ImageIcon("Assets/DeleteIcon.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);

        deleteButton.addActionListener(e -> {
           JTable table = studentTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 0);
                if (idValue != null) {
                    String collegeCode = idValue.toString();
                    Dialogs.deleteStudentDialog(collegeCode, studentTable); 
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
            JTable table = studentTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 0);
                if (idValue != null) {
                    String studentId = idValue.toString();
                    Dialogs.editStudentDialog(studentId, studentTable);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a college to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            } 
        });
        
        buttonsPanel.add(addCollegeButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);

        JLabel collegeText = new JLabel("Students");
        collegeText.setFont(new Font("Helvetica", Font.BOLD, 32));
        JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textContainer.setOpaque(false);

        textContainer.add(collegeText);
        textContainer.add(sortPanel);
        leftPanel.add(textContainer, BorderLayout.SOUTH);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false); 
        gbc.gridy = 2;
        gbc.weighty = 0.9;
        this.add(bottomRow, gbc);

       
    
        JScrollPane scrollPane = new JScrollPane(studentTable);
        bottomRow.add(scrollPane, BorderLayout.CENTER);
    }
    // In PStudentTablePanel.java
    public CStudentTable getStudentTableComponent() {
        return studentTable;
    }
    private void updateSortButtonIcon() {
        if (!studentTable.isSorted()) {
            sortButton.setIcon(new ImageIcon("Assets/SortDisabledIcon.png"));
        } else {
         sortButton.setIcon(new ImageIcon(
            studentTable.getCurrentSortOrder() == SortOrder.ASCENDING 
            ? "Assets/AscendingIcon.png" 
            : "Assets/DecendingIcon.png"
         ));
        }
    }
    
  
   
}