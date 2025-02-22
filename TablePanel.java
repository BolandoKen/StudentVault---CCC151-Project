import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class TablePanel extends JPanel {
    public TablePanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(0xE7E7E7));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Top row content
        JPanel topRow = new JPanel();
        topRow.setBackground(new Color(0xE7E7E7));
        topRow.setPreferredSize(new Dimension(1, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        this.add(topRow, gbc);

        // Bottom row content (JTable)
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(new Color(0xE7E7E7));
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        this.add(bottomRow, gbc);

        RoundedPanel tablePanel = new RoundedPanel(10);
        tablePanel.setBackground(new Color(0xffffff));

        String[] columnNames = {"ID Number", "Firstname", "Lastname", "Year Level", "College", "Program"};
        Object[][] data = {
            {"2023-1864", "Sheldono Enario", "Bolando", "1st", "CCS", "BSCS"},
            {"2024-1023", "Alexander", "Hamilton", "3rd", "COE", "BSEE"},
            {"2021-5678", "Marie", "Curie", "4th", "COS", "BS Chemistry"},
            {"2020-4321", "Nikola", "Tesla", "4th", "COE", "BSEE"},
            {"2019-8765", "Isaac", "Newton", "4th", "COS", "BS Physics"},
            {"2023-1864", "Lebron James", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Kobe Bryant", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Michael Jordan", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Stephen Curry", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Kevin Durant", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Kawhi Leonard", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "James Harden", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Anthony Davis", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Giannis Antetokounmpo", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Luka Doncic", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Damian Lillard", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Joel Embiid", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Nikola Jokic", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Devin Booker", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Jayson Tatum", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Donovan Mitchell", "Bolando", "1st", "CCS", "BSCS"},
            {"2023-1864", "Jimmy Butler", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Klay Thompson", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Paul George", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Russell Westbrook", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Kyrie Irving", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Zion Williamson", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Carmelo Anthony", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Chris Paul", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Dwyane Wade", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Dirk Nowitzki", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Tim Duncan", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Kevin Garnett", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Ray Allen", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Vince Carter", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Tracy McGrady", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Allen Iverson", "Bolando", "1st", "CCS", "BSIT"},
            {"2023-1864", "Shaquille O'Neal", "Bolando", "1st", "CCS", "BSIT"},
            

        };

        JTable table = new JTable(data, columnNames);
        JTableHeader header = table.getTableHeader();
        
        table.setRowHeight(30);
        header.setFont(new Font("Helvetica", Font.BOLD, 18));
        header.setForeground(new Color(0x7E7E7E));
        table.setFont(new Font("Helvetica", Font.PLAIN, 16));

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set preferred column widths dynamically
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(getPreferredColumnWidth(table, i));
        }

        // Dynamically adjust table height
        int tableHeight = table.getRowHeight() * table.getRowCount() + header.getPreferredSize().height;
        table.setPreferredSize(new Dimension(1155, tableHeight));
        table.setMaximumSize(new Dimension(1155, tableHeight));

        header.setBorder(BorderFactory.createEmptyBorder());
        header.setOpaque(false);
        header.setBackground(table.getBackground());

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(header, BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        HoverTableRenderer hoverRenderer = new HoverTableRenderer();
        table.setDefaultRenderer(Object.class, hoverRenderer);

        JPanel tableContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tableContainer.setOpaque(false);
        tableContainer.add(tablePanel);
        bottomRow.add(tableContainer, BorderLayout.CENTER);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                hoverRenderer.setHoveredRow(row);
                table.repaint();
            }
        });
    }

    private int getPreferredColumnWidth(JTable table, int columnIndex) {
        int maxWidth = 50;
        for (int row = 0; row < table.getRowCount(); row++) {
            Object value = table.getValueAt(row, columnIndex);
            if (value != null) {
                int width = table.getFontMetrics(table.getFont()).stringWidth(value.toString());
                maxWidth = Math.max(maxWidth, width + 20);
            }
        }
        return maxWidth;
    }
}
