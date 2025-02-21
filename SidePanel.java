import java.awt.*;
import javax.swing.*;

public class SidePanel extends JPanel {
    public SidePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(0x5C2434));

        ImageIcon SVLogo = new ImageIcon("Assets/StudentVaultLogo.png");
        JLabel logo = new JLabel(SVLogo);
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon addIcon = new ImageIcon("Assets/AddIcon.png");
        JButton addButton = createButton(addIcon);

        this.add(logo);
        this.add(addButton);
    }

    private JButton createButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBackground(new Color(0x5C2434));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
