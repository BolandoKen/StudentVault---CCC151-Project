import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class TablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

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

        // Create a refresh button
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        refreshButton.setBackground(Color.BLUE);
        refreshButton.setForeground(Color.black);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add action listener to refresh table
        refreshButton.addActionListener(e -> {
            System.out.println("Refreshing table...");
            refreshTable();
        });

        // Add the button to the topRow panel
        topRow.add(refreshButton);


        // Bottom row content (JTable)
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(new Color(0xE7E7E7));
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        this.add(bottomRow, gbc);

        RoundedPanel tablePanel = new RoundedPanel(10);
        tablePanel.setBackground(new Color(0xffffff));

        String[] columnNames = {"Firstname", "Lastname", "Gender", "Id Number", "Year Level", "College", "Program"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Create table with the model
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        
        // Set table properties
        table.setRowHeight(30);
        header.setFont(new Font("Helvetica", Font.BOLD, 18));
        header.setForeground(new Color(0x7E7E7E));
        table.setFont(new Font("Helvetica", Font.PLAIN, 16));

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Add sorting capability
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(1155, 400));

        // Set column widths
        int[] columnWidths = {150, 150, 100, 150, 100, 250, 250};
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
        }

        refreshTable();

        header.setBorder(BorderFactory.createEmptyBorder());
        header.setOpaque(false);
        header.setBackground(table.getBackground());

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
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
    

    public void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            model.setRowCount(0); // Clear table
            List<Student> students = StudentManager.loadStudents();
    
            System.out.println("Students loaded: " + students.size()); // Debugging
    
            for (Student student : students) {
                model.addRow(new Object[]{
                    student.getFirstName(),
                    student.getLastName(),
                    student.getGender(),
                    student.getIdNumber(),
                    student.getYearLevel(),
                    student.getCollege(),
                    student.getProgram()
                });
            }
    
            model.fireTableDataChanged(); // Notify JTable model
    
            // Refresh UI elements
            revalidate();
            repaint();
    
            // If inside a JScrollPane, refresh its parent
            Container parent = getParent();
            while (parent != null) {
                parent.revalidate();
                parent.repaint();
                parent = parent.getParent();
            }
        });
    }
    

    // Method to get the table (useful if you need to access it from outside)
    public JTable getTable() {
        return table;
    }
}