import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class TablePanel extends JPanel {
    public TablePanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(0xE7E7E7));

        GridBagConstraints gbcRow2 = new GridBagConstraints();
        gbcRow2.fill = GridBagConstraints.BOTH;
        gbcRow2.weightx = 1.0;

        JPanel topRow = new JPanel();
        topRow.setBackground(new Color(0xE7E7E7));
        topRow.setPreferredSize(new Dimension(1, 100)); // Ensures it has height
        gbcRow2.gridy = 0;
        gbcRow2.weighty = 0.2; // Takes 20% of row2's height
        this.add(topRow, gbcRow2);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(new Color(0xE7E7E7));
        gbcRow2.gridy = 1;
        gbcRow2.weighty = 0.8; // Takes 80% of row2's height
        this.add(bottomRow, gbcRow2);

        RoundedPanel tablePanel = new RoundedPanel(10);
        tablePanel.setBackground(new Color(0xffffff));
        tablePanel.setPreferredSize(new Dimension(1155, 800));

        //row2.add(tablePanel);

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
        table.setFillsViewportHeight(true);

        RoundedScrollPane scrollPane = new RoundedScrollPane(table, 10);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
         header.setBorder(BorderFactory.createEmptyBorder());
        header.setOpaque(false);
        header.setBackground(table.getBackground());
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        HoverTableRenderer hoverRenderer = new HoverTableRenderer();
        table.setDefaultRenderer(Object.class, hoverRenderer);

        JPanel tableContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tableContainer.setOpaque(false); // Make it transparent
        tableContainer.add(tablePanel);
        bottomRow.add(tableContainer, BorderLayout.CENTER);
        this.add(bottomRow, gbcRow2);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                hoverRenderer.setHoveredRow(row);
                table.repaint();
            }
        });
        
        // Removed redundant reinitializations
    }
}
