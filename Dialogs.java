import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class Dialogs {

    public static void addCollegeDialog(CCollegeTable collegeTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New College");
        dialog.setLayout(new GridLayout(3, 2));
        
        JTextField collegeNameField = new JTextField(20);
        JTextField collegeCodeField = new JTextField(20);
    
        dialog.add(new JLabel("College Name:"));
        dialog.add(collegeNameField);
        dialog.add(new JLabel("College Code:"));
        dialog.add(collegeCodeField);
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String newCollegeName = collegeNameField.getText().trim();
            String newCollegeCode = collegeCodeField.getText().trim();
            
            // Validate inputs
            if (newCollegeName.isEmpty() || newCollegeCode.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "College name and code cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for existing college code
            if (CollegeDataManager.collegeCodeExists(newCollegeCode)) {
                JOptionPane.showMessageDialog(dialog, 
                    "A college with this code already exists", 
                    "Duplicate College Code", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for existing college name
            if (CollegeDataManager.collegeNameExists(newCollegeName)) {
                JOptionPane.showMessageDialog(dialog, 
                    "A college with this name already exists", 
                    "Duplicate College Name", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Set the college details
            CollegeDataManager.setCollegeName(newCollegeName);
            CollegeDataManager.setCollegeCode(newCollegeCode);
            
            // Attempt to save the college
            if (CollegeDataManager.saveCollege()) {
                // Refresh the table to show the new data
                collegeTable.refreshTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to add college due to a database error", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
    
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
        
    public static void editCollegeDialog(String collegeCode, CCollegeTable collegeTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit College");
        dialog.setLayout(new GridLayout(3, 2));
        
        JTextField collegeNameField = new JTextField(20);
        collegeNameField.setText(CollegeDataManager.getCollegeName(collegeCode));
    
        JTextField collegeCodeField = new JTextField(20);
        collegeCodeField.setText(collegeCode);
    
        dialog.add(new JLabel("College Name:"));
        dialog.add(collegeNameField);
        dialog.add(new JLabel("College Code:"));
        dialog.add(collegeCodeField);
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String newCollegeName = collegeNameField.getText().trim();
            String newCollegeCode = collegeCodeField.getText().trim();
            
            // Validate inputs
            if (newCollegeName.isEmpty() || newCollegeCode.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "College name and code cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm code change if different from original
            if (!newCollegeCode.equals(collegeCode)) {
                int confirm = JOptionPane.showConfirmDialog(
                    dialog, 
                    "Changing the college code will update all related programs. Continue?", 
                    "Confirm Update", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Perform the update
            if (CollegeDataManager.updateCollege(collegeCode, newCollegeName, newCollegeCode)) {
                // Refresh the table to show updated data
                collegeTable.refreshTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update college", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
    
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
            
    public static void deleteCollegeDialog(String collegeCode, CCollegeTable collegeTable) {

        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "Are you sure you want to delete this college? This action cannot be undone.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (CollegeDataManager.deleteCollege(collegeCode)) {
                // Refresh the table to reflect the deletion
                collegeTable.refreshTable();
                JOptionPane.showMessageDialog(
                    null, 
                    "College deleted successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "Failed to delete college. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void addProgramDialog(CProgramTable programTable) {
    JDialog dialog = new JDialog();
    dialog.setTitle("Add New Program");
    dialog.setLayout(new GridLayout(4, 2));
    
    JTextField programNameField = new JTextField(20);
    JTextField programCodeField = new JTextField(20);
    
    // Create a college dropdown
    JComboBox<String> collegeDropdown = new JComboBox<>();
    Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
    // Add a prompt as first item
    collegeDropdown.addItem("-- Select College --");
    // Add all colleges to dropdown
    for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
        collegeDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
    }
    
    dialog.add(new JLabel("Program Name:"));
    dialog.add(programNameField);
    dialog.add(new JLabel("Program Code:"));
    dialog.add(programCodeField);
    dialog.add(new JLabel("College:"));
    dialog.add(collegeDropdown);
    
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    
    saveButton.addActionListener(e -> {
        String newProgramName = programNameField.getText().trim();
        String newProgramCode = programCodeField.getText().trim();
        
        // Get selected college
        int selectedIndex = collegeDropdown.getSelectedIndex();
        
        // Validate inputs
        if (newProgramName.isEmpty() || newProgramCode.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Program name and code cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate college selection
        if (selectedIndex <= 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a college for this program", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for existing program code
        if (ProgramDataManager.programCodeExists(newProgramCode)) {
            JOptionPane.showMessageDialog(dialog, 
                "A program with this code already exists", 
                "Duplicate Program Code", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for existing program name
        if (ProgramDataManager.programNameExists(newProgramName)) {
            JOptionPane.showMessageDialog(dialog, 
                "A program with this name already exists", 
                "Duplicate Program Name", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract college code from selected item
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        String collegeCode = selectedCollege.substring(selectedCollege.lastIndexOf("(") + 1, selectedCollege.lastIndexOf(")"));
        
        // Attempt to add the program directly
        if (ProgramDataManager.addProgram(newProgramName, newProgramCode, collegeCode)) {
            // Refresh the table to show the new data
            programTable.refreshData();
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to add program due to a database error", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
    
public static void editProgramDialog(String programCode, CProgramTable programTable) {
    JDialog dialog = new JDialog();
    dialog.setTitle("Edit Program");
    dialog.setLayout(new GridLayout(4, 2));
    
    // Get current program details
    ProgramDataManager.getProgramByCode(programCode);
    String currentCollegeCode = ProgramDataManager.getCollegeCode();
    
    JTextField programNameField = new JTextField(20);
    programNameField.setText(ProgramDataManager.getProgramName(programCode));

    JTextField programCodeField = new JTextField(20);
    programCodeField.setText(programCode);
    
    // Create college dropdown
    JComboBox<String> collegeDropdown = new JComboBox<>();
    Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
    
    // Add all colleges to dropdown
    for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
        String collegeOption = entry.getValue() + " (" + entry.getKey() + ")";
        collegeDropdown.addItem(collegeOption);
        
        // Select the current college
        if (entry.getKey().equals(currentCollegeCode)) {
            collegeDropdown.setSelectedItem(collegeOption);
        }
    }

    dialog.add(new JLabel("Program Name:"));
    dialog.add(programNameField);
    dialog.add(new JLabel("Program Code:"));
    dialog.add(programCodeField);
    dialog.add(new JLabel("College:"));
    dialog.add(collegeDropdown);

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    saveButton.addActionListener(e -> {
        String newProgramName = programNameField.getText().trim();
        String newProgramCode = programCodeField.getText().trim();
        
        // Get selected college
        int selectedIndex = collegeDropdown.getSelectedIndex();
        
        // Validate inputs
        if (newProgramName.isEmpty() || newProgramCode.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Program name and code cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate college selection
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a college for this program", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for existing program code (only if changed)
        if (!newProgramCode.equals(programCode) && ProgramDataManager.programCodeExists(newProgramCode)) {
            JOptionPane.showMessageDialog(dialog, 
                "A program with this code already exists", 
                "Duplicate Program Code", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract college code from selected item
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        String newCollegeCode = selectedCollege.substring(selectedCollege.lastIndexOf("(") + 1, selectedCollege.lastIndexOf(")"));
        
        // Check if college was changed
        boolean collegeChanged = !newCollegeCode.equals(currentCollegeCode);
        if (collegeChanged) {
            int confirm = JOptionPane.showConfirmDialog(
                dialog, 
                "You are changing the college for this program. Continue?", 
                "Confirm Change", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // Update the program with new details including college
        if (updateProgramWithCollege(programCode, newProgramName, newProgramCode, newCollegeCode)) {
            // If program code changed, update all students with the old program code
            if (!newProgramCode.equals(programCode)) {
                updateStudentsProgramCode(programCode, newProgramCode);
            }
            
            // Refresh the table to show updated data
            programTable.refreshData();
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to update program", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}

/**
 * Updates all students with the old program code to use the new program code
 * @param oldProgramCode The original program code to find students
 * @param newProgramCode The new program code to update to
 */
private static void updateStudentsProgramCode(String oldProgramCode, String newProgramCode) {
    // Get all students with the old program code
    List<Student> students = StudentDataManager.getStudentsByProgram(oldProgramCode);
    
    // Update each student's program code
    for (Student student : students) {
        StudentDataManager.updateStudent(
            student.getIdNumber(), 
            student.getIdNumber(), // same ID
            student.getFirstName(), 
            student.getLastName(), 
            student.getGender(), 
            student.getYearLevel(), 
            newProgramCode // updated program code
        );
    }
}

// Helper method to update program with college change
    private static boolean updateProgramWithCollege(String oldProgramCode, String newProgramName, String newProgramCode, String newCollegeCode) {
    List<String[]> programs = ProgramDataManager.readCSV();
    boolean updated = false;
    
    // Check if new code exists and is different from old code
    if (!oldProgramCode.equals(newProgramCode)) {
        for (String[] program : programs) {
            if (program[0].equals(newProgramCode)) {
                return false; // New code already exists
            }
        }
    }
    
    // Update program information including college code
    for (int i = 0; i < programs.size(); i++) {
        String[] program = programs.get(i);
        if (program[0].equals(oldProgramCode)) {
            programs.set(i, new String[] {newProgramCode, newProgramName, newCollegeCode});
            updated = true;
            break;
        }
    }
    
    // Write back to CSV if updated
    if (updated) {
        return ProgramDataManager.writeCSV(programs);
    }
    
    return false;
}

public static void deleteProgramDialog(String programCode, CProgramTable programTable) {
    // First check if there are any students in this program
    List<Student> studentsInProgram = StudentDataManager.getStudentsByProgram(programCode);
    boolean hasStudents = !studentsInProgram.isEmpty();
    
    String message;
    if (hasStudents) {
        message = "This program has " + studentsInProgram.size() + " student(s) enrolled.\n" +
                 "Deleting will set their program to 'N/A'.\n" +
                 "Are you sure you want to delete this program?";
    } else {
        message = "Are you sure you want to delete this program? This action cannot be undone.";
    }
    
    int confirm = JOptionPane.showConfirmDialog(
        null, 
        message, 
        "Confirm Delete", 
        JOptionPane.YES_NO_OPTION,
        hasStudents ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {
        // First update all students in this program to "N/A"
        if (hasStudents) {
            updateStudentsProgramToNA(programCode);
        }
        
        // Then delete the program
        if (ProgramDataManager.deleteProgram(programCode)) {
            // Refresh the table to reflect the deletion
            programTable.refreshData();
            JOptionPane.showMessageDialog(
                null, 
                "Program deleted successfully." + 
                (hasStudents ? " " + studentsInProgram.size() + " student(s) were set to 'N/A'." : ""), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to delete program. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

/**
 * Updates all students in a program to have "N/A" as their program code
 * @param programCode The program code to find students for
 */
private static void updateStudentsProgramToNA(String programCode) {
    // Get all students with the program code
    List<Student> students = StudentDataManager.getStudentsByProgram(programCode);
    
    // Update each student's program code to "N/A"
    for (Student student : students) {
        StudentDataManager.updateStudent(
            student.getIdNumber(), 
            student.getIdNumber(), // same ID
            student.getFirstName(), 
            student.getLastName(), 
            student.getGender(), 
            student.getYearLevel(), 
            "N/A" // set program to N/A
        );
    }
}

    public static void addStudentDialog(CStudentTable studentTable) {
    JDialog dialog = new JDialog();
    dialog.setTitle("Add New Student");
    dialog.setLayout(new GridLayout(10, 2));
    
    JTextField idNumberField = new JTextField(20);
    JTextField firstNameField = new JTextField(20);
    JTextField lastNameField = new JTextField(20);
    
    // Gender dropdown
    JComboBox<String> genderDropdown = new JComboBox<>();
    genderDropdown.addItem("-- Select Gender --");
    genderDropdown.addItem("Male");
    genderDropdown.addItem("Female");
    genderDropdown.addItem("Other");
    
    // Year level dropdown
    JComboBox<String> yearLevelDropdown = new JComboBox<>();
    yearLevelDropdown.addItem("-- Select Year Level --");
    yearLevelDropdown.addItem("1");
    yearLevelDropdown.addItem("2");
    yearLevelDropdown.addItem("3");
    yearLevelDropdown.addItem("4");
    yearLevelDropdown.addItem("5");
    
    // College dropdown - added as first step
    JComboBox<String> collegeDropdown = new JComboBox<>();
    collegeDropdown.addItem("-- Select College --");
    Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
    for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
        collegeDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
    }
    
    // Program dropdown - empty initially
    JComboBox<String> programDropdown = new JComboBox<>();
    programDropdown.addItem("-- Select Program --");
    programDropdown.setEnabled(false); // Disabled until college is selected
    
    // Labels and fields
    dialog.add(new JLabel("Student ID:"));
    dialog.add(idNumberField);
    dialog.add(new JLabel("First Name:"));
    dialog.add(firstNameField);
    dialog.add(new JLabel("Last Name:"));
    dialog.add(lastNameField);
    dialog.add(new JLabel("Gender:"));
    dialog.add(genderDropdown);
    dialog.add(new JLabel("Year Level:"));
    dialog.add(yearLevelDropdown);
    dialog.add(new JLabel("College:"));
    dialog.add(collegeDropdown);
    dialog.add(new JLabel("Program:"));
    dialog.add(programDropdown);
    
    // Add listener to college dropdown to update program dropdown
    collegeDropdown.addActionListener(e -> {
        int selectedIndex = collegeDropdown.getSelectedIndex();
        // Clear existing programs
        programDropdown.removeAllItems();
        programDropdown.addItem("-- Select Program --");
        
        if (selectedIndex > 0) {
            // Get selected college code
            String selectedCollege = (String) collegeDropdown.getSelectedItem();
            String collegeCode = selectedCollege.substring(selectedCollege.lastIndexOf("(") + 1, selectedCollege.lastIndexOf(")"));
            
            // Get programs for selected college
            Map<String, String> filteredPrograms = ProgramDataManager.loadProgramsByCollege(collegeCode);
            
            // Add filtered programs to dropdown
            for (Map.Entry<String, String> entry : filteredPrograms.entrySet()) {
                programDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
            }
            
            programDropdown.setEnabled(true);
        } else {
            programDropdown.setEnabled(false);
        }
    });
    
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    
    saveButton.addActionListener(e -> {
        String idNumber = idNumberField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        
        // Get selected values
        String gender = genderDropdown.getSelectedIndex() > 0 ? 
                        (String) genderDropdown.getSelectedItem() : "";
        
        String yearLevel = yearLevelDropdown.getSelectedIndex() > 0 ? 
                          (String) yearLevelDropdown.getSelectedItem() : "";
        
        // Validate required fields
        if (idNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Student ID, First Name, and Last Name are required fields", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (StudentDataManager.studentIdExists(idNumber)) {
            JOptionPane.showMessageDialog(dialog, 
                "A student with this ID already exists", 
                "Duplicate Student ID", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (collegeDropdown.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a college for the student", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (programDropdown.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a program for the student", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract college information
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        String collegeCode = selectedCollege.substring(selectedCollege.lastIndexOf("(") + 1, selectedCollege.lastIndexOf(")"));
        String collegeName = selectedCollege.substring(0, selectedCollege.lastIndexOf("(")).trim();
        
        // Extract program code and name
        String selectedProgram = (String) programDropdown.getSelectedItem();
        String programCode = selectedProgram.substring(selectedProgram.lastIndexOf("(") + 1, selectedProgram.lastIndexOf(")"));
        String programName = selectedProgram.substring(0, selectedProgram.lastIndexOf("(")).trim();
        
        // Add student using the data manager
        if (StudentDataManager.addStudent(idNumber, firstName, lastName, gender, yearLevel,programCode)) {
            studentTable.refreshTable();
            dialog.dispose();
            JOptionPane.showMessageDialog(null, 
                "Student added successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to add student. Please check all fields and try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}

  public static void editStudentDialog(String studentId, CStudentTable studentTable) {
    // First, load the student data
    if (!StudentDataManager.getStudentById(studentId)) {
        JOptionPane.showMessageDialog(null, 
            "Could not find student with ID: " + studentId, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    JDialog dialog = new JDialog();
    dialog.setTitle("Edit Student");
    dialog.setLayout(new GridLayout(8, 2)); // Adjusted rows
    
    // Create form fields with current values
    JTextField idNumberField = new JTextField(20);
    idNumberField.setText(StudentDataManager.getIdNumber());
    
    JTextField firstNameField = new JTextField(20);
    firstNameField.setText(StudentDataManager.getFirstName());
    
    JTextField lastNameField = new JTextField(20);
    lastNameField.setText(StudentDataManager.getLastName());
    
    // Gender dropdown
    JComboBox<String> genderDropdown = new JComboBox<>();
    genderDropdown.addItem("-- Select Gender --");
    genderDropdown.addItem("Male");
    genderDropdown.addItem("Female");
    genderDropdown.addItem("Other");
    
    // Set current gender if available
    String currentGender = StudentDataManager.getGender();
    if (currentGender != null && !currentGender.isEmpty()) {
        for (int i = 0; i < genderDropdown.getItemCount(); i++) {
            if (genderDropdown.getItemAt(i).equals(currentGender)) {
                genderDropdown.setSelectedIndex(i);
                break;
            }
        }
    }
    
    // Year level dropdown
    JComboBox<String> yearLevelDropdown = new JComboBox<>();
    yearLevelDropdown.addItem("-- Select Year Level --");
    yearLevelDropdown.addItem("1");
    yearLevelDropdown.addItem("2");
    yearLevelDropdown.addItem("3");
    yearLevelDropdown.addItem("4");
    yearLevelDropdown.addItem("5");
    
    // Set current year level if available
    String currentYearLevel = StudentDataManager.getYearLevel();
    if (currentYearLevel != null && !currentYearLevel.isEmpty()) {
        for (int i = 0; i < yearLevelDropdown.getItemCount(); i++) {
            if (yearLevelDropdown.getItemAt(i).equals(currentYearLevel)) {
                yearLevelDropdown.setSelectedIndex(i);
                break;
            }
        }
    }
    
    // College dropdown (for filtering programs)
    JComboBox<String> collegeDropdown = new JComboBox<>();
    collegeDropdown.addItem("-- All Colleges --");
    Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
    for (String collegeCode : collegeMap.keySet()) {
        collegeDropdown.addItem(collegeMap.get(collegeCode) + " (" + collegeCode + ")");
    }
    
    // Program dropdown (filtered by college selection)
    JComboBox<String> programDropdown = new JComboBox<>();
    programDropdown.addItem("-- Select Program --");
    
    // Load all programs initially
    Map<String, String> programMap = ProgramDataManager.loadProgramMap();
    Map<String, String> programToCollegeMap = new HashMap<>();
    
    // Populate program dropdown and create mapping
    String currentProgramCode = StudentDataManager.getProgramCode();
    int programIndex = 0;
    int counter = 1;
    
    for (Map.Entry<String, String> entry : programMap.entrySet()) {
        String programCode = entry.getKey();
        String programName = entry.getValue();
        
        // Get college for this program
        ProgramDataManager.getProgramByCode(programCode);
        String collegeCode = ProgramDataManager.getCollegeCode();
        String collegeName = CollegeDataManager.getCollegeName(collegeCode);
        
        programToCollegeMap.put(programCode, collegeCode);
        
        String displayText = programName + " (" + programCode + ") - " + collegeName;
        programDropdown.addItem(displayText);
        
        // Select current program
        if (programCode.equals(currentProgramCode)) {
            programIndex = counter;
        }
        counter++;
    }
    
    if (programIndex > 0) {
        programDropdown.setSelectedIndex(programIndex);
    }
    
    // College filter listener
    collegeDropdown.addActionListener(e -> {
        programDropdown.removeAllItems();
        programDropdown.addItem("-- Select Program --");
        
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        String selectedCollegeCode = selectedCollege.contains("(") ? 
            selectedCollege.substring(selectedCollege.lastIndexOf("(") + 1, selectedCollege.lastIndexOf(")")) : 
            "";
        
        for (Map.Entry<String, String> entry : programMap.entrySet()) {
            String programCode = entry.getKey();
            String programName = entry.getValue();
            String collegeCode = programToCollegeMap.get(programCode);
            
            if (selectedCollegeCode.isEmpty() || collegeCode.equals(selectedCollegeCode)) {
                String collegeName = CollegeDataManager.getCollegeName(collegeCode);
                String displayText = programName + " (" + programCode + ") - " + collegeName;
                programDropdown.addItem(displayText);
            }
        }
    });
    
    // Labels and fields
    dialog.add(new JLabel("Student ID:"));
    dialog.add(idNumberField);
    dialog.add(new JLabel("First Name:"));
    dialog.add(firstNameField);
    dialog.add(new JLabel("Last Name:"));
    dialog.add(lastNameField);
    dialog.add(new JLabel("Gender:"));
    dialog.add(genderDropdown);
    dialog.add(new JLabel("Year Level:"));
    dialog.add(yearLevelDropdown);
    dialog.add(new JLabel("Filter by College:"));
    dialog.add(collegeDropdown);
    dialog.add(new JLabel("Program:"));
    dialog.add(programDropdown);
    
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    
    saveButton.addActionListener(e -> {
        String oldId = studentId;
        String newId = idNumberField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        
        // Get selected values
        String gender = genderDropdown.getSelectedIndex() > 0 ? 
                       (String) genderDropdown.getSelectedItem() : "";
                       
        String yearLevel = yearLevelDropdown.getSelectedIndex() > 0 ? 
                          (String) yearLevelDropdown.getSelectedItem() : "";
        
        // Validate required fields
        if (newId.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Student ID, First Name, and Last Name are required fields", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for ID conflicts if ID was changed
        if (!oldId.equals(newId) && StudentDataManager.studentIdExists(newId)) {
            JOptionPane.showMessageDialog(dialog, 
                "A student with this new ID already exists", 
                "Duplicate Student ID", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (programDropdown.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a program for the student", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract program code from selection
        String selectedProgram = (String) programDropdown.getSelectedItem();
        String programCode = selectedProgram.substring(
            selectedProgram.indexOf("(") + 1, 
            selectedProgram.indexOf(")")
        );
        
        // Update the student (only saving the 6 fields to CSV)
        if (StudentDataManager.updateStudent(oldId, newId, firstName, lastName, 
                                           gender, yearLevel, programCode)) {
            studentTable.refreshTable();
            dialog.dispose();
            JOptionPane.showMessageDialog(null, 
                "Student updated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to update student. Please check all fields and try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
    public static void deleteStudentDialog(String studentId, CStudentTable studentTable) {
    int confirm = JOptionPane.showConfirmDialog(
        null, 
        "Are you sure you want to delete the student with ID: " + studentId + "?\nThis action cannot be undone.", 
        "Confirm Delete", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        if (StudentDataManager.deleteStudent(studentId)) {
            // Refresh the table to reflect the deletion
            studentTable.refreshTable();
            JOptionPane.showMessageDialog(
                null, 
                "Student deleted successfully.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to delete student. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

}