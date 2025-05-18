
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CLogo {
    private static final String LOGO_PATH = "Assets/StudentVaultLogo.png";
    
    public static JLabel createLogo() {
        JLabel logo = new JLabel(new ImageIcon(LOGO_PATH));
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return logo;
    }
}