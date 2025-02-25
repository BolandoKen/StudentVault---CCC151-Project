import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class StudentForm extends JPanel {
    private TablePanel tablePanel;
    private RoundedTextField firstnameField;
    private RoundedTextField lastnameField;
    private RoundedTextField idField;
    private RoundedComboBox genderComboBox;
    private RoundedComboBox yearLevelComboBox;
    private RoundedComboBox collegeComboBox;
    private RoundedComboBox programComboBox;
    private RoundedButton actionButton;
    private JLabel titleLabel;
    
    private boolean editMode = false;
    private String originalId = null; // To keep track of the ID before editing
    private Map<String, String[]> collegePrograms;
    
    public StudentForm(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
        if (tablePanel != null) {
            tablePanel.setStudentForm(this);
        }
        
        setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Row 0: Title
        gbc.gridy = 0;
        JPanel additionalInfoPanel = new JPanel(new GridBagLayout());
        additionalInfoPanel.setPreferredSize(new Dimension(450, 54));
        additionalInfoPanel.setBackground(Color.white);

        titleLabel = new JLabel("Add New Student");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        additionalInfoPanel.add(titleLabel);
        add(additionalInfoPanel, gbc);

        // Row 1: Firstname and Lastname
        gbc.gridy = 1;
        JPanel row1 = new JPanel(new GridBagLayout());
        row1.setPreferredSize(new Dimension(450, 54));
        row1.setBackground(Color.white);

        GridBagConstraints nameGbc = new GridBagConstraints();
        nameGbc.gridy = 0;
        nameGbc.fill = GridBagConstraints.BOTH;
        nameGbc.weightx = 1;
        nameGbc.insets = new Insets(5, 10, 5, 10);

        nameGbc.gridx = 0;
        firstnameField = new RoundedTextField(30, 10, "Firstname", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        row1.add(firstnameField, nameGbc);

        nameGbc.gridx = 1;
        lastnameField = new RoundedTextField(30, 10, "Lastname", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        row1.add(lastnameField, nameGbc);
        add(row1, gbc);

        // Row 2: Gender, ID Number, Year Level
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        JPanel row2 = new JPanel(new GridBagLayout());
        row2.setBackground(Color.white);
        GridBagConstraints row2Gbc = new GridBagConstraints();
        row2Gbc.gridy = 0;
        row2Gbc.fill = GridBagConstraints.BOTH;
        row2Gbc.weightx = 1;
        row2Gbc.insets = new Insets(5, 10, 5, 10);

        // Gender Dropdown
        row2Gbc.gridx = 0;
        genderComboBox = new RoundedComboBox(
            new String[]{"Gender", "Male", "Female", "Other"},
            Color.decode("#E7E7E7"),
            Color.gray,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(150, 40),
            "Gender",
            Color.decode("#6DBECA")
        );
        row2.add(genderComboBox, row2Gbc);

        // ID Number Field
        row2Gbc.gridx = 1;
        JPanel idPanel = new JPanel(new BorderLayout());
        idPanel.setBackground(Color.white);
        idPanel.setPreferredSize(new Dimension(150, 40));
        idField = new RoundedTextField(30, 10, "ID Number", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        idPanel.add(idField, BorderLayout.CENTER);
        row2.add(idPanel, row2Gbc);

        // Year Level Dropdown
        row2Gbc.gridx = 2;
        yearLevelComboBox = new RoundedComboBox(
            new String[]{"Year Level", "1st Year", "2nd Year", "3rd Year", "4th Year"},
            Color.decode("#E7E7E7"),
            Color.GRAY,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(150, 40),
            "Year Level",
            Color.decode("#6DBECA")
        );
        row2.add(yearLevelComboBox, row2Gbc);
        add(row2, gbc);

        // Row 3: College Panel
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        JPanel row3 = new JPanel(new GridBagLayout());
        row3.setPreferredSize(new Dimension(450, 54));
        row3.setBackground(Color.WHITE);

        GridBagConstraints row3Gbc = new GridBagConstraints();
        row3Gbc.gridy = 0;
        row3Gbc.fill = GridBagConstraints.BOTH;
        row3Gbc.weightx = 1; 
        row3Gbc.insets = new Insets(5, 10, 5, 10);

        JPanel collegePanel = new JPanel(new BorderLayout());
        collegePanel.setBackground(Color.white);

        collegePrograms = new HashMap<>();
        collegePrograms.put("College", new String[]{"Program"});
        collegePrograms.put("College of Computer Studies", new String[]{"BS Computer Science", "BS Information Technology"});
        collegePrograms.put("College of Engineering", new String[]{"BS Civil Engineering", "BS Electrical Engineering"});
        collegePrograms.put("College of Science and Mathematics", new String[]{"BS Mathematics", "BS Physics"});
        collegePrograms.put("College of Economics and Business Accountancy", new String[]{"BS Accountancy", "BS Economics"});
        collegePrograms.put("College of Arts and Social Sciences", new String[]{"BA Communication", "BA Political Science"});
        collegePrograms.put("College of Education", new String[]{"BS Secondary Education", "BS Elementary Education"});
        collegePrograms.put("College of Health Sciences", new String[]{"BS Nursing", "BS Pharmacy"});

        // Populate college dropdown
        String[] colleges = collegePrograms.keySet().toArray(new String[0]);
        collegeComboBox = new RoundedComboBox(
            colleges,
            Color.decode("#E7E7E7"),
            Color.GRAY,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(450, 40),
            "College",
            Color.decode("#6DBECA")
        );

        collegePanel.add(collegeComboBox, BorderLayout.CENTER);
        row3.add(collegePanel, row3Gbc);
        add(row3, gbc);

        // Row 4: Program Panel
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JPanel row4 = new JPanel(new GridBagLayout());
        row4.setPreferredSize(new Dimension(450, 54));
        row4.setBackground(Color.WHITE);

        GridBagConstraints row4Gbc = new GridBagConstraints();
        row4Gbc.gridy = 0;
        row4Gbc.fill = GridBagConstraints.BOTH;
        row4Gbc.weightx = 1; 
        row4Gbc.insets = new Insets(5, 10, 5, 10);

        JPanel programPanel = new JPanel(new BorderLayout());
        programPanel.setBackground(Color.white);
        programComboBox = new RoundedComboBox(
            new String[]{"Program"}, // Empty initially
            Color.decode("#E7E7E7"),
            Color.GRAY,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(450, 40),
            "Program",
            Color.decode("#6DBECA")
        );
        programPanel.add(programComboBox, BorderLayout.CENTER);
        row4.add(programPanel, row4Gbc);
        add(row4, gbc);

        collegeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCollege = (String) collegeComboBox.getSelectedItem();
                updateProgramComboBox(selectedCollege);
            }
        });

        // Row 5: Action Button (Add/Update)
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JPanel row5 = new JPanel(new GridBagLayout());
        row5.setPreferredSize(new Dimension(450, 54));
        row5.setBackground(Color.WHITE);

        GridBagConstraints row5Gbc = new GridBagConstraints();
        row5Gbc.gridy = 0;
        row5Gbc.fill = GridBagConstraints.BOTH;
        row5Gbc.weightx = 1; 
        row5Gbc.insets = new Insets(5, 10, 5, 10);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.white);
        actionButton = new RoundedButton(
            "Add Student",
            Color.decode("#6DBECA"),  // Background color (blue)
            Color.WHITE,              // Text color
            new Font("Helvetica", Font.PLAIN, 18), // Custom font
            10                        // Corner radius
        );
        actionButton.setPreferredSize(new Dimension(150, 40));
        actionPanel.add(actionButton, BorderLayout.CENTER);
        row5.add(actionPanel, row5Gbc);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    if (editMode) {
                        updateStudent();
                    } else {
                        addStudent();
                    }
                }
            }
        });
        add(row5, gbc);
        
        // Add a cancel button for edit mode
        gbc.gridy = 6;
        JPanel row6 = new JPanel(new GridBagLayout());
        row6.setPreferredSize(new Dimension(450, 54));
        row6.setBackground(Color.WHITE);

        GridBagConstraints row6Gbc = new GridBagConstraints();
        row6Gbc.gridy = 0;
        row6Gbc.fill = GridBagConstraints.BOTH;
        row6Gbc.weightx = 1; 
        row6Gbc.insets = new Insets(5, 10, 5, 10);

        JPanel cancelPanel = new JPanel(new BorderLayout());
        cancelPanel.setBackground(Color.white);
        RoundedButton cancelButton = new RoundedButton(
            "Cancel",
            Color.decode("#E7E7E7"),  // Background color (gray)
            Color.BLACK,              // Text color
            new Font("Helvetica", Font.PLAIN, 18), // Custom font
            10                        // Corner radius
        );
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelPanel.add(cancelButton, BorderLayout.CENTER);
        row6.add(cancelPanel, row6Gbc);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
                
                // Switch back to table view
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(StudentForm.this);
                if (frame instanceof GUI) {
                    ((GUI) frame).switchPanel("TABLE");
                }
            }
        });
        add(row6, gbc);
    }
    
    private void updateProgramComboBox(String selectedCollege) {
        programComboBox.removeAllItems();
        if (selectedCollege != null && collegePrograms.containsKey(selectedCollege)) {
            for (String program : collegePrograms.get(selectedCollege)) {
                programComboBox.addItem(program);
            }
        }
    }
    
    private boolean validateForm() {
        // Get input values and trim whitespace
        String firstName = firstnameField.getText().trim();
        String lastName = lastnameField.getText().trim();
        String gender = genderComboBox.getSelectedItem().toString();
        String idNumber = idField.getText().trim();
        String yearLevel = yearLevelComboBox.getSelectedItem().toString();
        String college = collegeComboBox.getSelectedItem().toString();
        String program = programComboBox.getSelectedItem().toString();

        // Validate all required fields
        StringBuilder errorMessage = new StringBuilder("Please fill in the following fields:\n");
        boolean hasError = false;

        if(firstName.isEmpty() || firstName.equals("Firstname")) {
            errorMessage.append("- First Name\n");
            hasError = true;
        }
        if(lastName.isEmpty() || lastName.equals("Lastname")) {
            errorMessage.append("- Last Name\n");
            hasError = true;
        }
        if(idNumber.isEmpty() || idNumber.equals("ID Number")) {
            errorMessage.append("- ID Number\n");
            hasError = true;
        }
        if(gender.equals("Gender")) {
            errorMessage.append("- Gender\n");
            hasError = true;
        }
        if(yearLevel.equals("Year Level")) {
            errorMessage.append("- Year Level\n");
            hasError = true;
        }
        if(college.equals("College")) {
            errorMessage.append("- College\n");
            hasError = true;
        }
        if(program.equals("Program")) {
            errorMessage.append("- Program\n");
            hasError = true;
        }

        if(hasError) {
            JOptionPane.showMessageDialog(
                StudentForm.this,
                errorMessage.toString(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        
        // Check if ID exists (only in add mode, or if ID has changed in edit mode)
        if (!editMode || !idNumber.equals(originalId)) {
            if (StudentManager.isIdExists(idNumber)) {
                JOptionPane.showMessageDialog(
                    StudentForm.this,
                    "A student with this ID already exists.",
                    "Duplicate ID",
                    JOptionPane.ERROR_MESSAGE
                );
                return false;
            }
        }
        
        return true;
    }
    
    private void addStudent() {
        try {
            String firstName = firstnameField.getText().trim();
            String lastName = lastnameField.getText().trim();
            String gender = genderComboBox.getSelectedItem().toString();
            String idNumber = idField.getText().trim();
            String yearLevel = yearLevelComboBox.getSelectedItem().toString();
            String college = collegeComboBox.getSelectedItem().toString();
            String program = programComboBox.getSelectedItem().toString();
            
            Student student = new Student(firstName, lastName, gender, idNumber, 
                                       yearLevel, college, program);
            
            // Save to CSV using StudentManager
            StudentManager.saveStudent(student);
            
            JOptionPane.showMessageDialog(
                StudentForm.this,
                "Student added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Refresh table
            if (tablePanel != null) {
                tablePanel.refreshTable();
            }
            
            // Clear form fields
            resetForm();
            
            // Switch back to table view
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof GUI) {
                ((GUI) frame).switchPanel("TABLE");
            }
            
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(
                StudentForm.this,
                "Error creating student: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void updateStudent() {
        try {
            String firstName = firstnameField.getText().trim();
            String lastName = lastnameField.getText().trim();
            String gender = genderComboBox.getSelectedItem().toString();
            String idNumber = idField.getText().trim();
            String yearLevel = yearLevelComboBox.getSelectedItem().toString();
            String college = collegeComboBox.getSelectedItem().toString();
            String program = programComboBox.getSelectedItem().toString();
            
            Student updatedStudent = new Student(firstName, lastName, gender, idNumber, 
                                       yearLevel, college, program);
            
            // Update student in CSV
            if (StudentManager.updateStudent(originalId, updatedStudent)) {
                JOptionPane.showMessageDialog(
                    StudentForm.this,
                    "Student updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                // Refresh table
                if (tablePanel != null) {
                    tablePanel.refreshTable();
                }
                
                // Reset form and switch back to add mode
                resetForm();
                
                // Switch back to table view
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame instanceof GUI) {
                    ((GUI) frame).switchPanel("TABLE");
                }
            } else {
                JOptionPane.showMessageDialog(
                    StudentForm.this,
                    "Failed to update student. The student may have been deleted.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
            
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(
                StudentForm.this,
                "Error updating student: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    // Set the form to edit mode with student data
    public void setEditMode(Student student) {
        editMode = true;
        originalId = student.getIdNumber();
        
        // Update UI to reflect edit mode
        titleLabel.setText("Edit Student");
        actionButton.setText("Update Student");
        
        // Populate form fields with student data
        firstnameField.setText(student.getFirstName());
        lastnameField.setText(student.getLastName());
        idField.setText(student.getIdNumber());
        
        // Set combo box selections
        for (int i = 0; i < genderComboBox.getItemCount(); i++) {
            if (genderComboBox.getItemAt(i).toString().equals(student.getGender())) {
                genderComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        for (int i = 0; i < yearLevelComboBox.getItemCount(); i++) {
            if (yearLevelComboBox.getItemAt(i).toString().equals(student.getYearLevel())) {
                yearLevelComboBox.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < collegeComboBox.getItemCount(); i++) {
            if (collegeComboBox.getItemAt(i).toString().equals(student.getCollege())) {
                collegeComboBox.setSelectedIndex(i);
                updateProgramComboBox(student.getCollege());
                break;
            }
        }

        for (int i = 0; i < programComboBox.getItemCount(); i++) {
            if (programComboBox.getItemAt(i).toString().equals(student.getProgram())) {
                programComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void resetForm() {
        editMode = false;
        originalId = null;

        titleLabel.setText("Add New Student");
        actionButton.setText("Add Student");

        firstnameField.setText("Firstname");
        lastnameField.setText("Lastname");
        idField.setText("ID Number");
        genderComboBox.setSelectedIndex(0);
        yearLevelComboBox.setSelectedIndex(0);
        collegeComboBox.setSelectedIndex(0);
        updateProgramComboBox("College");
    }
}
