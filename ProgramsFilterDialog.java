import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ProgramsFilterDialog extends JDialog {
    private final ProgramTablePanel programTablePanel;
    private JComboBox<String> collegeComboBox;
    
    public ProgramsFilterDialog(Window parent, ProgramTablePanel programTablePanel) {
        super(parent, "Filter Programs");
        this.programTablePanel = programTablePanel;
        
        initComponents();
        
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(parent);
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
        
        // College filter
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel collegeLabel = new JLabel("College:");
        collegeLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
        filterPanel.add(collegeLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        // Create a sorted list of colleges with "All Colleges" first
        List<String> colleges = CollegeDataManager.getAllColleges();
        Collections.sort(colleges);
        colleges.add(0, "All Colleges");
        
        collegeComboBox = new JComboBox<>(colleges.toArray(new String[0]));
        collegeComboBox.setFont(new Font("Helvetica", Font.PLAIN, 14));
        filterPanel.add(collegeComboBox, gbc);
        
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
        
        add(filterPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void resetFilters() {
        collegeComboBox.setSelectedIndex(0);
    }
    
    private void applyFilters() {
        String selectedCollege = (String) collegeComboBox.getSelectedItem();
        
        JTable table = programTablePanel.getProgramTable();
        
        // Reset to first page when filtering
        programTablePanel.refreshProgramTable();
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
            
        // College filter (column 0 in program table)
        if (selectedCollege != null && !selectedCollege.equals("All Colleges")) {
            // Get abbreviation if filtering by full name
            String collegeAbbr = CollegeDataManager.getCollegeAbbr(selectedCollege);
            sorter.setRowFilter(RowFilter.regexFilter("^" + collegeAbbr.trim() + "$", 0));
        } else {
            sorter.setRowFilter(null);
        }
    }
    public void refreshCollegeList() {
        // Get the current selection to restore it after refresh
        String currentSelection = (String) collegeComboBox.getSelectedItem();
        
        // Create a sorted list of colleges with "All Colleges" first
        List<String> colleges = CollegeDataManager.getAllColleges();
        Collections.sort(colleges);
        colleges.add(0, "All Colleges");
        
        // Update the combo box model
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(colleges.toArray(new String[0]));
        collegeComboBox.setModel(model);
        
        // Restore previous selection if it still exists
        if (currentSelection != null && colleges.contains(currentSelection)) {
            collegeComboBox.setSelectedItem(currentSelection);
        } else {
            collegeComboBox.setSelectedIndex(0); // Default to "All Colleges"
        }
    }
}