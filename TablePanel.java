import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public final class TablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private StudentForm studentForm; // Reference to the form

    public TablePanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(0xE7E7E7));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Top row content
JPanel topRow = new JPanel(new BorderLayout());
topRow.setBackground(new Color(0xE7E7E7));
topRow.setPreferredSize(new Dimension(1, 100));
gbc.gridx = 0;
gbc.gridy = 0;
gbc.weighty = 0.2;
this.add(topRow, gbc);

// Create a button panel for actions (Refresh, Edit, Delete)
JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
buttonPanel.setBackground(Color.blue); // Match background color

// Create Edit button
RoundedButton editButton = new RoundedButton(
    "Edit Student",
    Color.decode("#F4A261"),  // Orange
    Color.WHITE,
    new Font("Helvetica", Font.PLAIN, 18),
    10  
);
editButton.addActionListener(e -> editSelectedStudent());
buttonPanel.add(editButton);

// Create Delete button
RoundedButton deleteButton = new RoundedButton(
    "Delete Student",
    Color.decode("#E76F51"),  // Red
    Color.WHITE,
    new Font("Helvetica", Font.PLAIN, 18),
    10  
);
deleteButton.addActionListener(e -> deleteSelectedStudent());
buttonPanel.add(deleteButton);

String[] sortCategories = {"First Name", "Last Name", "Gender", "ID Number", "Year Level", "College", "Program"};
JComboBox<String> sortCategoryBox = new JComboBox<>(sortCategories);
sortCategoryBox.setFont(new Font("Helvetica", Font.PLAIN, 16));
buttonPanel.add(new JLabel("Sort by:"));
buttonPanel.add(sortCategoryBox);

JComboBox<String> sortOrderBox = new JComboBox<>();
sortOrderBox.setFont(new Font("Helvetica", Font.PLAIN, 16));
buttonPanel.add(sortOrderBox);

sortCategoryBox.addActionListener(e -> {
    String selectedCategory = (String) sortCategoryBox.getSelectedItem();
    sortOrderBox.removeAllItems();

    switch (selectedCategory) {
        case "First Name":
        case "Last Name":
        case "College":
        case "Program":
            sortOrderBox.addItem("A-Z");
            sortOrderBox.addItem("Z-A");
            break;
        case "Gender":
            sortOrderBox.addItem("Male → Female");
            sortOrderBox.addItem("Female → Male");
            break;
        case "ID Number":
        case "Year Level":
            sortOrderBox.addItem("Ascending");
            sortOrderBox.addItem("Descending");
            break;
    }
});
sortOrderBox.addActionListener(e -> {
    String category = (String) sortCategoryBox.getSelectedItem();
    String order = (String) sortOrderBox.getSelectedItem();
    if (category != null && order != null) {
        sortTable(category, order);
    }
});
topRow.add(buttonPanel, BorderLayout.WEST);

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
        
        // Add double-click listener for editing students
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedStudent();
                }
            }
        });
        
        // Add right-click context menu
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r >= 0 && r < table.getRowCount()) {
                    table.setRowSelectionInterval(r, r);
                } else {
                    table.clearSelection();
                }

                // Right-click context menu
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    JPopupMenu popup = createPopupMenu();
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    JPopupMenu popup = createPopupMenu();
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        refreshTable();
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
            // Convert view row index to model row index
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            // Get values from the selected row
            String firstName = model.getValueAt(modelRow, 0).toString();
            String lastName = model.getValueAt(modelRow, 1).toString();
            String gender = model.getValueAt(modelRow, 2).toString();
            String idNumber = model.getValueAt(modelRow, 3).toString();
            String yearLevel = model.getValueAt(modelRow, 4).toString();
            String college = model.getValueAt(modelRow, 5).toString();
            String program = model.getValueAt(modelRow, 6).toString();
            
            // Create a student object to pass to the form
            Student student = new Student(firstName, lastName, gender, idNumber, yearLevel, college, program);
            
            // Find the GUI instance to switch panels
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof GUI) {
                GUI gui = (GUI) frame;
                
                // If studentForm exists in the GUI, set it to edit mode
                if (studentForm != null) {
                    studentForm.setEditMode(student);
                    gui.switchPanel("ADD_STUDENT");
                } else {
                    // Create a new StudentForm in edit mode if it doesn't exist yet
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
            // Convert view row index to model row index
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            // Get student ID
            String idNumber = model.getValueAt(modelRow, 3).toString();
            
            // Confirm before deletion
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
    
        // Propagate refresh to parent containers
        Container parent = getParent();
        while (parent != null) {
            parent.revalidate();
            parent.repaint();
            parent = parent.getParent();
        }
    }
    
    // Method to get the table (useful if you need to access it from outside)
    public JTable getTable() {
        return table;
    }
    
    // Set the StudentForm to enable communication between components
    public void setStudentForm(StudentForm form) {
        this.studentForm = form;
    }
    private void sortTable(String category, String order) {
        if (category == null || order == null) return;
    
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
    
        int columnIndex = -1;
        switch (category) {
            case "First Name": columnIndex = 0; break;
            case "Last Name": columnIndex = 1; break;
            case "Gender": columnIndex = 2; break;
            case "ID Number": columnIndex = 3; break;
            case "Year Level": columnIndex = 4; break;
            case "College": columnIndex = 5; break;
            case "Program": columnIndex = 6; break;
        }
    
        if (columnIndex == -1) return; // Invalid category
    
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