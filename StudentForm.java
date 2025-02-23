import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StudentForm extends JPanel {
    public StudentForm() {
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

        JLabel addStudent = new JLabel("Add New Student");
        addStudent.setFont(new Font("Helvetica", Font.BOLD, 32));
        additionalInfoPanel.add(addStudent);
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
        RoundedTextField firstnameField = new RoundedTextField(30, 10, "Firstname", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        row1.add(firstnameField, nameGbc);

        nameGbc.gridx = 1;
        RoundedTextField lastnameField = new RoundedTextField(30, 10, "Firstname", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
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
        RoundedComboBox genderComboBox = new RoundedComboBox(
            new String[]{"Male", "Female", "Other"},
            Color.decode("#E7E7E7"),
            Color.gray,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(150, 40)
            , "Gender",
            Color.decode("#6DBECA")
        );
        row2.add(genderComboBox, row2Gbc);

        // ID Number Field
        row2Gbc.gridx = 1;
        JPanel idPanel = new JPanel(new BorderLayout());
        idPanel.setBackground(Color.white);
        idPanel.setPreferredSize(new Dimension(150, 40));
        RoundedTextField idField = new RoundedTextField(30, 10, "ID Number", false, "#E7E7E7", "Helvetica", Font.PLAIN, 18);
        idPanel.add(idField, BorderLayout.CENTER);
        row2.add(idPanel, row2Gbc);

        // Year Level Dropdown
        row2Gbc.gridx = 2;
        RoundedComboBox yearLevelComboBox = new RoundedComboBox(
            new String[]{"1st Year", "2nd Year", "3rd Year", "4th Year"},
            Color.decode("#E7E7E7"),
            Color.GRAY,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(150, 40) 
            , "Year Level",
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
        String[] colleges = {
        "Collage of Computer Studies", "College of Engineering", "College of Science and Mathemathics", 
        "College of Economics and Business Accountancy", "College of Arts and Social Sciences",
        "College of Education", "College of Health Sciences",
        };
        RoundedComboBox collegeComboBox = new RoundedComboBox(
            colleges,
            Color.decode("#E7E7E7"),
            Color.GRAY,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(450, 40) 
            , "College",
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
        String[] programs = {
        "Collage of Computer Studies", "College of Engineering", "College of Science and Mathemathics", 
        "College of Economics and Business Accountancy", "College of Arts and Social Sciences",
        "College of Education", "College of Health Sciences",
        };
        RoundedComboBox programComboBox = new RoundedComboBox(
            programs,
            Color.decode("#E7E7E7"),
            Color.GRAY,
            new Font("Helvetica", Font.PLAIN, 18),
            10, new Dimension(450, 40) 
            , "Program",
            Color.decode("#6DBECA")
        );
        programPanel.add(programComboBox, BorderLayout.CENTER);
        row4.add(programPanel, row4Gbc);
        add(row4, gbc);

        // Row 5: Add Student Panel
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

        JPanel addPanel = new JPanel(new BorderLayout());
        addPanel.setBackground(Color.white);
        RoundedButton addButton = new RoundedButton(
            "Add Student",
            Color.decode("#6DBECA"),  // Background color (blue)
            Color.WHITE,              // Text color
            new Font("Helvetica", Font.PLAIN, 18), // Custom font
            10                        // Corner radius
        );
        addButton.setPreferredSize(new Dimension(150, 40));
        addPanel.add(addButton, BorderLayout.CENTER);
        row5.add(addPanel, row5Gbc);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String firstName = firstnameField.getText();
                String lastName = lastnameField.getText();
                String gender = genderComboBox.getSelectedItem().toString();
                String idNumber = idField.getText();
                String yearLevel = yearLevelComboBox.getSelectedItem().toString();
                String college = collegeComboBox.getSelectedItem().toString();
                String program = programComboBox.getSelectedItem().toString();

                if(firstName.isEmpty() || lastName.isEmpty()) {
                   System.err.println("Please fill all fields!");
                    return;
                }
                Student student = new Student(firstName, lastName, gender, idNumber, yearLevel, college, program); // Add other fields as needed
            }
        });
        add(row5, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Form Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new StudentForm(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }
}
