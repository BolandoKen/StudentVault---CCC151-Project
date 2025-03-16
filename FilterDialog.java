import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FilterDialog extends JDialog {
    private final TablePanel tablePanel;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> yearLevelComboBox;
    private JComboBox<String> collegeComboBox;
    private JComboBox<String> programComboBox;
    private Map<String, List<String>> collegePrograms;
    
    public FilterDialog(Window parent, TablePanel tablePanel) {
        super(parent, "Filter Students");
        this.tablePanel = tablePanel;
        
        initializeCollegePrograms();
        initComponents();
        
        setSize(600, 500);
        setResizable(true);
        setLocationRelativeTo(parent);
    }
    
    private void initializeCollegePrograms() {
        // Make sure the data is loaded
        CollegeDataManager.loadFromCSV();
        
        collegePrograms = new HashMap<>();
        
        // Add "All Colleges" as default option
        collegePrograms.put("All Colleges", Collections.singletonList("All Programs"));
        
        // Get all colleges from CollegeDataManager
        List<String> colleges = CollegeDataManager.getAllColleges();
        
        // For each college, get its programs
        for (String college : colleges) {
            List<String> programs = new ArrayList<>();
            programs.add("All Programs"); // Add default option
            programs.addAll(CollegeDataManager.getProgramsForCollege(college)); // Add all programs for this college
            
            // Store in our map
            collegePrograms.put(college, programs);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        filterPanel.add(genderLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        genderComboBox = new JComboBox<>(new String[]{"All Genders", "Male", "Female", "Other"});
        genderComboBox.setFont(new Font("Helvetica", Font.PLAIN, 14));
        filterPanel.add(genderComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel yearLabel = new JLabel("Year Level:");
        yearLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        filterPanel.add(yearLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        yearLevelComboBox = new JComboBox<>(new String[]{
            "All Year Levels", "1st Year", "2nd Year", "3rd Year", "4th Year"
        });
        yearLevelComboBox.setFont(new Font("Helvetica", Font.PLAIN, 14));
        filterPanel.add(yearLevelComboBox, gbc);
        
        // College filter
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel collegeLabel = new JLabel("College:");
        collegeLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        filterPanel.add(collegeLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        // Create a sorted list of colleges with "All Colleges" first
        List<String> sortedColleges = new ArrayList<>(collegePrograms.keySet());
        sortedColleges.remove("All Colleges");
        Collections.sort(sortedColleges);
        sortedColleges.add(0, "All Colleges");
        
        collegeComboBox = new JComboBox<>(sortedColleges.toArray(new String[0]));
        collegeComboBox.setFont(new Font("Helvetica", Font.PLAIN, 14));
        filterPanel.add(collegeComboBox, gbc);
        
        // Program filter
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel programLabel = new JLabel("Program:");
        programLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        filterPanel.add(programLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        programComboBox = new JComboBox<>(new String[]{"All Programs"});
        programComboBox.setFont(new Font("Helvetica", Font.PLAIN, 14));
        filterPanel.add(programComboBox, gbc);
        
        collegeComboBox.addActionListener(e -> {
            String selectedCollege = (String) collegeComboBox.getSelectedItem();
            updateProgramComboBox(selectedCollege);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        RoundedButton applyButton = new RoundedButton(
            "Apply Filters",
            Color.decode("#6DBECA"),  
            Color.WHITE,             
            new Font("Helvetica", Font.PLAIN, 18),
            10  
        );
        RoundedButton resetButton = new RoundedButton(
            "Reset",
            Color.white, 
            Color.black,             
            new Font("Helvetica", Font.PLAIN, 18), 
            10  
        );
        RoundedButton cancelButton = new RoundedButton(
            "Cancel",
            Color.decode("#5C2434"), 
            Color.WHITE,             
            new Font("Helvetica", Font.PLAIN, 18), 
            10
        );
        applyButton.addActionListener(e -> {
            applyFilters();
            dispose();
        });
        
        resetButton.addActionListener(e -> resetFilters());
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(resetButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        
        JScrollPane scrollPane = new JScrollPane(filterPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateProgramComboBox(String selectedCollege) {
        programComboBox.removeAllItems();
        
        if (selectedCollege != null && collegePrograms.containsKey(selectedCollege)) {
            for (String program : collegePrograms.get(selectedCollege)) {
                programComboBox.addItem(program);
            }
        } else {
            programComboBox.addItem("All Programs");
        }
    }
    
    private void resetFilters() {
        genderComboBox.setSelectedIndex(0);
        yearLevelComboBox.setSelectedIndex(0);
        collegeComboBox.setSelectedIndex(0);
        updateProgramComboBox("All Colleges");
    }
    
    private void applyFilters() {
        String selectedGender = (String) genderComboBox.getSelectedItem();
        String selectedYearLevel = (String) yearLevelComboBox.getSelectedItem();
        String selectedCollege = (String) collegeComboBox.getSelectedItem();
        String selectedProgram = (String) programComboBox.getSelectedItem();
        
        // Debugging logs
        System.out.println("Selected Gender: " + selectedGender);
        System.out.println("Selected Year Level: " + selectedYearLevel);
        System.out.println("Selected College: " + selectedCollege);
        System.out.println("Selected Program: " + selectedProgram);
        
        JTable table = tablePanel.getTable();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        
        // Gender filter (column 3)
        if (selectedGender != null && !selectedGender.equals("All Genders")) {
            filters.add(RowFilter.regexFilter("^" + selectedGender.trim() + "$", 3));
            System.out.println("Added Gender Filter: " + selectedGender);
        }
        
        // Year Level filter (column 5)
        if (selectedYearLevel != null && !selectedYearLevel.equals("All Year Levels")) {
            filters.add(RowFilter.regexFilter("^" + selectedYearLevel.trim() + "$", 5));
            System.out.println("Added Year Level Filter: " + selectedYearLevel);
        }
        
        // College filter (column 6)
        if (selectedCollege != null && !selectedCollege.equals("All Colleges")) {
            // Get abbreviation if filtering by full name
            String collegeAbbr = CollegeDataManager.getCollegeAbbr(selectedCollege);
            filters.add(RowFilter.regexFilter("^" + collegeAbbr.trim() + "$", 6));
            System.out.println("Added College Filter (Abbreviation): " + collegeAbbr);
        }
        
        // Program filter (column 7)
        if (selectedProgram != null && !selectedProgram.equals("All Programs")) {
            // Get abbreviation if filtering by full name
            String programAbbr = CollegeDataManager.getProgramAbbr(selectedProgram);
            filters.add(RowFilter.regexFilter("^" + programAbbr.trim() + "$", 7));
            System.out.println("Added Program Filter (Abbreviation): " + programAbbr);
        }
        
        if (!filters.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filters));
            System.out.println("Filters Applied: " + filters.size());
        } else {
            sorter.setRowFilter(null);
            System.out.println("No Filters Applied");
        }
    }
}