import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SearchPanel extends JPanel {
    private TablePanel tablePanel;
    private RoundedTextField searchField;
    
    public SearchPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 0);
        gbc.weightx = 1.0;

        RoundedPanel searchPanel = new RoundedPanel(10);
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(new Color(0xE7E7E7));

        searchField = new RoundedTextField(200, 10, "Search", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        JButton clearButton = createButton("Assets/XIcon.png");

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch(searchField.getText());
            }
        });
        
        clearButton.addActionListener(e -> {
            searchField.setText("");
            performSearch("");
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        this.add(searchPanel, gbc);

        // Filter Button
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 0, 10, 10);
        JButton filterButton = createButton("Assets/FilterIcon.png");
        filterButton.addActionListener(e -> showFilterDialog());
        this.add(filterButton, gbc);
    }

    private JButton createButton(String iconPath) {
        JButton button = new JButton(new ImageIcon(iconPath));
        button.setPreferredSize(new Dimension(40, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }
    
    public void setTablePanel(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }
    
    private void performSearch(String searchText) {
        if (tablePanel == null) return;
        
        JTable table = tablePanel.getTable();
        if (table == null) return;
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        
        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }
    
    private void showFilterDialog() {
        if (tablePanel == null) return;
        
        FilterDialog dialog = new FilterDialog(SwingUtilities.getWindowAncestor(this), tablePanel);
        dialog.setVisible(true);
    }
}