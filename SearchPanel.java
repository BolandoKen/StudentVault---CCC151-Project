import java.awt.*;
import javax.swing.*;

public class SearchPanel extends JPanel {
    public SearchPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 0);
        gbc.weightx = 1.0;

        RoundedPanel searchPanel = new RoundedPanel(10);
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(new Color(0xE7E7E7));

        RoundedTextField searchField = new RoundedTextField(200, 10, "Search", false);
        searchField.setBackground(new Color(0xE7E7E7));
        searchField.setFont(new Font("Helvetica", Font.PLAIN, 18));
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JButton clearButton = createButton("Assets/XIcon.png");

        searchField.addActionListener(e -> System.out.println("Searching: " + searchField.getText()));
        clearButton.addActionListener(e -> searchField.setText(""));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        this.add(searchPanel, gbc);

        // Filter Button
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 0, 10, 10);
        JButton filterButton = createButton("Assets/FilterIcon.png");
        filterButton.addActionListener(e -> System.out.println("Filtering"));
        this.add(filterButton, gbc);
    }

    private JButton createButton(String iconPath) {
        JButton button = new JButton(new ImageIcon(iconPath));
        button.setPreferredSize(new Dimension(40, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }
}
