import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class HoverTooltipTableRenderer extends DefaultTableCellRenderer {
    private int hoveredRow = -1;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        
        if (row == hoveredRow && !isSelected) {
            setBackground(new Color(0xF5F5F5));
        } else if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        
        if (column == 6 || column == 7) {
            String abbreviation = value != null ? value.toString() : "";
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
    
    public void setHoveredRow(int row) {
        this.hoveredRow = row;
    }
}