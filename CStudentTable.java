import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class CStudentTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;
    private List<String> columnNames;
    private boolean isSorted = false;
    private SortOrder currentSortOrder = SortOrder.ASCENDING;
    private int currentSortColumn = 0; // Default sort column (ID Number)


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
        
        tableModel = new DefaultTableModel(columnNames.toArray(new String[0]), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        sorter.setSortable(0, true);  // ID Number
        sorter.setSortable(1, true);  // First Name
        sorter.setSortable(2, true);  // Last Name
        sorter.setSortable(3, true);  // Gender
        sorter.setSortable(4, true);  // Year Level
        sorter.setSortable(5, true);  // Program Code
        
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
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
        
        //add(searchPanel, BorderLayout.NORTH);
        
        // Load student data
        
       
        loadStudentData();
    }
    
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        
        int selectedIndex = searchColumnComboBox.getSelectedIndex();
        
        if (selectedIndex == 0) { 
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
    
    public void searchByColumn(String searchTerm, String columnName) {
        searchField.setText(searchTerm);
        
        for (int i = 0; i < columnNames.size(); i++) {
            if (columnNames.get(i).equals(columnName)) {
                searchColumnComboBox.setSelectedIndex(i + 1); 
            }
        }
        
        performSearch();
    }
    
    public void clearSearch() {
        searchField.setText("");
        performSearch();
    }
    
    private void loadStudentData() {
        tableModel.setRowCount(0);
        
        List<Student> students = StudentDataManager.loadStudents();
        
        for (Student student : students) {
            Object[] rowData = {
                student.getIdNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getGender(),
                student.getYearLevel(),
                student.getProgramCode()
            };
            tableModel.addRow(rowData);
        }
    }
    
    public void refreshTable() {
        int selectedRow = table.getSelectedRow();
        String selectedId = selectedRow >= 0 ? (String) table.getValueAt(selectedRow, table.convertColumnIndexToView(0)) : null;
        
        String currentSearch = searchField.getText();
        int currentSearchColumn = searchColumnComboBox.getSelectedIndex();
    
        // Save the current sort state
        boolean wasSorted = isSorted;
        int sortColumn = currentSortColumn;
        SortOrder sortOrder = currentSortOrder;
        
        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            // Clear any filtering/sorting temporarily
            if (wasSorted) {
                table.setRowSorter(null);
                isSorted = false;
            }
            
            // Reload data
            loadStudentData();
            
            // Restore sorting if it was active
            if (wasSorted) {
                table.setRowSorter(sorter);
                sorter.setSortKeys(Collections.singletonList(
                    new RowSorter.SortKey(sortColumn, sortOrder)
                ));
                isSorted = true;
                sorter.sort();
            }
            
            if (selectedId != null) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (selectedId.equals(table.getValueAt(i, table.convertColumnIndexToView(0)))) {
                        table.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            }
            
            if (!currentSearch.isEmpty()) {
                searchField.setText(currentSearch);
                searchColumnComboBox.setSelectedIndex(currentSearchColumn);
                performSearch();
            }
        } finally {
            setCursor(oldCursor);
        }
    }
    
    public JTable getTable() {
        return table;
    }
    
    public String getSelectedStudentId() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        
        int modelRow = table.convertRowIndexToModel(viewRow);
        return (String) tableModel.getValueAt(modelRow, 0);
    }
    
    public JTextField getSearchField() {
        return searchField;
    }
    
    public JComboBox<String> getSearchColumnComboBox() {
        return searchColumnComboBox;
    }

    public void toggleSorting(int columnIndex) {
    currentSortColumn = columnIndex;
    
    if (!isSorted) {
        // Enable sorting
        table.setRowSorter(sorter);
        sorter.setSortKeys(Collections.singletonList(
            new RowSorter.SortKey(currentSortColumn, currentSortOrder)
        ));
        isSorted = true;
    } else {
        // Toggle sort direction
        currentSortOrder = currentSortOrder == SortOrder.ASCENDING 
                         ? SortOrder.DESCENDING 
                         : SortOrder.ASCENDING;
        sorter.setSortKeys(Collections.singletonList(
            new RowSorter.SortKey(currentSortColumn, currentSortOrder)
        ));
    }
    
    // Update the UI to reflect changes
    sorter.sort();
}

public SortOrder getCurrentSortOrder() {
    return currentSortOrder;
}

public int getCurrentSortColumn() {
    return currentSortColumn;
}

public boolean isSorted() {
    return isSorted;
}
}