import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CollegeTableUtility {

    /**
     * Populates an existing JTable with college data (name and code)
     * @param table The JTable to populate
     * @return The populated JTable
     */
   public static JTable populateCollegeTable(JTable table) {
    String[] columnNames = {"College Name", "College Code"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    // Use the new getAllColleges() method
    List<String> colleges = CollegeManager.getAllColleges();
    
    for (String collegeName : colleges) {
        String collegeCode = CollegeManager.getCollegeAbbr(collegeName);
        model.addRow(new Object[]{collegeName, collegeCode});
    }
    
    table.setModel(model);
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