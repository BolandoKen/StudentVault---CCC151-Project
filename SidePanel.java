import java.awt.*;
import javax.swing.*;

public class SidePanel extends JPanel {
    public SidePanel(GUI parentFrame) { // Accept reference to GUI
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(0x5C2434));

        JLabel logo = new JLabel(new ImageIcon("Assets/StudentVaultLogo.png"));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addButton = createButton(new ImageIcon("Assets/AddIcon.png"));
        JButton tableButton = createButton(new ImageIcon("Assets/TableIcon.png"));

        this.add(logo);
        this.add(addButton);
        this.add(tableButton);

        // Switch to AddStudentPanel when Add button is clicked
        addButton.addActionListener(e -> parentFrame.switchPanel("ADD_STUDENT"));

        // Switch back to TablePanel when Table button is clicked
        tableButton.addActionListener(e -> parentFrame.switchPanel("TABLE"));
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
