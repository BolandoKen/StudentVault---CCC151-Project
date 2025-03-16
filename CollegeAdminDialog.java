import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CollegeAdminDialog extends JDialog {
    private JList<String> collegeList;
    private JList<String> programList;
    private DefaultListModel<String> collegeListModel;
    private DefaultListModel<String> programListModel;
    private JButton addCollegeButton;
    private JButton removeCollegeButton;
    private JButton addProgramButton;
    private JButton removeProgramButton;
    private StudentForm parentForm;
    private JButton editCollegeButton;
    private JButton editProgramButton;
    private JTextField collegeAbbrField;
    private JTextField programAbbrField;

    public CollegeAdminDialog(Frame owner, StudentForm parentForm) {
        super(owner, "College and Program Administration", true);
        this.parentForm = parentForm;
        
        setSize(800, 500);
        setLocationRelativeTo(owner);
        
        initComponents();
        loadCollegeData();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Manage Colleges and Programs");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Lists Panel (Center)
        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // College Panel
        JPanel collegePanel = new JPanel(new BorderLayout(0, 5));
        collegePanel.setBorder(BorderFactory.createTitledBorder("Colleges"));
        JPanel collegeDetailsPanel = new JPanel(new BorderLayout(5, 0));
    
    collegeListModel = new DefaultListModel<>();
    collegeList = new JList<>(collegeListModel);
    collegeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    collegeList.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                updateProgramList();
                updateCollegeAbbreviation();
                updateButtonStates();
            }
        }
    });
    
    JScrollPane collegeScrollPane = new JScrollPane(collegeList);
    collegeDetailsPanel.add(collegeScrollPane, BorderLayout.CENTER);

    JPanel collegeAbbrPanel = new JPanel(new BorderLayout(5, 0));
    collegeAbbrPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    JLabel collegeAbbrLabel = new JLabel("Abbreviation:");
    collegeAbbrField = new JTextField(10);
    collegeAbbrField.setEnabled(false); // Only editable when editing a college
    
    collegeAbbrPanel.add(collegeAbbrLabel, BorderLayout.WEST);
    collegeAbbrPanel.add(collegeAbbrField, BorderLayout.CENTER);
    collegeDetailsPanel.add(collegeAbbrPanel, BorderLayout.SOUTH);
    
    collegePanel.add(collegeDetailsPanel, BorderLayout.CENTER);
    
        JPanel collegeButtonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        collegeButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding

        addCollegeButton = new JButton("Add College");
        removeCollegeButton = new JButton("Remove College");
        
        addCollegeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCollege();
            }
        });
        
        removeCollegeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeCollege();
            }
        });
        editCollegeButton = new JButton("Edit College");
        editCollegeButton.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {
                editCollege();
        }
        });
        
        collegeButtonPanel.add(addCollegeButton);
        collegeButtonPanel.add(removeCollegeButton);
        collegeButtonPanel.add(editCollegeButton);  
        collegePanel.add(collegeButtonPanel, BorderLayout.SOUTH);
        
        // Program Panel
       JPanel programPanel = new JPanel(new BorderLayout(0, 5));
    programPanel.setBorder(BorderFactory.createTitledBorder("Programs"));
    
    JPanel programDetailsPanel = new JPanel(new BorderLayout(5, 0));
    
    programListModel = new DefaultListModel<>();
    programList = new JList<>(programListModel);
    programList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    programList.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                updateProgramAbbreviation();
                updateButtonStates();
            }
        }
    });
    JScrollPane programScrollPane = new JScrollPane(programList);
    programDetailsPanel.add(programScrollPane, BorderLayout.CENTER);
    
    // Add abbreviation panel below the program list
    JPanel programAbbrPanel = new JPanel(new BorderLayout(5, 0));
    programAbbrPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
    JLabel programAbbrLabel = new JLabel("Abbreviation:");
    programAbbrField = new JTextField(10);
    programAbbrField.setEnabled(false);

    programAbbrPanel.add(programAbbrLabel, BorderLayout.WEST);
    programAbbrPanel.add(programAbbrField, BorderLayout.CENTER);
    programDetailsPanel.add(programAbbrPanel, BorderLayout.SOUTH);
    
    programPanel.add(programDetailsPanel, BorderLayout.CENTER);
    

        JPanel programButtonPanel = new JPanel(new GridLayout(1, 3, 5, 0)); // Use GridLayout with gaps between components
        programButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
        addProgramButton = new JButton("Add Program");
        removeProgramButton = new JButton("Remove Program");
        
        addProgramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProgram();
            }
        });
        
        removeProgramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProgram();
            }
        });
        editProgramButton = new JButton("Edit Program");
        editProgramButton.addActionListener(new ActionListener() {
          @Override
             public void actionPerformed(ActionEvent e) {
            editProgram();
         }
        });
       
        programButtonPanel.add(addProgramButton);
        programButtonPanel.add(removeProgramButton);
        programButtonPanel.add(editProgramButton);
        programPanel.add(programButtonPanel, BorderLayout.SOUTH);
        
        listsPanel.add(collegePanel);
        listsPanel.add(programPanel);
        mainPanel.add(listsPanel, BorderLayout.CENTER);
        
        // Close Button (South)
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        closePanel.add(closeButton);
        mainPanel.add(closePanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        updateButtonStates();
    }
    
    private void loadCollegeData() {
        collegeListModel.clear();
        programListModel.clear();
        
        Map<String, Map<String, Object>> colleges = CollegeManager.loadColleges();
        
        for (String collegeName : colleges.keySet()) {
            collegeListModel.addElement(collegeName);
        }
        
        // Clear abbreviation fields
        collegeAbbrField.setText("");
        programAbbrField.setText("");
        
        // Update button states
        updateButtonStates();
    }
    
    private void updateProgramList() {
        programListModel.clear();
        String selectedCollege = collegeList.getSelectedValue();
        
        if (selectedCollege != null) {
            String[] programs = CollegeManager.getProgramsForCollege(selectedCollege);
            for (String program : programs) {
                programListModel.addElement(program);
            }
        }
    }
    
    private void updateButtonStates() {
        boolean collegeSelected = collegeList.getSelectedValue() != null;
        boolean programSelected = programList.getSelectedValue() != null;
        
        removeCollegeButton.setEnabled(collegeSelected);
        editCollegeButton.setEnabled(collegeSelected);
        addProgramButton.setEnabled(collegeSelected);
        removeProgramButton.setEnabled(programSelected);
        editProgramButton.setEnabled(programSelected);
    }
    
    private void addCollege() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField abbrField = new JTextField();
        
        inputPanel.add(new JLabel("College Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Abbreviation:"));
        inputPanel.add(abbrField);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            inputPanel, 
            "Add New College", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String collegeName = nameField.getText().trim();
            String abbreviation = abbrField.getText().trim();
            
            if (collegeName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "College name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
                
            // Check for duplicates
            for (int i = 0; i < collegeListModel.size(); i++) {
                if (collegeListModel.get(i).equals(collegeName)) {
                    JOptionPane.showMessageDialog(this, "College already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!abbreviation.isEmpty()) {
                // Check for duplicate abbreviations
                Map<String, Map<String, Object>> allColleges = CollegeManager.loadColleges();
                for (Map.Entry<String, Map<String, Object>> entry : allColleges.entrySet()) {
                    String existingAbbr = (String) entry.getValue().get("abbreviation");
                    if (abbreviation.equalsIgnoreCase(existingAbbr)) {
                        JOptionPane.showMessageDialog(this, "College abbreviation already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            
            // Add college with abbreviation
            CollegeManager.addCollege(collegeName, abbreviation);
            loadCollegeData();
            
            // Select the newly added college
            collegeList.setSelectedValue(collegeName, true);
            
            // Refresh parent form
            if (parentForm != null) {
                parentForm.refreshCollegeData();
            }
        }
    }
    private void removeCollege() {
        String selectedCollege = collegeList.getSelectedValue();
        if (selectedCollege != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove the college: " + selectedCollege + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (CollegeManager.removeCollege(selectedCollege)) {
                    loadCollegeData();
                    
                    // Refresh parent form AND table
                    if (parentForm != null) {
                        parentForm.refreshCollegeData();
                        parentForm.refreshTable(); // Use public method instead of direct access
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove college", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void removeProgram() {
        String selectedCollege = collegeList.getSelectedValue();
        String selectedProgram = programList.getSelectedValue();
        
        if (selectedCollege != null && selectedProgram != null) {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove the program: " + selectedProgram + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (CollegeManager.removeProgram(selectedCollege, selectedProgram)) {
                    updateProgramList();
                    
                    // Refresh parent form AND table
                    if (parentForm != null) {
                        parentForm.refreshCollegeData();
                        parentForm.refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove program", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } // Closing brace for removeProgram()
    // Add these methods to the CollegeAdminDialog class
    private void editCollege() {
        String selectedCollege = collegeList.getSelectedValue();
        if (selectedCollege != null) {
            String currentAbbr = collegeAbbrField.getText();
            
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField nameField = new JTextField(selectedCollege);
            JTextField abbrField = new JTextField(currentAbbr);
            
            inputPanel.add(new JLabel("College Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Abbreviation:"));
            inputPanel.add(abbrField);
            
            int result = JOptionPane.showConfirmDialog(
                this, 
                inputPanel, 
                "Edit College", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String newCollegeName = nameField.getText().trim();
                String newAbbreviation = abbrField.getText().trim();
                
                if (newCollegeName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "College name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check for duplicates if name changed
                if (!newCollegeName.equals(selectedCollege)) {
                    for (int i = 0; i < collegeListModel.size(); i++) {
                        if (collegeListModel.get(i).equals(newCollegeName)) {
                            JOptionPane.showMessageDialog(this, "College already exists", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                if (!newAbbreviation.equals(currentAbbr)) {
                    // Check for duplicate abbreviations (only if changed)
                    Map<String, Map<String, Object>> allColleges = CollegeManager.loadColleges();
                    for (Map.Entry<String, Map<String, Object>> entry : allColleges.entrySet()) {
                        if (!entry.getKey().equals(selectedCollege)) { // Don't compare with itself
                            String existingAbbr = (String) entry.getValue().get("abbreviation");
                            if (newAbbreviation.equalsIgnoreCase(existingAbbr)) {
                                JOptionPane.showMessageDialog(this, "College abbreviation already exists", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                }
                
                // Update college with new name and abbreviation
                if (CollegeManager.updateCollegeName(selectedCollege, newCollegeName, newAbbreviation)) {
                    loadCollegeData();
                    
                    // Select the edited college
                    collegeList.setSelectedValue(newCollegeName, true);
                    
                    // Refresh parent form
                    if (parentForm != null) {
                        parentForm.refreshCollegeData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update college", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private void addProgram() {
        String selectedCollege = collegeList.getSelectedValue();
        if (selectedCollege != null) {
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField nameField = new JTextField();
            JTextField abbrField = new JTextField();
            
            inputPanel.add(new JLabel("Program Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Abbreviation:"));
            inputPanel.add(abbrField);
            
            int result = JOptionPane.showConfirmDialog(
                this, 
                inputPanel, 
                "Add New Program", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String programName = nameField.getText().trim();
                String abbreviation = abbrField.getText().trim();
                
                if (programName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Program name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check for duplicates
                for (int i = 0; i < programListModel.size(); i++) {
                    if (programListModel.get(i).equals(programName)) {
                        JOptionPane.showMessageDialog(this, "Program already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (!abbreviation.isEmpty()) {
                    // Check for duplicate program abbreviations within the same college
                    String[] programs = CollegeManager.getProgramsForCollege(selectedCollege);
                    for (String existingProgram : programs) {
                        String existingAbbr = CollegeManager.getProgramAbbreviation(selectedCollege, existingProgram);
                        if (abbreviation.equalsIgnoreCase(existingAbbr)) {
                            JOptionPane.showMessageDialog(this, "Program abbreviation already exists in this college", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                // Add program with abbreviation
                if (CollegeManager.addProgram(selectedCollege, programName, abbreviation)) {
                    updateProgramList();
                    
                    // Select the newly added program
                    programList.setSelectedValue(programName, true);
                    
                    // Refresh parent form
                    if (parentForm != null) {
                        parentForm.refreshCollegeData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add program", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void editProgram() {
        String selectedCollege = collegeList.getSelectedValue();
        String selectedProgram = programList.getSelectedValue();
        
        if (selectedCollege != null && selectedProgram != null) {
            String currentAbbr = programAbbrField.getText();
            
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField nameField = new JTextField(selectedProgram);
            JTextField abbrField = new JTextField(currentAbbr);
            
            inputPanel.add(new JLabel("Program Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Abbreviation:"));
            inputPanel.add(abbrField);
            
            int result = JOptionPane.showConfirmDialog(
                this, 
                inputPanel, 
                "Edit Program", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (result == JOptionPane.OK_OPTION) {
                String newProgramName = nameField.getText().trim();
                String newAbbreviation = abbrField.getText().trim();
                
                if (newProgramName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Program name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check for duplicates if name changed
                if (!newProgramName.equals(selectedProgram)) {
                    for (int i = 0; i < programListModel.size(); i++) {
                        if (programListModel.get(i).equals(newProgramName)) {
                            JOptionPane.showMessageDialog(this, "Program already exists", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                if (!newAbbreviation.equals(currentAbbr)) {
                    // Check for duplicate program abbreviations (only if changed)
                    String[] programs = CollegeManager.getProgramsForCollege(selectedCollege);
                    for (String existingProgram : programs) {
                        if (!existingProgram.equals(selectedProgram)) { // Don't compare with itself
                            String existingAbbr = CollegeManager.getProgramAbbreviation(selectedCollege, existingProgram);
                            if (newAbbreviation.equalsIgnoreCase(existingAbbr)) {
                                JOptionPane.showMessageDialog(this, "Program abbreviation already exists in this college", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }
                }
                
                // Update program with new name and abbreviation
                if (CollegeManager.updateProgramName(selectedCollege, selectedProgram, newProgramName, newAbbreviation)) {
                    updateProgramList();
                    
                    // Select the edited program
                    programList.setSelectedValue(newProgramName, true);
                    
                    // Refresh parent form
                    if (parentForm != null) {
                        parentForm.refreshCollegeData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update program", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
private void updateCollegeAbbreviation() {
    String selectedCollege = collegeList.getSelectedValue();
    if (selectedCollege != null) {
        String abbr = CollegeManager.getCollegeAbbreviation(selectedCollege);
        collegeAbbrField.setText(abbr);
    } else {
        collegeAbbrField.setText("");
    }
}

private void updateProgramAbbreviation() {
    String selectedCollege = collegeList.getSelectedValue();
    String selectedProgram = programList.getSelectedValue();
    if (selectedCollege != null && selectedProgram != null) {
        String abbr = CollegeManager.getProgramAbbreviation(selectedCollege, selectedProgram);
        programAbbrField.setText(abbr);
    } else {
        programAbbrField.setText("");
    }
}

}