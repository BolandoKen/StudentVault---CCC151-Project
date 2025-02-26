import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public final class TablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private StudentForm studentForm;
    private RoundedComboBox sortOrderBox;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;

    public TablePanel() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(1, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        this.add(topRow, gbc);

        GridBagConstraints gbcTopRow = new GridBagConstraints();
        gbcTopRow.fill = GridBagConstraints.BOTH;
        gbcTopRow.gridy = 0; 
        gbcTopRow.weightx = 0.5;
        gbcTopRow.weighty = 1.0; 
        gbcTopRow.anchor = GridBagConstraints.SOUTH; 

        gbcTopRow.gridx = 0;
        gbcTopRow.weightx = 0.5;
        gbcTopRow.anchor = GridBagConstraints.WEST; 
        JPanel leftPanel = new JPanel(new BorderLayout());
        topRow.add(leftPanel, gbcTopRow);

        final JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sortPanel.setOpaque(false);
        leftPanel.add(sortPanel, BorderLayout.SOUTH);

        gbcTopRow.gridx = 1;
        gbcTopRow.weightx = 0.5; 
        gbcTopRow.anchor = GridBagConstraints.EAST; 
        JPanel rightPanel = new JPanel(new BorderLayout());
        topRow.add(rightPanel, gbcTopRow);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, -5));
        buttonsPanel.setOpaque(false);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JLabel studentVaultText = new JLabel("StudentVault");
        studentVaultText.setFont(new Font("Helvetica", Font.BOLD, 32));
        JLabel sortbytext = new JLabel("Sort by: ");
        sortbytext.setFont(new Font("Helvetica", Font.PLAIN, 16));
        sortbytext.setForeground(new Color(0x7E7E7E));

        JButton editButton = new JButton(new ImageIcon("Assets/EditIcon.png"));
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setContentAreaFilled(false);
        editButton.addActionListener(e -> editSelectedStudent());

        deleteButton = new JButton(new ImageIcon("Assets/DeleteIcon.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.addActionListener(e -> toggleSelectionMode());

        // Create buttons for multiple delete mode (initially not visible)
        cancelButton = new JButton("Cancel");
        cancelButton.setVisible(false);
        cancelButton.addActionListener(e -> toggleSelectionMode());

        confirmDeleteButton = new JButton("Delete Selected");
        confirmDeleteButton.setVisible(false);
        confirmDeleteButton.addActionListener(e -> deleteSelectedStudents());

        // Create custom styled RoundedComboBoxes
        String[] sortCategories = {"First Name", "Last Name", "Gender", "ID Number", "Year Level", "College", "Program"};
        final Color backgroundColor = new Color(0xFFFFFF);
        final Color textColor = new Color(0x7E7E7E);
        final Font comboFont = new Font("Helvetica", Font.PLAIN, 16);
        final int cornerRadius = 10;
        final Dimension comboSize = new Dimension(160, 28);
        final Color selectionColor = new Color(0x658CF1);
        
        // Replace standard JComboBox with RoundedComboBox
        final RoundedComboBox sortCategoryBox = new RoundedComboBox(
            sortCategories,
            backgroundColor,
            textColor,
            comboFont,
            cornerRadius,
            comboSize,
            "Select Category",
            selectionColor
        );

        sortOrderBox = new RoundedComboBox(
            new String[]{},
            backgroundColor,
            textColor,
            comboFont,
            cornerRadius,
            comboSize,
            "Select Order",
            selectionColor
        );
        sortPanel.add(studentVaultText);
        sortPanel.add(sortbytext);
        sortPanel.add(sortCategoryBox);
        sortPanel.add(sortOrderBox);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(confirmDeleteButton);

        sortOrderBox.addActionListener(orderEvent -> {
            if (sortOrderBox.getSelectedIndex() > 0 || 
                (sortOrderBox.getSelectedIndex() == 0 && !sortOrderBox.getItemAt(0).equals("Select Order"))) {
                String category = (String) sortCategoryBox.getSelectedItem();
                String order = (String) sortOrderBox.getSelectedItem();
                if (category != null && order != null) {
                    sortTable(category, order);
                }
            }
        });

        sortCategoryBox.addActionListener(e -> {
            if (sortCategoryBox.getSelectedIndex() == 0 && sortCategoryBox.getItemAt(0).equals("Select Category")) {
                return;
            }
            
            String selectedCategory = (String) sortCategoryBox.getSelectedItem();
            updateSortOrderOptions(selectedCategory, sortPanel, backgroundColor, textColor, comboFont, cornerRadius, comboSize, selectionColor);
        });

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        this.add(bottomRow, gbc);

        RoundedPanel tablePanel = new RoundedPanel(10);
        tablePanel.setBackground(new Color(0xffffff));
        // Use BorderLayout for the table panel to allow full expansion
        tablePanel.setLayout(new BorderLayout());
        // Set minimum size but not preferred size to allow expansion
        tablePanel.setMinimumSize(new Dimension(800, 400));

        // Modified model with checkbox column
        String[] columnNames = {"", "Firstname", "Lastname", "Gender", "Id Number", "Year Level", "College", "Program"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only checkbox column is editable
                return column == 0 && selectionMode; 
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                // Make the first column appear as checkboxes
                return column == 0 ? Boolean.class : Object.class;
            }
        };

        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        
        table.setRowHeight(30);
        header.setFont(new Font("Helvetica", Font.BOLD, 18));
        header.setForeground(new Color(0x7E7E7E));
        table.setFont(new Font("Helvetica", Font.PLAIN, 16));

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setBorder(null);
        header.setBorder(null);

        // Set the checkbox column width
        TableColumn checkboxColumn = table.getColumnModel().getColumn(0);
        checkboxColumn.setMaxWidth(30);
        checkboxColumn.setMinWidth(30);
        checkboxColumn.setPreferredWidth(30);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Add table directly without scroll pane
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set relative column widths (adjusted for checkbox column)
        int[] columnWidthPercentages = {3, 14, 14, 10, 14, 10, 17, 18}; // Percentages should add up to 100
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        table.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int totalWidth = table.getWidth();
                TableColumn column;
                
                for (int i = 0; i < table.getColumnCount(); i++) {
                    column = table.getColumnModel().getColumn(i);
                    int width = (int)(totalWidth * (columnWidthPercentages[i] / 100.0));
                    column.setPreferredWidth(width);
                }
            }
        });

        header.setBorder(BorderFactory.createEmptyBorder());
        header.setOpaque(false);
        header.setBackground(table.getBackground());

        // Custom renderer for hover effects (with checkbox support)
        HoverTableRenderer hoverRenderer = new HoverTableRenderer();
        table.setDefaultRenderer(Object.class, hoverRenderer);

        // Use GridBagLayout for the tableContainer to allow for expansion
        JPanel tableContainer = new JPanel(new GridBagLayout());
        tableContainer.setOpaque(false);
        
        GridBagConstraints tablePanelConstraints = new GridBagConstraints();
        tablePanelConstraints.gridx = 0;
        tablePanelConstraints.gridy = 0;
        tablePanelConstraints.weightx = 1.0;
        tablePanelConstraints.weighty = 1.0;
        tablePanelConstraints.fill = GridBagConstraints.BOTH;
        tablePanelConstraints.insets = new Insets(10, 10, 10, 10);
        
        tableContainer.add(tablePanel, tablePanelConstraints);
        bottomRow.add(tableContainer, BorderLayout.CENTER);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                hoverRenderer.setHoveredRow(row);
                table.repaint();
            }
        });
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !selectionMode) {
                    editSelectedStudent();
                }
            }
        });
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r >= 0 && r < table.getRowCount()) {
                    table.setRowSelectionInterval(r, r);
                } else {
                    table.clearSelection();
                }

                if (e.isPopupTrigger() && e.getComponent() instanceof JTable && !selectionMode) {
                    JPopupMenu popup = createPopupMenu();
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable && !selectionMode) {
                    JPopupMenu popup = createPopupMenu();
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        refreshTable();
    }
    
    private void toggleSelectionMode() {
        selectionMode = !selectionMode;
        
        // Toggle visibility of buttons
        deleteButton.setVisible(!selectionMode);
        cancelButton.setVisible(selectionMode);
        confirmDeleteButton.setVisible(selectionMode);
        
        // Refresh table to update checkbox state
        if (!selectionMode) {
            // Reset all checkboxes when exiting selection mode
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(false, i, 0);
            }
        }
        
        // Enable or disable the checkbox column
        table.getColumnModel().getColumn(0).setMinWidth(selectionMode ? 30 : 0);
        table.getColumnModel().getColumn(0).setMaxWidth(selectionMode ? 30 : 0);
        table.getColumnModel().getColumn(0).setPreferredWidth(selectionMode ? 30 : 0);
        
        // Repaint components
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        table.revalidate();
        table.repaint();
    }
    
    private void deleteSelectedStudents() {
        List<String> selectedIds = new ArrayList<>();
        
        // Collect all selected student IDs
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                String idNumber = model.getValueAt(i, 4).toString(); // Adjust index to ID column
                selectedIds.add(idNumber);
            }
        }
        
        if (selectedIds.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "No students selected for deletion",
                "Warning",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete " + selectedIds.size() + " selected student(s)?",
            "Confirm Multiple Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            
            // Delete each student
            for (String id : selectedIds) {
                if (StudentManager.deleteStudent(id)) {
                    successCount++;
                }
            }
            
            // Show results
            if (successCount > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    successCount + " student(s) deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Exit selection mode and refresh table
                toggleSelectionMode();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete students",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void updateSortOrderOptions(String selectedCategory, JPanel sortPanel, Color backgroundColor, 
                                       Color textColor, Font comboFont, int cornerRadius, 
                                       Dimension comboSize, Color selectionColor) {
        String[] options;
        switch (selectedCategory) {
            case "First Name":
            case "Last Name":
            case "College":
            case "Program":
                options = new String[]{"A-Z", "Z-A"};
                break;
            case "Gender":
                options = new String[]{"Male → Female", "Female → Male"};
                break;
            case "ID Number":
            case "Year Level":
                options = new String[]{"Ascending", "Descending"};
                break;
            default:
                options = new String[]{};
        }
        
        sortPanel.remove(sortOrderBox);
        
        sortOrderBox = new RoundedComboBox(
            options,
            backgroundColor,
            textColor,
            comboFont,
            cornerRadius,
            comboSize,
            "Select Order",
            selectionColor
        );
        
        final RoundedComboBox finalSortCategoryBox = (RoundedComboBox) sortPanel.getComponent(2);
        sortOrderBox.addActionListener(orderEvent -> {
            if (sortOrderBox.getSelectedIndex() > 0 || 
                (sortOrderBox.getSelectedIndex() == 0 && !sortOrderBox.getItemAt(0).equals("Select Order"))) {
                String category = (String) finalSortCategoryBox.getSelectedItem();
                String order = (String) sortOrderBox.getSelectedItem();
                if (category != null && order != null) {
                    sortTable(category, order);
                }
            }
        });
        
        sortPanel.add(sortOrderBox);
        sortPanel.revalidate();
        sortPanel.repaint();
    }
    
    private JPopupMenu createPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        
        JMenuItem editItem = new JMenuItem("Edit Student");
        editItem.addActionListener(e -> editSelectedStudent());
        popup.add(editItem);
        
        JMenuItem deleteItem = new JMenuItem("Delete Student");
        deleteItem.addActionListener(e -> deleteSelectedStudent());
        popup.add(deleteItem);
        
        return popup;
    }
    
    private void editSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            // Adjust column indices for checkbox column
            String firstName = model.getValueAt(modelRow, 1).toString();
            String lastName = model.getValueAt(modelRow, 2).toString();
            String gender = model.getValueAt(modelRow, 3).toString();
            String idNumber = model.getValueAt(modelRow, 4).toString();
            String yearLevel = model.getValueAt(modelRow, 5).toString();
            String college = model.getValueAt(modelRow, 6).toString();
            String program = model.getValueAt(modelRow, 7).toString();

            Student student = new Student(firstName, lastName, gender, idNumber, yearLevel, college, program);
            
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof GUI) {
                GUI gui = (GUI) frame;
                
                if (studentForm != null) {
                    studentForm.setEditMode(student);
                    gui.switchPanel("ADD_STUDENT");
                } else {
                    Container parent = gui.getContentPane();
                    for (Component comp : parent.getComponents()) {
                        if (comp instanceof JPanel) {
                            findAndSetStudentForm((JPanel) comp, student);
                        }
                    }
                }
            }
        }
    }
    
    private void findAndSetStudentForm(Container container, Student student) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof StudentForm) {
                ((StudentForm) comp).setEditMode(student);
                break;
            } else if (comp instanceof Container) {
                findAndSetStudentForm((Container) comp, student);
            }
        }
    }
    
    private void deleteSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            // Adjust index for checkbox column
            String idNumber = model.getValueAt(modelRow, 4).toString();
            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete student with ID: " + idNumber + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (StudentManager.deleteStudent(idNumber)) {
                    refreshTable();
                    JOptionPane.showMessageDialog(
                        this,
                        "Student deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete student",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    public void refreshTable() {
        model.setRowCount(0); 
        List<Student> students = StudentManager.loadStudents();
    
        System.out.println("Students loaded: " + students.size());
    
        for (Student student : students) {
            model.addRow(new Object[]{
                false, // Checkbox column (initially unchecked)
                student.getFirstName(),
                student.getLastName(),
                student.getGender(),
                student.getIdNumber(),
                student.getYearLevel(),
                student.getCollege(),
                student.getProgram()
            });
        }
    
        model.fireTableDataChanged(); 
    
        // Hide checkbox column when not in selection mode
        if (!selectionMode) {
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    
        revalidate();
        repaint();
    
        Container parent = getParent();
        while (parent != null) {
            parent.revalidate();
            parent.repaint();
            parent = parent.getParent();
        }
    }
    
    public JTable getTable() {
        return table;
    }
    
    public void setStudentForm(StudentForm form) {
        this.studentForm = form;
    }
    
    private void sortTable(String category, String order) {
        if (category == null || order == null) return;
    
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
    
        // Adjust column indices for checkbox column
        int columnIndex = -1;
        switch (category) {
            case "First Name": columnIndex = 1; break;
            case "Last Name": columnIndex = 2; break;
            case "Gender": columnIndex = 3; break;
            case "ID Number": columnIndex = 4; break;
            case "Year Level": columnIndex = 5; break;
            case "College": columnIndex = 6; break;
            case "Program": columnIndex = 7; break;
        }
    
        if (columnIndex == -1) return; 
    
        SortOrder sortOrder;
        if (order.equals("A-Z") || order.equals("Male → Female") || order.equals("Ascending")) {
            sortOrder = SortOrder.ASCENDING;
        } else {
            sortOrder = SortOrder.DESCENDING;
        }
    
        sorter.setSortKeys(List.of(new RowSorter.SortKey(columnIndex, sortOrder)));
        sorter.sort();
    }
}