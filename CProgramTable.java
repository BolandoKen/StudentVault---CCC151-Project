import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CProgramTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public CProgramTable() {
        setLayout(new BorderLayout());
        
        // Define column headers
        String[] columns = {"Program Name", "Program Code", "College Code"};
        
        // Create a table model that doesn't allow cell editing
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table with the model
        table = new JTable(tableModel);
        
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load program data
        loadProgramData();
    }
    
    /**
     * Loads program data from CSV file using ProgramDataManager
     */
    private void loadProgramData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Use ProgramDataManager to read program data from CSV
        List<String[]> programs = new ArrayList<>();
        
        try {
            // We need to modify this to use the ProgramDataManager's readCSV method
            // Since it's private in ProgramDataManager, we'll use a different approach
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("Programs.csv"));
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split line by comma, but handle quoted values properly
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Remove quotes from program name if present
                if (values.length >= 2 && values[1].startsWith("\"") && values[1].endsWith("\"")) {
                    values[1] = values[1].substring(1, values[1].length() - 1);
                }
                
                // Add program to table - order is: program_name, program_code, college_code
                Object[] rowData = {values[1], values[0], values[2]};
                tableModel.addRow(rowData);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error loading program data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refreshes table data from the CSV file
     */
    public void refreshData() {
        loadProgramData();
    }
    
    /**
     * Returns the JTable component
     */
    public JTable getTable() {
        return table;
    }
    
    /**
     * Gets the program code from the selected row
     * @param row The selected row index
     * @return The program code or null if invalid row
     */
    public String getSelectedProgramCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 1);
        }
        return null;
    }
    
    /**
     * Gets the college code from the selected row
     * @param row The selected row index
     * @return The college code or null if invalid row
     */
    public String getSelectedCollegeCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 2);
        }
        return null;
    }
    
    /**
     * Gets the program name from the selected row
     * @param row The selected row index
     * @return The program name or null if invalid row
     */
    public String getSelectedProgramName(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 0);
        }
        return null;
    }
}