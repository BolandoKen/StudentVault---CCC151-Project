import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class CSearchPanels {

    public static class CollegeSearchPanel extends JPanel {
        private JTextField searchField;
        private JComboBox<String> searchColumnComboBox;
        private JButton searchButton;
        private JButton clearButton;
        private Consumer<String[]> searchCallback;
        private Timer searchTimer;

        public CollegeSearchPanel(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
            initializeUI();
        }

        public CollegeSearchPanel() {
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createTitledBorder("Search Colleges"));

            searchField = new JTextField(20);
            
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
            });

            searchColumnComboBox = new JComboBox<>(new String[]{"All", "College Code", "College Name"});
            searchColumnComboBox.addActionListener(e -> performSearch());

            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> performSearch());

            clearButton = new JButton("Clear");
            clearButton.addActionListener(e -> {
                searchField.setText("");
                searchColumnComboBox.setSelectedIndex(0);
                performSearch();
            });
    
            add(new JLabel("Search:"));
            add(searchField);
            add(new JLabel("in"));
            add(searchColumnComboBox);
            add(searchButton);
            add(clearButton);
           
            searchTimer = new Timer(300, e -> performSearch());
            searchTimer.setRepeats(false);
        }
        
        private void triggerDelayedSearch() {
            if (searchTimer.isRunning()) {
                searchTimer.stop();
            }
            searchTimer.start();
        }
        
        public void setSearchCallback(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
        }
    
        private void performSearch() {
            if (searchCallback == null) {
                return;
            }

            String searchText = searchField.getText().trim();
            String columnName = (String) searchColumnComboBox.getSelectedItem();
            
            searchCallback.accept(new String[]{searchText, columnName});
        }
        
        public void setSearchText(String text) {
            searchField.setText(text);
        }
     
        public void setSearchColumn(String columnName) {
            for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
                if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                    searchColumnComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        public void clearSearch() {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        }
       
        public void executeSearch() {
            performSearch();
        }
        
        public String getSearchText() {
            return searchField.getText();
        }

        public String getSelectedColumn() {
            return (String) searchColumnComboBox.getSelectedItem();
        }
    }

    public static class ProgramSearchPanel extends JPanel {
        private JTextField searchField;
        private JComboBox<String> searchColumnComboBox;
        private JButton searchButton;
        private JButton clearButton;
        private Consumer<String[]> searchCallback;
        private Timer searchTimer;
    
        public ProgramSearchPanel(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
            initializeUI();
        }
    
        public ProgramSearchPanel() {
            initializeUI();
        }
    
        private void initializeUI() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createTitledBorder("Search Programs"));
    
            searchField = new JTextField(20);
            
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
    
                @Override
                public void removeUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
    
                @Override
                public void changedUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
            });
    
            searchColumnComboBox = new JComboBox<>(new String[]{"All", "Program Name", "Program Code", "College Code"});
            searchColumnComboBox.addActionListener(e -> performSearch());
    
            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> performSearch());
    
            clearButton = new JButton("Clear");
            clearButton.addActionListener(e -> {
                searchField.setText("");
                searchColumnComboBox.setSelectedIndex(0);
                performSearch();
            });
    
            add(new JLabel("Search:"));
            add(searchField);
            add(new JLabel("in"));
            add(searchColumnComboBox);
            add(searchButton);
            add(clearButton);
    
            searchTimer = new Timer(300, e -> performSearch());
            searchTimer.setRepeats(false);
        }
    
        private void triggerDelayedSearch() {
            if (searchTimer.isRunning()) {
                searchTimer.stop();
            }
            searchTimer.start();
        }
    
        public void setSearchCallback(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
        }
    
        private void performSearch() {
            if (searchCallback == null) {
                return;
            }
    
            String searchText = searchField.getText().trim();
            String columnName = (String) searchColumnComboBox.getSelectedItem();
            
            searchCallback.accept(new String[]{searchText, columnName});
        }
    
        public void setSearchText(String text) {
            searchField.setText(text);
        }
    
    
        public void setSearchColumn(String columnName) {
            for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
                if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                    searchColumnComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    
        public void clearSearch() {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        }
    
        public void executeSearch() {
            performSearch();
        }
    
        public String getSearchText() {
            return searchField.getText();
        }
    
        public String getSelectedColumn() {
            return (String) searchColumnComboBox.getSelectedItem();
        }
    }

    public static class StudentSearchPanel extends JPanel {
        private JTextField searchField;
        private JComboBox<String> searchColumnComboBox;
        private JButton searchButton;
        private JButton clearButton;
        private Consumer<String[]> searchCallback;
        private Timer searchTimer;
    
        
        public StudentSearchPanel(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
            initializeUI();
        }
    
        public StudentSearchPanel() {
            initializeUI();
        }
    
        private void initializeUI() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createTitledBorder("Search Students"));
    
            searchField = new JTextField(20);
            
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
    
                @Override
                public void removeUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
    
                @Override
                public void changedUpdate(DocumentEvent e) {
                    triggerDelayedSearch();
                }
            });
    
            searchColumnComboBox = new JComboBox<>(new String[]{"All", "ID Number", "First Name", "Last Name", 
                                                               "Gender", "Year Level", "Program Code"});
            searchColumnComboBox.addActionListener(e -> performSearch());
    
            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> performSearch());
  
            clearButton = new JButton("Clear");
            clearButton.addActionListener(e -> {
                searchField.setText("");
                searchColumnComboBox.setSelectedIndex(0);
                performSearch();
            });
    
            add(new JLabel("Search:"));
            add(searchField);
            add(new JLabel("in"));
            add(searchColumnComboBox);
            add(searchButton);
            add(clearButton);
    
            searchTimer = new Timer(300, e -> performSearch());
            searchTimer.setRepeats(false);
        }
    
        private void triggerDelayedSearch() {
            if (searchTimer.isRunning()) {
                searchTimer.stop();
            }
            searchTimer.start();
        }
    
        public void setSearchCallback(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
        }
    
        private void performSearch() {
            if (searchCallback == null) {
                return;
            }
    
            String searchText = searchField.getText().trim();
            String columnName = (String) searchColumnComboBox.getSelectedItem();
            
            searchCallback.accept(new String[]{searchText, columnName});
        }
    
        public void setSearchText(String text) {
            searchField.setText(text);
        }
    
        public void setSearchColumn(String columnName) {
            for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
                if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                    searchColumnComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        public void clearSearch() {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        }
    
        public void executeSearch() {
            performSearch();
        }
    
        public String getSearchText() {
            return searchField.getText();
        }
    
        public String getSelectedColumn() {
            return (String) searchColumnComboBox.getSelectedItem();
        }
    }

}