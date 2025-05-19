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

        /**
         * Constructs a college search panel
         * 
         * @param searchCallback Callback that receives search parameters [searchText, columnName]
         *                       where columnName can be "All", "College Code", or "College Name"
         */
        public CollegeSearchPanel(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
            initializeUI();
        }

        /**
         * Default constructor - must set callback later using setSearchCallback
         */
        public CollegeSearchPanel() {
            initializeUI();
        }

        /**
         * Sets up the UI components
         */
        private void initializeUI() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createTitledBorder("Search Colleges"));

            // Create search field
            searchField = new JTextField(20);
            
            // Add document listener for live search
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

            // Create column selector for search
            searchColumnComboBox = new JComboBox<>(new String[]{"All", "College Code", "College Name"});
            searchColumnComboBox.addActionListener(e -> performSearch());

            // Create search button (still available for explicit search if needed)
            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> performSearch());

            // Create clear button
            clearButton = new JButton("Clear");
            clearButton.addActionListener(e -> {
                searchField.setText("");
                searchColumnComboBox.setSelectedIndex(0);
                performSearch();
            });

            // Add components to panel
            add(new JLabel("Search:"));
            add(searchField);
            add(new JLabel("in"));
            add(searchColumnComboBox);
            add(searchButton);
            add(clearButton);

            // Initialize search timer for delayed search
            searchTimer = new Timer(300, e -> performSearch());
            searchTimer.setRepeats(false);
        }

        /**
         * Triggers a delayed search after user stops typing
         */
        private void triggerDelayedSearch() {
            if (searchTimer.isRunning()) {
                searchTimer.stop();
            }
            searchTimer.start();
        }

        /**
         * Sets the search callback
         * 
         * @param searchCallback Callback that receives search parameters [searchText, columnName]
         */
        public void setSearchCallback(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
        }

        /**
         * Performs the search by calling the callback with current search parameters
         */
        private void performSearch() {
            if (searchCallback == null) {
                return;
            }

            String searchText = searchField.getText().trim();
            String columnName = (String) searchColumnComboBox.getSelectedItem();
            
            searchCallback.accept(new String[]{searchText, columnName});
        }

        /**
         * Sets the search text programmatically
         * 
         * @param text The text to search for
         */
        public void setSearchText(String text) {
            searchField.setText(text);
        }

        /**
         * Sets the search column programmatically
         * 
         * @param columnName The column to search in ("All", "College Code", or "College Name")
         */
        public void setSearchColumn(String columnName) {
            for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
                if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                    searchColumnComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        /**
         * Clears the search criteria
         */
        public void clearSearch() {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        }

        /**
         * Executes the search with current parameters
         */
        public void executeSearch() {
            performSearch();
        }

        /**
         * Gets the current search text
         * 
         * @return The current search text
         */
        public String getSearchText() {
            return searchField.getText();
        }

        /**
         * Gets the currently selected column for searching
         * 
         * @return The selected column name
         */
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
    
        /**
         * Constructs a program search panel
         * 
         * @param searchCallback Callback that receives search parameters [searchText, columnName]
         *                       where columnName can be "All", "Program Name", "Program Code", or "College Code"
         */
        public ProgramSearchPanel(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
            initializeUI();
        }
    
        /**
         * Default constructor - must set callback later using setSearchCallback
         */
        public ProgramSearchPanel() {
            initializeUI();
        }
    
        /**
         * Sets up the UI components
         */
        private void initializeUI() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createTitledBorder("Search Programs"));
    
            // Create search field
            searchField = new JTextField(20);
            
            // Add document listener for live search
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
    
            // Create column selector for search (matches CProgramTable columns)
            searchColumnComboBox = new JComboBox<>(new String[]{"All", "Program Name", "Program Code", "College Code"});
            searchColumnComboBox.addActionListener(e -> performSearch());
    
            // Create search button (still available for explicit search if needed)
            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> performSearch());
    
            // Create clear button
            clearButton = new JButton("Clear");
            clearButton.addActionListener(e -> {
                searchField.setText("");
                searchColumnComboBox.setSelectedIndex(0);
                performSearch();
            });
    
            // Add components to panel
            add(new JLabel("Search:"));
            add(searchField);
            add(new JLabel("in"));
            add(searchColumnComboBox);
            add(searchButton);
            add(clearButton);
    
            // Initialize search timer for delayed search
            searchTimer = new Timer(300, e -> performSearch());
            searchTimer.setRepeats(false);
        }
    
        /**
         * Triggers a delayed search after user stops typing
         */
        private void triggerDelayedSearch() {
            if (searchTimer.isRunning()) {
                searchTimer.stop();
            }
            searchTimer.start();
        }
    
        /**
         * Sets the search callback
         * 
         * @param searchCallback Callback that receives search parameters [searchText, columnName]
         */
        public void setSearchCallback(Consumer<String[]> searchCallback) {
            this.searchCallback = searchCallback;
        }
    
        /**
         * Performs the search by calling the callback with current search parameters
         */
        private void performSearch() {
            if (searchCallback == null) {
                return;
            }
    
            String searchText = searchField.getText().trim();
            String columnName = (String) searchColumnComboBox.getSelectedItem();
            
            searchCallback.accept(new String[]{searchText, columnName});
        }
    
        /**
         * Sets the search text programmatically
         * 
         * @param text The text to search for
         */
        public void setSearchText(String text) {
            searchField.setText(text);
        }
    
        /**
         * Sets the search column programmatically
         * 
         * @param columnName The column to search in ("All", "Program Name", "Program Code", or "College Code")
         */
        public void setSearchColumn(String columnName) {
            for (int i = 0; i < searchColumnComboBox.getItemCount(); i++) {
                if (searchColumnComboBox.getItemAt(i).equals(columnName)) {
                    searchColumnComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    
        /**
         * Clears the search criteria
         */
        public void clearSearch() {
            searchField.setText("");
            searchColumnComboBox.setSelectedIndex(0);
            performSearch();
        }
    
        /**
         * Executes the search with current parameters
         */
        public void executeSearch() {
            performSearch();
        }
    
        /**
         * Gets the current search text
         * 
         * @return The current search text
         */
        public String getSearchText() {
            return searchField.getText();
        }
    
        /**
         * Gets the currently selected column for searching
         * 
         * @return The selected column name
         */
        public String getSelectedColumn() {
            return (String) searchColumnComboBox.getSelectedItem();
        }
    }
}