import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class CollegeTableUtility {

    /**
     * Populates an existing JTable with college data (name and code)
     * @param table The JTable to populate
     * @return The populated JTable
     */
    public static JTable populateCollegeTable(JTable table) {
        // Create table model with column names
        String[] columnNames = {"College Name", "College Code"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        // Get college data from CollegeManager
        Map<String, Map<String, Object>> collegeData = CollegeManager.loadColleges();
        
        // Populate table with college data
        for (Map.Entry<String, Map<String, Object>> entry : collegeData.entrySet()) {
            String collegeName = entry.getKey();
            String collegeCode = (String) entry.getValue().get("abbreviation");
            
            // Add row to table model
            model.addRow(new Object[]{collegeName, collegeCode});
        }
        
        // Set the model to the table
        table.setModel(model);
        
        // Set column widths for better display
        table.getColumnModel().getColumn(0).setPreferredWidth(400);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        
        return table;
    }
    
    /**
     * Creates a panel containing a JTable with college data
     * @return A JPanel containing the college data table
     */
    public static JPanel createCollegeTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Create a JTable
        JTable table = new JTable();
        
        // Populate the table
        populateCollegeTable(table);
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Example usage in an existing frame/panel:
    /*
    // Option 1: If you already have a JTable
    JTable myExistingTable = new JTable();
    myExistingTable = CollegeTableUtility.populateCollegeTable(myExistingTable);
    JScrollPane scrollPane = new JScrollPane(myExistingTable);
    myPanel.add(scrollPane);
    
    // Option 2: If you want a complete panel with the table
    JPanel collegeTablePanel = CollegeTableUtility.createCollegeTablePanel();
    myFrame.add(collegeTablePanel);
    */
}