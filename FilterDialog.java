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
    private Map<String, String[]> collegePrograms;
    
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
        collegePrograms = new HashMap<>();
        collegePrograms.put("All Colleges", new String[]{"All Programs"});
        collegePrograms.put("College of Computer Studies", new String[]{
            "All Programs",
            "Bachelor of Science in Computer Science",
            "Bachelor of Science in Information Technology",
            "Bachelor of Science in Information Systems",
            "Bachelor of Science in Computer Application"
        });
        collegePrograms.put("College of Engineering", new String[]{
            "All Programs",
            "Diploma in Chemical Engineering Technology",
            "Bachelor of Science in Ceramic Engineering",
            "Bachelor of Science in Civil Engineering",
            "Bachelor of Science in Electrical Engineering",
            "Bachelor of Science in Mechanical Engineering",
            "Bachelor of Science in Chemical Engineering",
            "Bachelor of Science in Metallurgical Engineering",
            "Bachelor of Science in Computer Engineering",
            "Bachelor of Science in Mining Engineering",
            "Bachelor of Science in Electronics & Communications Engineering",
            "Bachelor of Science in Environmental Engineering"
        });
        collegePrograms.put("College of Science and Mathematics", new String[]{
            "All Programs",
            "Bachelor of Science in Biology (Botany)",
            "Bachelor of Science in Chemistry",
            "Bachelor of Science in Mathematics",
            "Bachelor of Science in Physics",
            "Bachelor of Science in Biology (Zoology)",
            "Bachelor of Science in Biology (Marine)",
            "Bachelor of Science in Biology (General)",
            "Bachelor of Science in Statistics"
        });
        collegePrograms.put("College of Economics and Business Accountancy", new String[]{
            "All Programs",
            "Bachelor of Science in Accountancy",
            "Bachelor of Science in Business Administration (Business Economics)",
            "Bachelor of Science in Business Administration (Marketing Management)",
            "Bachelor of Science in Entrepreneurship",
            "Bachelor of Science in Hospitality Management"
        });
        collegePrograms.put("College of Arts and Social Sciences", new String[]{
            "All Programs",
            "Bachelor of Arts in English Language Studies",
            "Bachelor of Arts in Literary and Cultural Studies",
            "Bachelor of Arts in Filipino",
            "Bachelor of Arts in Panitikan",
            "Bachelor of Arts in Political Science",
            "Bachelor of Arts in Psychology",
            "Bachelor of Arts in Sociology",
            "Bachelor of Arts in History (International History Track)",
            "Bachelor of Science in Philosophy",
            "Bachelor of Science in Psychology"
        });
        collegePrograms.put("College of Education", new String[]{
            "All Programs",
            "Bachelor of Elementary Education (Science and Mathematics)",
            "Bachelor of Elementary Education (Language Education)",
            "Bachelor of Secondary Education (Biology)",
            "Bachelor of Secondary Education (Chemistry)",
            "Bachelor of Secondary Education (Physics)",
            "Bachelor of Secondary Education (Mathematics)",
            "Bachelor of Physical Education",
            "Bachelor of Technology and Livelihood Education (Home Economics)",
            "Bachelor of Technology and Livelihood Education (Industrial Arts)",
            "Bachelor of Technical-Vocational Teacher Education (Drafting Technology)"
        });
        collegePrograms.put("College of Health Sciences", new String[]{
            "All Programs",
            "Bachelor of Science in Nursing"
        });
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
        
        // Gender filter
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
        
        // Year Level filter
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
        String[] colleges = collegePrograms.keySet().toArray(new String[0]);
        Arrays.sort(colleges);
        // Put "All Colleges" at the beginning
        String[] finalColleges = new String[colleges.length];
        finalColleges[0] = "All Colleges";
        int index = 1;
        for (String college : colleges) {
            if (!college.equals("All Colleges")) {
                finalColleges[index++] = college;
            }
        }
        collegeComboBox = new JComboBox<>(finalColleges);
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
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = createStyledButton("Apply Filters", Color.BLACK);
        JButton resetButton = createStyledButton("Reset Filters", Color.BLACK);
        JButton cancelButton = createStyledButton("Cancel", Color.BLACK);
        
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
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Helvetica", Font.PLAIN, 14));
        button.setBackground(backgroundColor);
        if (backgroundColor.equals(new Color(0x6DBECA))) {
            button.setForeground(Color.WHITE);
        } else {
            button.setForeground(Color.BLACK);
        }
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
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
        
        JTable table = tablePanel.getTable();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        
        // Add gender filter if not "All Genders"
        if (selectedGender != null && !selectedGender.equals("All Genders")) {
            filters.add(RowFilter.regexFilter("^" + selectedGender + "$", 2));
        }
        
        // Add year level filter if not "All Year Levels"
        if (selectedYearLevel != null && !selectedYearLevel.equals("All Year Levels")) {
            filters.add(RowFilter.regexFilter("^" + selectedYearLevel + "$", 4));
        }
        
        // Add college filter if not "All Colleges"
        if (selectedCollege != null && !selectedCollege.equals("All Colleges")) {
            filters.add(RowFilter.regexFilter("^" + selectedCollege + "$", 5));
        }
        
        if (selectedProgram != null && !selectedProgram.equals("All Programs")) {
            filters.add(RowFilter.regexFilter("^" + selectedProgram + "$", 6));
        }
        
        if (!filters.isEmpty()) {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else {
            sorter.setRowFilter(null);
        }
    }
}