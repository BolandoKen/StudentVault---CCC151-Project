import javax.swing.*;

public class StudentVault extends JFrame {

    public StudentVault() {
        initComponents();
    }

    private void initComponents() {
        jButton1 = new JButton();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jButton1.setBackground(new java.awt.Color(51, 153, 255));
        jButton1.setText("Add Student");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton1)
                .addContainerGap(275, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(251, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(26, 26, 26))
        );

        pack();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new StudentVault().setVisible(true));
    }

    private JButton jButton1;
}
