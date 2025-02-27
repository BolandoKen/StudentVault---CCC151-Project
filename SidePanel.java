import java.awt.*;
import javax.swing.*;

public class SidePanel extends JPanel {
    private final ImageIcon addIconDefault = new ImageIcon("Assets/AddIcon.png");
    private final ImageIcon addIconClicked = new ImageIcon("Assets/SelectedAddIcon.png");
    private final ImageIcon tableIconDefault = new ImageIcon("Assets/TableIcon.png");
    private final ImageIcon tableIconClicked = new ImageIcon("Assets/SelectedTableIcon.png");
    
    private JButton addButton;
    private JButton tableButton;
    private JButton activeButton = null;

    public SidePanel(GUI parentFrame) { 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(0x5C2434));

        JLabel logo = new JLabel(new ImageIcon("Assets/StudentVaultLogo.png"));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton = createButton(addIconDefault);
        tableButton = createButton(tableIconDefault);

        this.add(logo);
        this.add(addButton);
        this.add(tableButton);

        addButton.addActionListener(e -> {
            updateButtonState(addButton);
            parentFrame.switchPanel("ADD_STUDENT");
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
            }
        }
        
        activeButton = clickedButton;
        if (clickedButton == addButton) {
            clickedButton.setIcon(addIconClicked);
        } else if (clickedButton == tableButton) {
            clickedButton.setIcon(tableIconClicked);
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