import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ProgramTableUtility {

    /**
     * Populates an existing JTable with program data
     * @param table The JTable to populate
     * @return The populated JTable
     */
    public static JTable populateProgramTable(JTable table) {
        // Create table model with column names
        String[] columnNames = {"College Code", "Program Name", "Program Code"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        // Get program data
        java.util.List<ProgramEntry> programData = loadProgramData();
        
        // Populate table with program data
        for (ProgramEntry entry : programData) {
            model.addRow(new Object[]{
                entry.getCollegeCode(),
                entry.getProgramName(),
                entry.getProgramCode()
            });
        }
        
        // Set the model to the table
        table.setModel(model);
        
        // Add sorting capability
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        // Set column widths for better display
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        return table;
    }
    
    /**
     * Creates a panel containing a JTable with program data
     * @return A JPanel containing the program data table
     */
    public static JPanel createProgramTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create a JTable
        JTable table = new JTable();
        
        // Populate the table
        populateProgramTable(table);
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add a search panel at the top
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Load program data using the CollegeManager class
     * @return List of program entries
     */
    private static java.util.List<ProgramEntry> loadProgramData() {
        java.util.List<ProgramEntry> programEntries = new ArrayList<>();
        
        // Use the CollegeManager to load college data
        Map<String, Map<String, Object>> collegeData = CollegeManager.loadColleges();
        
        for (Map.Entry<String, Map<String, Object>> collegeEntry : collegeData.entrySet()) {
            String collegeName = collegeEntry.getKey();
            String collegeCode = (String) collegeEntry.getValue().get("abbreviation");
            
            @SuppressWarnings("unchecked")
            Map<String, String> programs = (Map<String, String>) collegeEntry.getValue().get("programs");
            
            if (programs != null) {
                for (Map.Entry<String, String> programEntry : programs.entrySet()) {
                    String programName = programEntry.getKey();
                    String programCode = programEntry.getValue();
                    
                    programEntries.add(new ProgramEntry(collegeCode, programName, programCode));
                }
            }
        }
        
        return programEntries;
    }
    
    /**
     * Helper class to store program entry information
     */
    public static class ProgramEntry {
        private final String collegeCode;
        private final String programName;
        private final String programCode;
        
        public ProgramEntry(String collegeCode, String programName, String programCode) {
            this.collegeCode = collegeCode;
            this.programName = programName;
            this.programCode = programCode;
        }
        
        public String getCollegeCode() {
            return collegeCode;
        }
        
        public String getProgramName() {
            return programName;
        }
        
        public String getProgramCode() {
            return programCode;
        }
    }
    
    // Example usage in an existing frame/panel:
    /*
    // Option 1: If you already have a JTable
    JTable myExistingTable = new JTable();
    myExistingTable = ProgramTableUtility.populateProgramTable(myExistingTable);
    JScrollPane scrollPane = new JScrollPane(myExistingTable);
    myPanel.add(scrollPane);
    
    // Option 2: If you want a complete panel with the table
    JPanel programTablePanel = ProgramTableUtility.createProgramTablePanel();
    myFrame.add(programTablePanel);
    */
}