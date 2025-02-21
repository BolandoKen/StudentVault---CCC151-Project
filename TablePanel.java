import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class TablePanel extends JPanel {
    public TablePanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0xE7E7E7));

        String[] columnNames = {"ID Number", "Firstname", "Lastname", "Year Level", "College", "Program"};
        Object[][] data = {
            {"2023-1864", "Jan Atkison", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Jan Atkison", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Jan Atkison", "Bolando", "1st", "CCS", "BSCS"}
        };

        JTable table = new JTable(data, columnNames);
        JTableHeader header = table.getTableHeader();
        
        table.setRowHeight(30);
        header.setFont(new Font("Helvetica", Font.BOLD, 18));
        header.setForeground(new Color(0x7E7E7E));
        table.setFont(new Font("Helvetica", Font.PLAIN, 16));
        
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        HoverTableRenderer hoverRenderer = new HoverTableRenderer();
        table.setDefaultRenderer(Object.class, hoverRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(scrollPane, BorderLayout.CENTER);
    }
}
