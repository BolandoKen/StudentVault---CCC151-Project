import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class GUI extends JFrame {
    public GUI() {
        this.setSize(1280, 832);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(0x5C2434));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // SIDE PANEL - NAVIGATION
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS)); 
        sidePanel.setBackground(new Color(0x5C2434));
        gbc.gridx = 0;
        gbc.weightx = 0.05;
        background.add(sidePanel, gbc);

        ImageIcon SVLogo = new ImageIcon("Assets/StudentVaultLogo.png");
        JLabel logo = new JLabel(SVLogo);
        logo.setPreferredSize(new Dimension(40, 60));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        ImageIcon addIcon = new ImageIcon("Assets/AddIcon.png");
        JButton addButton = new JButton(addIcon);
        addButton.setPreferredSize(new Dimension(40, 40));
        addButton.setBackground(new Color(0x5C2434));
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setMargin(new Insets(20, 0, 20, 0)); 
 
        sidePanel.add(logo);
        sidePanel.add(addButton);  

        // COLUMN 2 - MAIN CONTENT
        JPanel column2 = new JPanel(new GridBagLayout());
        column2.setBackground(new Color(0xE7E7E7));
        gbc.gridx = 1;
        gbc.weightx = 0.95;
        background.add(column2, gbc);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.BOTH;
        gbc2.weightx = 1.0;

        // ROW 1 - Search bar
        JPanel row1 = new JPanel(new GridBagLayout());
        row1.setBackground(Color.white);
        gbc2.gridy = 0;
        gbc2.weighty = 0.02; // Adjusted for better layout control
        column2.add(row1, gbc2);

        // ROW 2 - Main content
        // ROW 2 - Main content with 2 rows
        // ROW 2 - Main content
        JPanel row2 = new JPanel();
        row2.setBackground(new Color(0xE7E7E7));
        row2.setLayout(new GridBagLayout()); // Ensures proper alignment inside the scroll pane

        // Wrap row2 in a JScrollPane
        JScrollPane scrollPaneRow2 = new JScrollPane(row2);
        scrollPaneRow2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scrollbar
        scrollPaneRow2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        scrollPaneRow2.getViewport().setBackground(new Color(0xE7E7E7)); // Keep background consistent
        scrollPaneRow2.setBorder(BorderFactory.createEmptyBorder());

        // Add scrollable row2 to column2
        gbc2.gridy = 1;
        gbc2.weighty = 0.98;
        column2.add(scrollPaneRow2, gbc2);

        GridBagConstraints gbcRow2 = new GridBagConstraints();
        gbcRow2.fill = GridBagConstraints.BOTH;
        gbcRow2.weightx = 1.0;

        // First row of row2 (Red)
        JPanel topRow = new JPanel();
        topRow.setBackground(new Color(0xE7E7E7));
        topRow.setPreferredSize(new Dimension(1, 100)); // Ensures it has height
        gbcRow2.gridy = 0;
        gbcRow2.weighty = 0.2; // Takes 20% of row2's height
        row2.add(topRow, gbcRow2);

        // Second row of row2 (Blue) containing the table panel
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(new Color(0xE7E7E7));
        gbcRow2.gridy = 1;
        gbcRow2.weighty = 0.8; // Takes 80% of row2's height

        RoundedPanel searchPanel = new RoundedPanel(10);
        searchPanel.setBackground(new Color(0xE7E7E7));
        searchPanel.setLayout(new BorderLayout());

        RoundedTextField searchField = new RoundedTextField(200, 10);
        searchField.setBackground(new Color(0xE7E7E7));
        searchField.setFont(new Font("Helvetica", Font.PLAIN, 18));
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); 

        searchField.addActionListener(e -> {
            String query = searchField.getText();
            System.out.println("Searching for: " + query);
        });

        ImageIcon clearIcon = new ImageIcon("Assets/XIcon.png");
        JButton clearButton = new JButton(clearIcon);
        clearButton.setPreferredSize(new Dimension(40, 40));
        clearButton.setBackground(new Color(0x5C2434));
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        clearButton.setContentAreaFilled(false);

        clearButton.addActionListener(e -> searchField.setText(""));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        gbc2.gridx = 0;
        gbc2.weightx = 1.0;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.insets = new Insets(10, 10, 10, 0);
        row1.add(searchPanel, gbc2);

        ImageIcon filterIcon = new ImageIcon("Assets/FilterIcon.png");
        JButton filterButton = new JButton(filterIcon);
        filterButton.setPreferredSize(new Dimension(40, 40));
        filterButton.setBackground(new Color(0x5C2434));
        filterButton.setBorderPainted(false);
        filterButton.setFocusPainted(false);
        filterButton.setContentAreaFilled(false);

        filterButton.setMargin(new Insets(0, 10, 0, 10)); 

        gbc2.gridx = 1;
        gbc2.weightx = 0.0;
        gbc2.insets = new Insets(10, 0, 10, 0);
        row1.add(filterButton, gbc2);

        filterButton.addActionListener(e -> {
            System.out.println("Filtering");
        });

        RoundedPanel tablePanel = new RoundedPanel(10);
        tablePanel.setBackground(new Color(0xffffff));
        tablePanel.setPreferredSize(new Dimension(1155, 800));

        //row2.add(tablePanel);

        String[] columnNames = {"ID Number", "Name", "Year Level", "Gender", "Course", "Collage"};
        Object[][] data = {
            {"20210001", "Juan Dela Cruz", "1st Year", "Male", "BSIT", "CIT"},
            {"20210002", "Maria Clara", "2nd Year", "Female", "BSIT", "CIT"},
            {"20210003", "Pedro Penduko", "3rd Year", "", "BSIT", "CIT"},
            {"20210004", "Juan Tamad", "4th Year", "", "BSIT", "CIT"},
            {"20210005", "Maria Makiling", "1st Year", "", "BSIT", "CIT"},
            {"20210006", "Pedro Calungsod", "2nd Year", "", "BSIT", "CIT"},
            {"20210007", "Juan Luna", "3rd Year", "", "BSIT", "CIT"},
            {"20210008", "Maria Ozawa", "4th Year", "", "BSIT", "CIT"},
            {"20210009", "Pedro Gil", "1st Year", "", "BSIT", "CIT"},
            {"20210010", "Juan Tamad", "2nd Year", "", "BSIT", "CIT"},
            {"20210011", "Maria Makiling", "3rd Year", "", "BSIT", "CIT"},
            {"20210012", "Pedro Calungsod", "4th Year", "", "BSIT", "CIT"},
            {"20210013", "Juan Luna", "1st Year", "", "BSIT", "CIT"},
            {"20210014", "Maria Ozawa", "2nd Year", "", "BSIT", "CIT"},
            {"20210015", "Pedro Gil", "3rd Year", "", "BSIT", "CIT"},
            {"20210016", "Juan Tamad", "4th Year", "", "BSIT", "CIT"},
            {"20210017", "Maria Makiling", "1st Year", "", "BSIT", "CIT"},
            {"20210018", "Pedro Calungsod", "2nd Year", "", "BSIT", "CIT"},
            {"20210019", "Juan Luna", "3rd Year", "", "BSIT", "CIT"},
            {"20210020", "Maria Ozawa", "4th Year", "", "BSIT", "CIT"},
        };

        JTable table = new JTable(data, columnNames);
        JTableHeader header = table.getTableHeader();
        
        table.setRowHeight(30);
        header.setFont(new Font("Helvetica", Font.BOLD, 18));
        header.setForeground(new Color(0x7E7E7E)); // Change to any color you like
        table.setFont(new Font("Helvetica", Font.PLAIN, 16));
        table.setSelectionBackground(Color.black); // Change background when selected
        table.setSelectionForeground(Color.black);         // Change text color when selected
                
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
        row2.add(bottomRow, gbcRow2);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                hoverRenderer.setHoveredRow(row);
                table.repaint();
            }
        });
        
        this.add(background);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
