import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CProgramTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> sortByBox;
    private JButton sortButton;
    private SortOrder currentSortOrder = SortOrder.DESCENDING;

    public CProgramTable() {
        setLayout(new BorderLayout());

        String[] columns = {"Program Name", "Program Code", "College Code"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        loadProgramData();
    }
    
    private void loadProgramData() {
        tableModel.setRowCount(0);
        
        List<String[]> programs = new ArrayList<>();
        
        try {
   
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("Programs.csv"));
            String line;
          
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                if (values.length >= 2 && values[1].startsWith("\"") && values[1].endsWith("\"")) {
                    values[1] = values[1].substring(1, values[1].length() - 1);
                }
                
                Object[] rowData = {values[1], values[0], values[2]};
                tableModel.addRow(rowData);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error loading program data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void refreshData() {
        loadProgramData();
    }
    
    public JTable getTable() {
        return table;
    }
 
    public String getSelectedProgramCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 1);
        }
        return null;
    }
    
    public String getSelectedCollegeCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 2);
        }
        return null;
    }
    
    public String getSelectedProgramName(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 0);
        }
        return null;
    }
}