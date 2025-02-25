import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FilterDialog extends JDialog {
    private final TablePanel tablePanel;
    private Map<String, JCheckBox> genderCheckboxes = new HashMap<>();
    private Map<String, JCheckBox> yearCheckboxes = new HashMap<>();
    private Map<String, JCheckBox> collegeCheckboxes = new HashMap<>();
    private Map<String, JCheckBox> programCheckboxes = new HashMap<>();
    
    public FilterDialog(Window parent, TablePanel tablePanel) {
        super(parent, "Filter Students");
        this.tablePanel = tablePanel;
        
        initComponents();
        loadFilterOptions();
        
        // Set dialog properties
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Gender filter
        JPanel genderPanel = createFilterSection("Filter by Gender");
        genderCheckboxes.put("Male", new JCheckBox("Male"));
        genderCheckboxes.put("Female", new JCheckBox("Female"));
        genderCheckboxes.put("Other", new JCheckBox("Other"));
        
        for (JCheckBox checkbox : genderCheckboxes.values()) {
            checkbox.setSelected(true);
            genderPanel.add(checkbox);
        }
        
        // Year Level filter
        JPanel yearPanel = createFilterSection("Filter by Year Level");
        for (int i = 1; i <= 4; i++) {
            String yearText = i + getOrdinalSuffix(i) + " Year";
            JCheckBox yearBox = new JCheckBox(yearText);
            yearBox.setSelected(true);
            yearPanel.add(yearBox);
            yearCheckboxes.put(String.valueOf(i), yearBox);
        }
        
        // College and Program filters will be populated dynamically
        JPanel collegePanel = createFilterSection("Filter by College");
        JPanel programPanel = createFilterSection("Filter by Program");
        
        // Add all filter sections
        filterPanel.add(genderPanel);
        filterPanel.add(Box.createVerticalStrut(10));
        filterPanel.add(yearPanel);
        filterPanel.add(Box.createVerticalStrut(10));
        filterPanel.add(collegePanel);
        filterPanel.add(Box.createVerticalStrut(10));
        filterPanel.add(programPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = new JButton("Apply Filters");
        JButton resetButton = new JButton("Reset Filters");
        JButton cancelButton = new JButton("Cancel");
        
        applyButton.addActionListener(e -> {
            applyFilters();
            dispose();
        });
        
        resetButton.addActionListener(e -> resetFilters());
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(resetButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        JScrollPane scrollPane = new JScrollPane(filterPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadFilterOptions() {
        // Get the components directly from the content pane
        Container contentPane = getContentPane();
        
        // The content pane has a BorderLayout with a JScrollPane in the CENTER
        JScrollPane scrollPane = (JScrollPane) contentPane.getComponent(0);
        // Get the view component from the scroll pane (which is the filter panel)
        JPanel filterPanel = (JPanel) scrollPane.getViewport().getView();
        
        // Now get the college panel (index 4 in the filter panel)
        JPanel collegePanel = (JPanel) filterPanel.getComponent(4);
        
        Set<String> colleges = getAvailableColleges();
        collegeCheckboxes.clear();
        collegePanel.removeAll();
        
        JLabel collegeLabel = new JLabel("Filter by College");
        collegeLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        collegeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        collegePanel.add(collegeLabel);
        
        for (String college : colleges) {
            JCheckBox box = new JCheckBox(college);
            box.setSelected(true);
            collegePanel.add(box);
            collegeCheckboxes.put(college, box);
        }
        
        // Get the program panel (index 6 in the filter panel)
        JPanel programPanel = (JPanel) filterPanel.getComponent(6);
        
        Set<String> programs = getAvailablePrograms();
        programCheckboxes.clear();
        programPanel.removeAll();
        
        JLabel programLabel = new JLabel("Filter by Program");
        programLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        programLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        programPanel.add(programLabel);
        
        for (String program : programs) {
            JCheckBox box = new JCheckBox(program);
            box.setSelected(true);
            programPanel.add(box);
            programCheckboxes.put(program, box);
        }
    }
    
    private JPanel createFilterSection(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel label = new JLabel(title);
        label.setFont(new Font("Helvetica", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        panel.add(label);
        
        return panel;
    }
    
    private String getOrdinalSuffix(int number) {
        if (number >= 11 && number <= 13) {
            return "th";
        }
        
        switch (number % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
    
    private void resetFilters() {
        // Reset all checkboxes to selected
        for (JCheckBox box : genderCheckboxes.values()) {
            box.setSelected(true);
        }
        
        for (JCheckBox box : yearCheckboxes.values()) {
            box.setSelected(true);
        }
        
        for (JCheckBox box : collegeCheckboxes.values()) {
            box.setSelected(true);
        }
        
        for (JCheckBox box : programCheckboxes.values()) {
            box.setSelected(true);
        }
    }
    
    private void applyFilters() {
        // Collect selected filter values
        Set<String> selectedGenders = new HashSet<>();
        for (Map.Entry<String, JCheckBox> entry : genderCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedGenders.add(entry.getKey());
            }
        }
        
        Set<String> selectedYears = new HashSet<>();
        for (Map.Entry<String, JCheckBox> entry : yearCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedYears.add(entry.getKey());
            }
        }
        
        Set<String> selectedColleges = new HashSet<>();
        for (Map.Entry<String, JCheckBox> entry : collegeCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedColleges.add(entry.getKey());
            }
        }
        
        Set<String> selectedPrograms = new HashSet<>();
        for (Map.Entry<String, JCheckBox> entry : programCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selectedPrograms.add(entry.getKey());
            }
        }
        
        // Apply filters to the table
        JTable table = tablePanel.getTable();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        // Create a composite row filter that combines all our filter criteria
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        
        // Gender filter (column 2)
        if (!selectedGenders.isEmpty() && selectedGenders.size() < genderCheckboxes.size()) {
            filters.add(RowFilter.orFilter(selectedGenders.stream()
                .map(gender -> RowFilter.regexFilter("^" + gender + "$", 2))
                .toList()));
        }
        
        // Year Level filter (column 4)
        if (!selectedYears.isEmpty() && selectedYears.size() < yearCheckboxes.size()) {
            filters.add(RowFilter.orFilter(selectedYears.stream()
                .map(year -> RowFilter.regexFilter("^" + year + ".*", 4))
                .toList()));
        }
        
        // College filter (column 5)
        if (!selectedColleges.isEmpty() && selectedColleges.size() < collegeCheckboxes.size()) {
            filters.add(RowFilter.orFilter(selectedColleges.stream()
                .map(college -> RowFilter.regexFilter("^" + Pattern.quote(college) + "$", 5))
                .toList()));
        }
        
        // Program filter (column 6)
        if (!selectedPrograms.isEmpty() && selectedPrograms.size() < programCheckboxes.size()) {
            filters.add(RowFilter.orFilter(selectedPrograms.stream()
                .map(program -> RowFilter.regexFilter("^" + Pattern.quote(program) + "$", 6))
                .toList()));
        }
        
        // Combine all filters with AND logic
        if (!filters.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else {
            sorter.setRowFilter(null); // No filters, show all rows
        }
    }
    
    private Set<String> getAvailableColleges() {
        // Get unique colleges from the table data
        Set<String> colleges = new HashSet<>();
        
        JTable table = tablePanel.getTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        for (int row = 0; row < model.getRowCount(); row++) {
            String college = (String) model.getValueAt(row, 5); // College is at index 5
            if (college != null && !college.isEmpty()) {
                colleges.add(college);
            }
        }
        
        // If no colleges found, add some default values
        if (colleges.isEmpty()) {
            colleges.add("College of Engineering");
            colleges.add("College of Science");
            colleges.add("College of Arts and Letters");
            colleges.add("College of Business Administration");
        }
        
        return colleges;
    }
    
    private Set<String> getAvailablePrograms() {
        // Get unique programs from the table data
        Set<String> programs = new HashSet<>();
        
        JTable table = tablePanel.getTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        for (int row = 0; row < model.getRowCount(); row++) {
            String program = (String) model.getValueAt(row, 6); // Program is at index 6
            if (program != null && !program.isEmpty()) {
                programs.add(program);
            }
        }
        
        // If no programs found, add some default values
        if (programs.isEmpty()) {
            programs.add("Computer Science");
            programs.add("Civil Engineering");
            programs.add("Business Administration");
            programs.add("Psychology");
        }
        
        return programs;
    }
}