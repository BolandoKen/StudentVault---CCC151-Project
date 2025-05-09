import java.awt.*;
import javax.swing.*;

public class SidePanel extends JPanel {
    private final ImageIcon addIconDefault = new ImageIcon("Assets/AddStudentIcon.png");
    private final ImageIcon addIconClicked = new ImageIcon("Assets/SelectedAddStudentIcon.png");
    private final ImageIcon tableIconDefault = new ImageIcon("Assets/StudentIcon.png");
    private final ImageIcon tableIconClicked = new ImageIcon("Assets/SelectedStudentIcon.png");
    private final ImageIcon collegeIconDefault = new ImageIcon("Assets/CollegeIcon.png");
    private final ImageIcon collegeIconClicked = new ImageIcon("Assets/SelectedCollegeIcon.png"); 
    private final ImageIcon programIconDefault = new ImageIcon("Assets/ProgramIcon.png");
    private final ImageIcon programIconClicked = new ImageIcon("Assets/SelectedProgramIcon.png"); 
    
    private JButton addButton;
    private JButton tableButton;
    private JButton activeButton = null;
    private JButton collegeButton;
    private JButton programButton;
    private GUI parentFrame;

    public SidePanel(GUI parentFrame) { 
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(0x5C2434));

        JLabel logo = new JLabel(new ImageIcon("Assets/StudentVaultLogo.png"));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton = createButton(addIconDefault);
        tableButton = createButton(tableIconDefault);
        collegeButton = createButton(collegeIconDefault);
        programButton = createButton(programIconDefault);

        this.add(logo);
        this.add(addButton);
        this.add(collegeButton);
        this.add(programButton);
        this.add(tableButton);
       
        addButton.addActionListener(e -> {
            updateButtonState(addButton);
            parentFrame.switchPanel("ADD_STUDENT");
        });

        collegeButton.addActionListener(e -> {
            updateButtonState(collegeButton);
            parentFrame.switchPanel("COLLEGETABLEPANEL");
            
        });

        programButton.addActionListener(e -> {
            updateButtonState(programButton);
            parentFrame.switchPanel("PROGRAMTABLEPANEL");
        });

        tableButton.addActionListener(e -> {
            updateButtonState(tableButton);
            parentFrame.switchPanel("TABLE");
        });
    }
    
    private void updateButtonState(JButton clickedButton) {
        // Reset previously active button if exists
        if (activeButton != null && activeButton != clickedButton) {
            if (activeButton == addButton) {
                activeButton.setIcon(addIconDefault);
            } else if (activeButton == tableButton) {
                activeButton.setIcon(tableIconDefault);
            } else if (activeButton == collegeButton) {
                activeButton.setIcon(collegeIconDefault);
            } else if (activeButton == programButton) {
                activeButton.setIcon(programIconDefault);
            }
        }
        
        activeButton = clickedButton;
        
        // Set clicked button state
        if (clickedButton == addButton) {
            clickedButton.setIcon(addIconClicked);
        } else if (clickedButton == tableButton) {
            clickedButton.setIcon(tableIconClicked);
        } else if (clickedButton == collegeButton) {
            clickedButton.setIcon(collegeIconClicked);
        } else if (clickedButton == programButton) {
            clickedButton.setIcon(programIconClicked);
        }
    }

    private JButton createButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBackground(new Color(0x5C2434));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}