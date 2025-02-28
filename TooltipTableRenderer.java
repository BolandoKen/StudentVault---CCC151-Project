import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TooltipTableRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        
        if (column == 6 || column == 7) {
            String abbreviation = value.toString();
            String fullName;
            
            if (column == 6) {
                fullName = CollegeAbbreviationConverter.getFullCollegeName(abbreviation);
            } else {
                fullName = CollegeAbbreviationConverter.getFullProgramName(abbreviation);
            }
            
            if (!abbreviation.equals(fullName)) {
                setToolTipText(fullName);
            } else {
                setToolTipText(null);
            }
        } else {
            setToolTipText(null);
        }
        
        return c;
    }
}