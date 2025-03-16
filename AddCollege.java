import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCollege extends JFrame {
    private JTextField programNameField;
    private JButton saveButton;
    private JButton cancelButton;
    private String collegeName;
    private StudentForm parentForm;
    
    public AddCollege(String collegeName, StudentForm parentForm) {
        this.collegeName = collegeName;
        this.parentForm = parentForm;
        
        // Set up the frame
        setTitle("Add Program to " + collegeName);
        setSize(500, 300);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel with some padding
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header with college name
        JLabel headerLabel = new JLabel("Add New Program to " + collegeName);
        headerLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        headerLabel.setForeground(Color.decode("#6DBECA"));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Program name label and field
        JLabel programLabel = new JLabel("Program Name:");
        programLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        formPanel.add(programLabel, gbc);
        
        programNameField = new JTextField(20);
        programNameField.setFont(new Font("Helvetica", Font.PLAIN, 16));
        programNameField.setPreferredSize(new Dimension(300, 40));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        formPanel.add(programNameField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        cancelButton.setBackground(Color.LIGHT_GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });
        
        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        saveButton.setBackground(Color.decode("#6DBECA"));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String programName = programNameField.getText().trim();
                if (programName.isEmpty()) {
                    JOptionPane.showMessageDialog(AddCollege.this, 
                                                 "Please enter a program name", 
                                                 "Input Required", 
                                                 JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Add program to college using CollegeManager
                if (CollegeManager.addProgram(collegeName, programName)) {
                    JOptionPane.showMessageDialog(AddCollege.this, 
                                                 "Program added successfully", 
                                                 "Success", 
                                                 JOptionPane.INFORMATION_MESSAGE);
                    // Refresh parent form's college combo box
                    if (parentForm != null) {
                        parentForm.refreshCollegeData();
                    }
                    dispose(); // Close the window
                } else {
                    JOptionPane.showMessageDialog(AddCollege.this, 
                                                 "Program already exists or college not found", 
                                                 "Error", 
                                                 JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        // Add all components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the content pane
        setContentPane(mainPanel);
    }
}