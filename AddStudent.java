import java.awt.*;
import javax.swing.*;

public class AddStudent extends JPanel {
    private final StudentForm studentForm;  // Store the StudentForm instance
    
    public AddStudent(TablePanel tablePanel) {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        // Create and store the StudentForm instance
        this.studentForm = new StudentForm(tablePanel);

        GridBagConstraints gbcRow2 = new GridBagConstraints();
        gbcRow2.fill = GridBagConstraints.BOTH;
        gbcRow2.weightx = 1.0;

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(new Color(0xE7E7E7));
        topRow.setPreferredSize(new Dimension(1, 100)); 
        gbcRow2.gridy = 0;
        gbcRow2.weighty = 0.02; 
        this.add(topRow, gbcRow2);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(new Color(0xE7E7E7));
        gbcRow2.gridy = 1;
        gbcRow2.weighty = 0.98; 
        this.add(bottomRow, gbcRow2);

        JLabel label = new JLabel("StudentVault");
        label.setFont(new Font("Helvetica", Font.BOLD, 32));
        label.setForeground(Color.GRAY);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setBackground(new Color(0xE7E7E7));
        labelPanel.add(label);

        topRow.add(labelPanel, BorderLayout.SOUTH);
        
        RoundedPanel formPanel = new RoundedPanel(10);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(0xffffff));
        formPanel.add(studentForm);  // Use the stored instance
        formPanel.setPreferredSize(new Dimension(400, 300));
        bottomRow.add(formPanel, BorderLayout.CENTER);
    }

    public StudentForm getStudentForm() {
        return studentForm;
    }
}