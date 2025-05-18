import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.util.Collections;
import java.util.Map;

public class CCollegeTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;
    private boolean isSorted = false;
    private SortOrder currentSortOrder = SortOrder.ASCENDING;

    public CCollegeTable() {
        setLayout(new BorderLayout());
        
        // Define column headers (college code and name)
        String[] columns = {"College Code", "College Name"};
        
        // Create a table model that doesn't allow cell editing
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        
        sorter = new TableRowSorter<>(tableModel);
        sorter.setSortable(0, true);  
        sorter.setSortable(1, true);  
        table.setRowSorter(sorter);
        
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
        
        // Create column selection dropdown for search
        searchColumnComboBox = new JComboBox<>(new String[]{"All Columns", "College Code", "College Name"});
        
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
        
        //add(searchPanel, BorderLayout.NORTH);
        
        // Load college data
        loadCollegeData();
    }
    
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null); // Show all rows if search is empty
            return;
        }
        
        int selectedIndex = searchColumnComboBox.getSelectedIndex();
        
        if (selectedIndex == 0) { // All Columns
            // Create a composite row filter that checks all columns
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
            // Search in specific column (subtract 1 because "All Columns" is at index 0)
            int columnToSearch = selectedIndex - 1;
            RowFilter<DefaultTableModel, Integer> filter = 
                RowFilter.regexFilter("(?i)" + searchText, columnToSearch);
            sorter.setRowFilter(filter);
        }
    }
    
    public void searchAllColumns(String searchTerm) {
        searchField.setText(searchTerm);
        searchColumnComboBox.setSelectedIndex(0); // All columns
        performSearch();
    }
    
    public void searchByColumn(String searchTerm, String columnName) {
        searchField.setText(searchTerm);
        
        // Set the appropriate column in combo box
        if (columnName.equals("College Code")) {
            searchColumnComboBox.setSelectedIndex(1);
        } else if (columnName.equals("College Name")) {
            searchColumnComboBox.setSelectedIndex(2);
        } else {
            searchColumnComboBox.setSelectedIndex(0); // Default to All Columns
        }
        
        performSearch();
    }
    
    public void clearSearch() {
        searchField.setText("");
        performSearch();
    }
    
    private void loadCollegeData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get college data from CollegeDataManager
        Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
        
        // Sort by college code (use TreeMap if you need guaranteed sort order)
        collegeMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String collegeCode = entry.getKey();
                String collegeName = entry.getValue();
                
                // Add row to table
                Object[] rowData = {collegeCode, collegeName};
                tableModel.addRow(rowData);
            });
    }
    
    public void refreshTable() {
        // Save current selection and search state
        int selectedRow = table.getSelectedRow();
        String selectedCode = selectedRow >= 0 ? (String) table.getValueAt(selectedRow, table.convertColumnIndexToView(0)) : null;
        
        String currentSearch = searchField.getText();
        int currentSearchColumn = searchColumnComboBox.getSelectedIndex();

        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            // Clear current filters
            sorter.setRowFilter(null);
            
            // Reload data
            loadCollegeData();
            
            // Restore selection if possible
            if (selectedCode != null) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (selectedCode.equals(table.getValueAt(i, table.convertColumnIndexToView(0)))) {
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
    
    public JTable getTable() {
        return table;
    }
    
    public String getSelectedCollegeCode() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        
        // Convert view row index to model row index
        int modelRow = table.convertRowIndexToModel(viewRow);
        return (String) tableModel.getValueAt(modelRow, 0);
    }
    
    public JTextField getSearchField() {
        return searchField;
    }
    
    public JComboBox<String> getSearchColumnComboBox() {

        return searchColumnComboBox;
    }
public void toggleSorting() {
    if (!isSorted) {
        // Enable sorting
        table.setRowSorter(sorter);
        // Sort by first column (College Code) by default
        sorter.setSortKeys(Collections.singletonList(
            new RowSorter.SortKey(0, currentSortOrder)
        ));
        isSorted = true;
    } else {
        // Toggle sort direction
        currentSortOrder = currentSortOrder == SortOrder.ASCENDING 
                         ? SortOrder.DESCENDING 
                         : SortOrder.ASCENDING;
        sorter.setSortKeys(Collections.singletonList(
            new RowSorter.SortKey(0, currentSortOrder)
        ));
    }
    
    // Update the UI to reflect changes
    sorter.sort();
}

public SortOrder getCurrentSortOrder() {
    return currentSortOrder;
}

}