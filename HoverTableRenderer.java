import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

class HoverTableRenderer extends DefaultTableCellRenderer {
    private int hoveredRow = -1; // Store the hovered row

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (row == hoveredRow) {
            cell.setBackground(new Color(0xc2e7ff)); // Hover color
        } else {
            cell.setBackground(Color.white); // Default color
        }
        
        return cell;
    }

    public void setHoveredRow(int row) {
        this.hoveredRow = row;
    }
}
