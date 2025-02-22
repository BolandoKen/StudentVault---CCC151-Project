import java.awt.*;
import javax.swing.*;

public class AddStudent extends JPanel {
    public AddStudent() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints gbcRow2 = new GridBagConstraints();
        gbcRow2.fill = GridBagConstraints.BOTH;
        gbcRow2.weightx = 1.0;

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(new Color(0xE7E7E7));
        topRow.setPreferredSize(new Dimension(1, 100)); // Ensures it has height
        gbcRow2.gridy = 0;
        gbcRow2.weighty = 0.02; // Takes 20% of row2's height
        this.add(topRow, gbcRow2);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(Color.red);
        gbcRow2.gridy = 1;
        gbcRow2.weighty = 0.98; // Takes 80% of row2's height
        this.add(bottomRow, gbcRow2);

        JLabel label = new JLabel("Add Student");
        label.setFont(new Font("Helvetica", Font.BOLD, 32));
        label.setForeground(Color.BLACK);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setBackground(new Color(0xE7E7E7));
        labelPanel.add(label);

        // Add to the bottom-left of topRow
        topRow.add(labelPanel, BorderLayout.SOUTH);

        JPanel Container = new JPanel(new GridBagLayout());


    }
}
