import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SearchPanel extends JPanel {
    private TablePanel tablePanel;
    private RoundedTextField searchField;
    private RoundedComboBox searchByField;
    private CollegeTablePanel collegeTablePanel;
    private ProgramTablePanel programTablePanel;
    private JButton filterButton;
    
    // Constants for search field options
    private static final String ALL_FIELDS = "All Fields";
    private static final String NO_FILTER = "No Filter";
    private ActionListener customFilterAction;
    
    public SearchPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 0);
        gbc.weightx = 1.0;

        RoundedPanel searchPanel = new RoundedPanel(10);
        searchPanel.setLayout(new BorderLayout(10, 0));
        searchPanel.setBackground(new Color(0xE7E7E7));

        JPanel searchControlsPanel = new JPanel(new BorderLayout(10, 0));
        searchControlsPanel.setOpaque(false);
        
        searchField = new RoundedTextField(200, 10, "Search", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        
        // Default search fields with "No Filter" option
        String[] defaultSearchFields = new String[]{NO_FILTER, ALL_FIELDS};
        
        Color backgroundColor = new Color(0xE7E7E7);
        Color textColor = new Color(0x7E7E7E);
        Font comboFont = new Font("Helvetica", Font.PLAIN, 16);
        int cornerRadius = 10;
        Dimension comboSize = new Dimension(180, 28); // Increased width for longer options
        Color selectionColor = new Color(0x658CF1);
        
        searchByField = new RoundedComboBox(
            defaultSearchFields,
            backgroundColor,
            textColor,
            comboFont,
            cornerRadius,
            comboSize,
            NO_FILTER, // Default selected item
            selectionColor
        );
        
        JPanel searchByWrapper = new JPanel(new BorderLayout());
        searchByWrapper.setOpaque(false);
        searchByWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchByWrapper.add(searchByField, BorderLayout.CENTER);
        
        JButton clearButton = createButton("Assets/XIcon.png");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(searchField.getText(), (String) searchByField.getSelectedItem());
            }
        });
        
        searchByField.addActionListener(e -> {
            if (!searchField.getText().isEmpty()) {
                performSearch(searchField.getText(), (String) searchByField.getSelectedItem());
            } else {
                // Update the search panel if filter selection changes even if search field is empty
                performSearch("", (String) searchByField.getSelectedItem());
            }
        });
        
        clearButton.addActionListener(e -> {
            searchField.setText("");
            performSearch("", (String) searchByField.getSelectedItem());
        });

        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        searchControlsPanel.add(searchByWrapper, BorderLayout.WEST);
        
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setOpaque(false);
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        paddedPanel.add(searchControlsPanel, BorderLayout.CENTER);
        
        searchPanel.add(paddedPanel, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        this.add(searchPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 0, 10, 10);
        filterButton = createButton("Assets/FilterIcon.png");
        filterButton.addActionListener(e -> {
            if (customFilterAction != null) {
                customFilterAction.actionPerformed(e);
            } else {
                showFilterDialog(); // Default behavior
            }
        });
        this.add(filterButton, gbc);
    }

    private JButton createButton(String iconPath) {
        JButton button = new JButton(new ImageIcon(iconPath));
        button.setPreferredSize(new Dimension(40, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }
    
    public void setTablePanel(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }

    public void setCollegeTablePanel(CollegeTablePanel collegeTablePanel) {
        this.collegeTablePanel = collegeTablePanel;
        updateSearchFields();
    }

    public void setProgramTablePanel(ProgramTablePanel programTablePanel) {
        this.programTablePanel = programTablePanel;
        updateSearchFields();
    }
    
    private void updateSearchFields() {
        if (searchByField == null) return;
        
        String[] searchFields;
        if (collegeTablePanel != null) {
            searchFields = new String[]{NO_FILTER, ALL_FIELDS, "College Name", "College Abbreviation"};
        } else if (programTablePanel != null) {
            // Enhanced program search fields
            searchFields = new String[]{
                NO_FILTER, 
                ALL_FIELDS, 
                "Program Name", 
                "Program Abbreviation", 
                "College",
                "Program Type", // Added for more search options
                "Department"    // Added for more search options
            };
        } else {
            searchFields = new String[]{NO_FILTER, ALL_FIELDS};
        }
        
        // Remember the previously selected item if possible
        String previousSelection = (String) searchByField.getSelectedItem();
        searchByField.setModel(new DefaultComboBoxModel<>(searchFields));
        
        // Try to restore previous selection or default to NO_FILTER
        boolean found = false;
        for (String option : searchFields) {
            if (option.equals(previousSelection)) {
                searchByField.setSelectedItem(previousSelection);
                found = true;
                break;
            }
        }
        
        if (!found) {
            searchByField.setSelectedItem(NO_FILTER);
        }
    }

    private void performSearch(String searchText, String searchField) {
        // If "No Filter" is selected, clear any filters regardless of search text
        if (NO_FILTER.equals(searchField)) {
            clearAllFilters();
            return;
        }
        
        if (collegeTablePanel != null) {
            performCollegeSearch(searchText, searchField);
        } else if (programTablePanel != null) {
            performProgramSearch(searchText, searchField);
        } else if (tablePanel != null) {
            performStudentSearch(searchText, searchField);
        }
    }
    
    private void clearAllFilters() {
        if (collegeTablePanel != null && collegeTablePanel.getCollegeTable() != null) {
            collegeTablePanel.getCollegeTable().setRowSorter(null);
        } else if (programTablePanel != null && programTablePanel.getProgramTable() != null) {
            programTablePanel.getProgramTable().setRowSorter(null);
        } else if (tablePanel != null) {
            // Clear student table filters if implemented
        }
    }
    
    private void performCollegeSearch(String searchText, String searchField) {
        JTable table = collegeTablePanel.getCollegeTable();
        if (table == null) return;
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            if (searchField.equals(ALL_FIELDS)) {
                List<RowFilter<Object, Object>> filters = new ArrayList<>();
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 0)); // Name column
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 1)); // Abbreviation column
                sorter.setRowFilter(RowFilter.orFilter(filters));
            } else {
                int columnIndex = getCollegeColumnIndex(searchField);
                if (columnIndex != -1) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, columnIndex));
                }
            }
        }
    }
    
    private void performProgramSearch(String searchText, String searchField) {
        JTable table = programTablePanel.getProgramTable();
        if (table == null) return;
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            if (searchField.equals(ALL_FIELDS)) {
                List<RowFilter<Object, Object>> filters = new ArrayList<>();
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 0)); // Name column
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 1)); // Abbreviation column
                filters.add(RowFilter.regexFilter("(?i)" + searchText, 2)); // College column
                // Add new columns to search if they exist in the table model
                if (table.getModel().getColumnCount() > 3) {
                    filters.add(RowFilter.regexFilter("(?i)" + searchText, 3)); // Program Type column
                }
                if (table.getModel().getColumnCount() > 4) {
                    filters.add(RowFilter.regexFilter("(?i)" + searchText, 4)); // Department column
                }
                sorter.setRowFilter(RowFilter.orFilter(filters));
            } else {
                int columnIndex = getProgramColumnIndex(searchField);
                if (columnIndex != -1) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText, columnIndex));
                }
            }
        }
    }
    
    private void performStudentSearch(String searchText, String searchField) {
        if (tablePanel == null) return;
        // ... existing student search implementation ...
    }
    
    private int getCollegeColumnIndex(String fieldName) {
        switch (fieldName) {
            case "College Name": return 0;
            case "College Abbreviation": return 1;
            default: return -1;
        }
    }
    
    private int getProgramColumnIndex(String fieldName) {
        switch (fieldName) {
            case "Program Name": return 1;
            case "Program Abbreviation": return 2;
            case "College": return 0;
            default: return -1;
        }
    }
    
    private int getStudentColumnIndex(String fieldName) {
        switch (fieldName) {
            case "First Name": return 1;
            case "Last Name": return 2;
            case "Gender": return 3;
            case "ID Number": return 4;
            case "Year Level": return 5;
            case "College": return 6;
            case "Program": return 7;
            default: return -1;
        }
    }
    
    private void showFilterDialog() {
        if (tablePanel == null) return;
        FilterDialog dialog = new FilterDialog(SwingUtilities.getWindowAncestor(this), tablePanel);
        dialog.setVisible(true);
    }
    public JButton getFilterButton() {
        return filterButton;
    }
    public void setFilterButtonAction(ActionListener action) {
    this.customFilterAction = action;
    }
}