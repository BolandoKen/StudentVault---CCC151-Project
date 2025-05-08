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
    
    public SearchPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 0);
        gbc.weightx = 1.0;

        RoundedPanel searchPanel = new RoundedPanel(10);
        searchPanel.setLayout(new BorderLayout(10, 0)); // Add horizontal gap between components
        searchPanel.setBackground(new Color(0xE7E7E7));

        // Create a panel for the search field and the search by combo box
        JPanel searchControlsPanel = new JPanel(new BorderLayout(10, 0)); // Increased gap
        searchControlsPanel.setOpaque(false);
        
        // Create the search field
        searchField = new RoundedTextField(200, 10, "Search", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        
        // Create the search by field combo box with increased width
        String[] searchFields = {"All Fields", "First Name", "Last Name", "ID Number", "Year Level", "College", "Program"};
        Color backgroundColor = new Color(0xE7E7E7);
        Color textColor = new Color(0x7E7E7E);
        Font comboFont = new Font("Helvetica", Font.PLAIN, 16);
        int cornerRadius = 10;
        Dimension comboSize = new Dimension(150, 28); // Increased width from 120 to 150
        Color selectionColor = new Color(0x658CF1);
        
        searchByField = new RoundedComboBox(
            searchFields,
            backgroundColor,
            textColor,
            comboFont,
            cornerRadius,
            comboSize,
            "All Fields",
            selectionColor
        );
        
        // Add some padding around the combo box
        JPanel searchByWrapper = new JPanel(new BorderLayout());
        searchByWrapper.setOpaque(false);
        searchByWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchByWrapper.add(searchByField, BorderLayout.CENTER);
        
        // Create the clear button
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
            }
        });
        
        clearButton.addActionListener(e -> {
            searchField.setText("");
            performSearch("", (String) searchByField.getSelectedItem());
        });

        // Add components to the search controls panel
        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        searchControlsPanel.add(searchByWrapper, BorderLayout.WEST);
        
        // Add padding inside the search panel
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
        JButton filterButton = createButton("Assets/FilterIcon.png");
        filterButton.addActionListener(e -> showFilterDialog());
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
    }


    public void setProgramTablePanel(ProgramTablePanel programTablePanel) {
        this.programTablePanel = programTablePanel;
    }
    private void performSearch(String searchText, String searchField) {
        if (tablePanel == null) return;
    
        JTable table = tablePanel.getTable();
        if (table == null) return;
    
        // Reset to first page when searching
        tablePanel.resetPage();
    
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
    
        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // If "All Fields" is selected, search in all columns
            if (searchField.equals("All Fields")) {
                // Create a composite filter that checks both abbreviations and full names
                List<RowFilter<Object, Object>> filters = new ArrayList<>();
    
                // Add direct text search for all columns
                filters.add(RowFilter.regexFilter("(?i)" + searchText));
    
                // Add search by full college name
                for (String college : CollegeDataManager.getAllColleges()) {
                    if (college.toLowerCase().contains(searchText.toLowerCase())) {
                        String abbr = CollegeDataManager.getCollegeAbbr(college);
                        filters.add(RowFilter.regexFilter("(?i)" + abbr, 6));
                    }
                }
    
                // Add search by full program name
                for (String college : CollegeDataManager.getAllColleges()) {
                    for (String program : CollegeDataManager.getProgramsForCollege(college)) {
                        if (program.toLowerCase().contains(searchText.toLowerCase())) {
                            String abbr = CollegeDataManager.getProgramAbbr(program);
                            filters.add(RowFilter.regexFilter("(?i)" + abbr, 7));
                        }
                    }
                }
    
                sorter.setRowFilter(RowFilter.orFilter(filters));
            } else {
                // Get the column index based on the selected field
                int columnIndex = getColumnIndex(searchField);
                if (columnIndex != -1) {
                    if (columnIndex == 6) { // College column
                        // Search by college name
                        List<RowFilter<Object, Object>> filters = new ArrayList<>();
                        filters.add(RowFilter.regexFilter("(?i)" + searchText, columnIndex));
    
                        // Add search by full college name
                        for (String college : CollegeDataManager.getAllColleges()) {
                            if (college.toLowerCase().contains(searchText.toLowerCase())) {
                                String abbr = CollegeDataManager.getCollegeAbbr(college);
                                filters.add(RowFilter.regexFilter("(?i)" + abbr, columnIndex));
                            }
                        }
    
                        sorter.setRowFilter(RowFilter.orFilter(filters));
                    } else if (columnIndex == 7) { // Program column
                        // Search by program name
                        List<RowFilter<Object, Object>> filters = new ArrayList<>();
                        filters.add(RowFilter.regexFilter("(?i)" + searchText, columnIndex));
    
                        // Add search by full program name
                        for (String college : CollegeDataManager.getAllColleges()) {
                            for (String program : CollegeDataManager.getProgramsForCollege(college)) {
                                if (program.toLowerCase().contains(searchText.toLowerCase())) {
                                    String abbr = CollegeDataManager.getProgramAbbr(program);
                                    filters.add(RowFilter.regexFilter("(?i)" + abbr, columnIndex));
                                }
                            }
                        }
    
                        sorter.setRowFilter(RowFilter.orFilter(filters));
                    } else {
                        // For other columns, use standard search
                        List<RowFilter<Object, Object>> filters = new ArrayList<>();
                        filters.add(RowFilter.regexFilter("(?i)" + searchText, columnIndex));
                        sorter.setRowFilter(RowFilter.orFilter(filters));
                    }
                }
            }
        }
    }
    
    private int getColumnIndex(String fieldName) {
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
}