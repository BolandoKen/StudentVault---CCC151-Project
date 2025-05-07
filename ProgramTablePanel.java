import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public final class ProgramTablePanel extends JPanel {
    private JTable table;
    private final DefaultTableModel model;
    private StudentForm studentForm;
    private RoundedComboBox sortOrderBox;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private int currentPage = 0;
    private int pageSize = 20;
    private List<Student> allStudents = new ArrayList<>();
    private JButton previousButton;
    private JButton nextButton;
    private JLabel pageInfoLabel;
    private List<Student> filteredStudents = new ArrayList<>();
    private boolean isFiltered = false;


    public ProgramTablePanel() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

        SearchPanel searchPanel = new SearchPanel();
        searchPanel.setProgramTablePanel(this);

        searchPanelContainer.add(searchPanel, BorderLayout.CENTER);
        this.add(searchPanelContainer, gbc);

        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(1, 100));
        gbc.gridx = 0;
        gbc.gridy = 1;
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

        JLabel studentVaultText = new JLabel("Programs");
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
        gbc.gridy = 2;
        gbc.weighty = 0.8;
        this.add(bottomRow, gbc);

        RoundedPanel tablePanel = new RoundedPanel(10);
        tablePanel.setBackground(new Color(0xffffff));
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setMinimumSize(new Dimension(800, 400));

        String[] columnNames = {"", "Firstname", "Lastname", "Gender", "Id Number", "Year Level", "College", "Program"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 && selectionMode; 
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
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

        TableColumn checkboxColumn = table.getColumnModel().getColumn(0);
        checkboxColumn.setMaxWidth(30);
        checkboxColumn.setMinWidth(30);
        checkboxColumn.setPreferredWidth(30);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int[] columnWidthPercentages = {3, 14, 14, 10, 14, 10, 17, 18}; 
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

        HoverTooltipTableRenderer hoverTooltipRenderer = new HoverTooltipTableRenderer();
        table.setDefaultRenderer(Object.class, hoverTooltipRenderer);


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
JPanel paginationPanel = createPaginationPanel();
bottomRow.add(paginationPanel, BorderLayout.SOUTH);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                hoverTooltipRenderer.setHoveredRow(row);
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
    
        deleteButton.setVisible(!selectionMode);
        cancelButton.setVisible(selectionMode);
        confirmDeleteButton.setVisible(selectionMode);
        

        if (!selectionMode) {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(false, i, 0);
            }
        }
        
        table.getColumnModel().getColumn(0).setMinWidth(selectionMode ? 30 : 0);
        table.getColumnModel().getColumn(0).setMaxWidth(selectionMode ? 30 : 0);
        table.getColumnModel().getColumn(0).setPreferredWidth(selectionMode ? 30 : 0);
        
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
        table.revalidate();
        table.repaint();
    }
    
    private void deleteSelectedStudents() {
        List<String> selectedIds = new ArrayList<>();
        
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isSelected = (Boolean) model.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                String idNumber = model.getValueAt(i, 4).toString();
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
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete " + selectedIds.size() + " selected student(s)?",
            "Confirm Multiple Deletion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            int successCount = 0;
            
            for (String id : selectedIds) {
                if (StudentManager.deleteStudent(id)) {
                    successCount++;
                }
            }
            
            if (successCount > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    successCount + " student(s) deleted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
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
    private JPanel createPaginationPanel() {
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setOpaque(false);
        
        previousButton = new JButton("← Previous");
        previousButton.setEnabled(false);
        previousButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateTableForPage(currentPage);
            }
        });
        
        pageInfoLabel = new JLabel("Page 1");
        pageInfoLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        pageInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        
        nextButton = new JButton("Next →");
        nextButton.setEnabled(false);
        nextButton.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double) allStudents.size() / pageSize) - 1;
            if (currentPage < maxPage) {
                currentPage++;
                updateTableForPage(currentPage);
            }
        });
        
        paginationPanel.add(previousButton);
        paginationPanel.add(pageInfoLabel);
        paginationPanel.add(nextButton);
        
        return paginationPanel;
    }
    
    private void editSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            String firstName = model.getValueAt(modelRow, 1).toString();
            String lastName = model.getValueAt(modelRow, 2).toString();
            String gender = model.getValueAt(modelRow, 3).toString();
            String idNumber = model.getValueAt(modelRow, 4).toString();
            String yearLevel = model.getValueAt(modelRow, 5).toString();
            String collegeAbbr = model.getValueAt(modelRow, 6).toString();
            String programAbbr = model.getValueAt(modelRow, 7).toString();
            
            // Get full names from abbreviations
            String college = CollegeDataManager.getCollegeName(collegeAbbr);
            String program = CollegeDataManager.getProgramName(programAbbr);
    
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
                            findAndSetStudentForm((Container) comp, student);
                        }
                    }
                }
            }
        }
    }
    private void deleteSelectedStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
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
        CollegeDataManager.loadFromCSV(); // Ensure data is up-to-date
        allStudents = StudentManager.loadStudents();
        
        // Reset to first page when refreshing
        currentPage = 0;
        
        updateTableForPage(currentPage);
    }
    
    private void updateTableForPage(int page) {
        model.setRowCount(0);
    
        // Use filteredStudents if filtering is active, otherwise use allStudents
        List<Student> studentsToDisplay = isFiltered ? filteredStudents : allStudents;
    
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, studentsToDisplay.size());
        List<String> validColleges = CollegeDataManager.getAllColleges();
    
        for (int i = startIndex; i < endIndex; i++) {
            Student student = studentsToDisplay.get(i);
    
            String college = student.getCollege();
            String program = student.getProgram();
    
            String collegeDisplay = "N/A";
            String programDisplay = "N/A";
    
            // Check if college exists
            if (college != null && validColleges.contains(college)) {
                collegeDisplay = CollegeDataManager.getCollegeAbbr(college);
    
                // Check if program exists in the college
                List<String> collegePrograms = CollegeDataManager.getProgramsForCollege(college);
                if (program != null && collegePrograms.contains(program)) {
                    programDisplay = CollegeDataManager.getProgramAbbr(program);
                }
            }
    
            model.addRow(new Object[]{
                false,
                student.getFirstName(),
                student.getLastName(),
                student.getGender(),
                student.getIdNumber(),
                student.getYearLevel(),
                collegeDisplay,
                programDisplay
            });
        }
    
        model.fireTableDataChanged();
    
        // Update pagination controls
        int totalPages = (int) Math.ceil((double) studentsToDisplay.size() / pageSize);
        pageInfoLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
        previousButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < totalPages - 1);
    }
    public JTable getTable() {
        return table;
    }
    
    public void setStudentForm(StudentForm form) {
        this.studentForm = form;
    }
    
    private void sortTable(String category, String order) {
        if (category == null || order == null) return;
    
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
    
        // Sort the full list of students
        final int finalColumnIndex = columnIndex;
        allStudents.sort((s1, s2) -> {
            String val1 = getStudentValueForColumn(s1, finalColumnIndex);
            String val2 = getStudentValueForColumn(s2, finalColumnIndex);
            
            int result;
            if (finalColumnIndex == 4 || finalColumnIndex == 5) { // ID Number or Year Level
                try {
                    result = Integer.compare(Integer.parseInt(val1), Integer.parseInt(val2));
                } catch (NumberFormatException e) {
                    result = val1.compareTo(val2);
                }
            } else {
                result = val1.compareTo(val2);
            }
            
            return order.equals("A-Z") || order.equals("Male → Female") || order.equals("Ascending") ? 
                   result : -result;
        });
        
        // Reset to the first page after sorting
        currentPage = 0;
        updateTableForPage(currentPage);
    }
    
    private String getStudentValueForColumn(Student student, int columnIndex) {
        switch (columnIndex) {
            case 1: return student.getFirstName();
            case 2: return student.getLastName();
            case 3: return student.getGender();
            case 4: return student.getIdNumber();
            case 5: return student.getYearLevel();
            case 6: return CollegeDataManager.getCollegeAbbr(student.getCollege());
            case 7: return CollegeDataManager.getProgramAbbr(student.getProgram());
            default: return "";
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
    public void resetPage() {
        currentPage = 0;
        updateTableForPage(currentPage);
    }
    public void applyFilter(RowFilter<DefaultTableModel, Integer> filter) {
        isFiltered = (filter != null);
    
        if (filter == null) {
            // No filter, use all students
            filteredStudents = new ArrayList<>(allStudents);
        } else {
            // Apply filter to all students
            filteredStudents = new ArrayList<>();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
    
            for (Student student : allStudents) {
                // Convert student to row data
                Object[] rowData = {
                    false,
                    student.getFirstName(),
                    student.getLastName(),
                    student.getGender(),
                    student.getIdNumber(),
                    student.getYearLevel(),
                    CollegeDataManager.getCollegeAbbr(student.getCollege()),
                    CollegeDataManager.getProgramAbbr(student.getProgram())
                };
    
                // Test if this student matches the filter
                if (filter.include(new RowFilter.Entry<DefaultTableModel, Integer>() {
                    @Override
                    public DefaultTableModel getModel() {
                        return model;
                    }
    
                    @Override
                    public int getValueCount() {
                        return rowData.length;
                    }
    
                    @Override
                    public Object getValue(int index) {
                        return rowData[index];
                    }
    
                    @Override
                    public Integer getIdentifier() {
                        return 0; // Not used in this context
                    }
                })) {
                    filteredStudents.add(student);
                }
            }
        }
    
        // Reset to first page and update the table
        currentPage = 0;
        updateTableForPage(currentPage);
    }
}