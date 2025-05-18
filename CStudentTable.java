import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.util.List;
import java.util.ArrayList;

public class CStudentTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;
    private List<String> columnNames;

    public CStudentTable() {
        setLayout(new BorderLayout());
        
        // Define column headers based on Students.csv structure
columnNames = new ArrayList<>();
columnNames.add("ID Number");
columnNames.add("First Name");
columnNames.add("Last Name");
columnNames.add("Gender");
columnNames.add("Year Level");
columnNames.add("Program Code");
        
        // Create a table model that doesn't allow cell editing
        tableModel = new DefaultTableModel(columnNames.toArray(new String[0]), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table with the model
        table = new JTable(tableModel);
        
        // Create and add the sorter for search filtering
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // Set table appearance
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create the search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(20);
        JLabel columnLabel = new JLabel("in");
        
        // Create column selection dropdown for search with all column options
        String[] searchOptions = new String[columnNames.size() + 1];
        searchOptions[0] = "All Columns";
        for (int i = 0; i < columnNames.size(); i++) {
            searchOptions[i + 1] = columnNames.get(i);
        }
        searchColumnComboBox = new JComboBox<>(searchOptions);
        
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        
        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> clearSearch());
        searchField.addActionListener(e -> performSearch()); // Search on Enter key
        
        // Add components to search panel
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(columnLabel);
        searchPanel.add(searchColumnComboBox);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Load student data
        loadStudentData();
    }
    
    /**
     * Performs the search based on the search field and column selection
     */
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        
        int selectedIndex = searchColumnComboBox.getSelectedIndex();
        
        if (selectedIndex == 0) { // All Columns
            RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            sorter.setRowFilter(filter);
        } else {
            int columnToSearch = selectedIndex - 1;
            RowFilter<DefaultTableModel, Integer> filter = 
                RowFilter.regexFilter("(?i)" + searchText, columnToSearch);
            sorter.setRowFilter(filter);
        }
    }
    
    /**
     * Search in a specific column
     * 
     * @param searchTerm The term to search for
     * @param columnName The name of the column to search in
     */
    public void searchByColumn(String searchTerm, String columnName) {
        searchField.setText(searchTerm);
        
        // Find the column index by name
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equals(columnName)) {
                searchColumnComboBox.setSelectedIndex(i + 1); // +1 because "All Columns" is at index 0
                break;
            }
        }
        
        performSearch();
    }
    
    /**
     * Clear the current search and show all rows
     */
    public void clearSearch() {
        searchField.setText("");
        performSearch();
    }
    
    /**
     * Loads student data from CSV using StudentDataManager
     */
    private void loadStudentData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get student data from StudentDataManager
        List<Student> students = StudentDataManager.loadStudents();
        
        // Add each student to the table
        for (Student student : students) {
            Object[] rowData = {
                student.getIdNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getGender(),
                student.getYearLevel(),
                student.getProgramCode()
                // Removed programName, collegeName, and collegeCode
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * Refreshes the table data while maintaining selection and search state
     */
    public void refreshTable() {
        // Save current selection and search state
        int selectedRow = table.getSelectedRow();
        String selectedId = selectedRow >= 0 ? (String) table.getValueAt(selectedRow, table.convertColumnIndexToView(0)) : null;
        
        String currentSearch = searchField.getText();
        int currentSearchColumn = searchColumnComboBox.getSelectedIndex();

        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            // Clear current filters
            sorter.setRowFilter(null);
            
            // Reload data
            loadStudentData();
            
            // Restore selection if possible
            if (selectedId != null) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (selectedId.equals(table.getValueAt(i, table.convertColumnIndexToView(0)))) {
                        table.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            }
            
            // Restore search if there was one
            if (!currentSearch.isEmpty()) {
                searchField.setText(currentSearch);
                searchColumnComboBox.setSelectedIndex(currentSearchColumn);
                performSearch();
            }
        } finally {
            // Restore cursor
            setCursor(oldCursor);
        }
    }
    
    /**
     * Get the JTable component
     * @return The JTable
     */
    public JTable getTable() {
        return table;
    }
    
    /**
     * Get the ID of the currently selected student
     * @return The selected student ID, or null if none selected
     */
    public String getSelectedStudentId() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        
        // Convert view row index to model row index
        int modelRow = table.convertRowIndexToModel(viewRow);
        return (String) tableModel.getValueAt(modelRow, 0);
    }
    
    /**
     * Get the search field component
     * @return The search field JTextField
     */
    public JTextField getSearchField() {
        return searchField;
    }
    
    /**
     * Get the search column combo box component
     * @return The column selection JComboBox
     */
    public JComboBox<String> getSearchColumnComboBox() {
        return searchColumnComboBox;
    }
}