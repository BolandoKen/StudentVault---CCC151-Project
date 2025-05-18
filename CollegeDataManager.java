import java.io.*;
import java.util.*;

public class CollegeDataManager {
    private static final String CSV_FILE_PATH = "Colleges.csv";
    
    private static String collegeName;
    private static String collegeCode;
    
    public static void setCollegeName(String value) { collegeName = value; }
    public static String getCollegeName() { return collegeName; }
    
    public static void setCollegeCode(String value) { collegeCode = value; }
    public static String getCollegeCode() { return collegeCode; }
  
    // Read all colleges from CSV file
    private static List<String[]> readCSV() {
        List<String[]> colleges = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split line by comma, but handle quoted values properly
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Remove quotes from college name if present
                if (values.length >= 2 && values[1].startsWith("\"") && values[1].endsWith("\"")) {
                    values[1] = values[1].substring(1, values[1].length() - 1);
                }
                
                colleges.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return colleges;
    }
    
    // Write all colleges to CSV file
    private static boolean writeCSV(List<String[]> colleges) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            bw.write("college_code,college_name");
            bw.newLine();
            
            // Write data
            for (String[] college : colleges) {
                String name = college[1];
                // Add quotes if name contains commas
                if (name.contains(",")) {
                    name = "\"" + name + "\"";
                }
                bw.write(college[0] + "," + name);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
  
    public static boolean saveCollege() {
        if (!validateFields()) {
            return false;
        }
        
        List<String[]> colleges = readCSV();
        
        // Check if college code already exists
        for (String[] college : colleges) {
            if (college[0].equals(collegeCode)) {
                return false; // College code already exists
            }
        }
        
        // Add new college
        colleges.add(new String[] {collegeCode, collegeName});
        
        // Write back to CSV
        return writeCSV(colleges);
    }
  
    public static boolean getCollegeByCode(String code) {
        List<String[]> colleges = readCSV();
        
        for (String[] college : colleges) {
            if (college[0].equals(code)) {
                // Load the retrieved data into the static fields
                collegeCode = college[0];
                collegeName = college[1];
                return true; // College found and data loaded
            }
        }
        
        return false; // College not found
    }
    
    public static boolean addCollege(String name, String code) {
        // Validate input
        if (name == null || name.trim().isEmpty() || 
            code == null || code.trim().isEmpty()) {
            return false; // Invalid input
        }

        // Trim the input to remove any leading/trailing whitespace
        name = name.trim();
        code = code.trim();

        // Check if college code already exists
        if (collegeCodeExists(code)) {
            return false; // College code already exists
        }

        // Check if college name already exists
        if (collegeNameExists(name)) {
            return false; // College name already exists
        }
        
        // Set the static fields
        collegeName = name;
        collegeCode = code;
        
        // Save to CSV
        return saveCollege();
    }
    
    public static boolean updateCollege(String oldCode, String newName, String newCode) {
        List<String[]> colleges = readCSV();
        boolean oldCodeFound = false;
        boolean updated = false;
        
        // Check if new code exists and is different from old code
        if (!oldCode.equals(newCode) && collegeCodeExists(newCode)) {
            return false; // New code already exists
        }
        
        // Update college information
        for (int i = 0; i < colleges.size(); i++) {
            String[] college = colleges.get(i);
            if (college[0].equals(oldCode)) {
                if (!oldCode.equals(newCode)) {
                    // Code is changing, so make a new entry
                    colleges.set(i, new String[] {newCode, newName});
                } else {
                    // Just update the name
                    colleges.set(i, new String[] {oldCode, newName});
                }
                updated = true;
                break;
            }
        }
        
        // Write back to CSV if updated
        if (updated) {
            return writeCSV(colleges);
        }
        
        return false;
    }
    
    public static String getCollegeName(String code) {
        List<String[]> colleges = readCSV();
        
        for (String[] college : colleges) {
            if (college[0].equals(code)) {
                return college[1];
            }
        }
        
        return null;
    }
    
    public static boolean validateFields() {
        return collegeName != null && !collegeName.isEmpty() &&
               collegeCode != null && !collegeCode.isEmpty();
    }
    
    public static boolean deleteCollege(String code) {
        List<String[]> colleges = readCSV();
        int initialSize = colleges.size();
        
        // Remove the college with the given code
        colleges.removeIf(college -> college[0].equals(code));
        
        // Check if any college was removed
        if (colleges.size() < initialSize) {
            // Update programs with the deleted college code to "N/A"
            updateProgramsCollegeCode(code, "N/A");
            
            return writeCSV(colleges);
        }
        
        return false; // No college found with the given code
    }
    
    
    public static Map<String, String> loadCollegeMap() {
        Map<String, String> collegeMap = new HashMap<>();
        List<String[]> colleges = readCSV();
        
        for (String[] college : colleges) {
            collegeMap.put(college[0], college[1]);
        }
        
        return collegeMap;
    }
    
    public static boolean collegeCodeExists(String code) {
        List<String[]> colleges = readCSV();
        
        for (String[] college : colleges) {
            if (college[0].equals(code)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean collegeNameExists(String name) {
        List<String[]> colleges = readCSV();
        
        for (String[] college : colleges) {
            if (college[1].equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    /**
 * Updates the college code for all programs with a specific college code
 * @param oldCode The college code to replace
 * @param newCode The new college code to use
 * @return true if successful, false otherwise
 */
private static boolean updateProgramsCollegeCode(String oldCode, String newCode) {
    try {
        // Read all programs from CSV
        List<String[]> programs = new ArrayList<>();
        boolean changed = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader("Programs.csv"))) {
            String line;
            // Skip header
            String header = br.readLine();
            programs.add(header.split(","));
            
            while ((line = br.readLine()) != null) {
                // Split line by comma, but handle quoted values properly
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Check if this program has the old college code
                if (values.length >= 3 && values[2].equals(oldCode)) {
                    // Update to new college code
                    values[2] = newCode;
                    changed = true;
                }
                
                programs.add(values);
            }
        }
        
        // If we made changes, write back to file
        if (changed) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("Programs.csv"))) {
                // Write data
                for (int i = 0; i < programs.size(); i++) {
                    String[] program = programs.get(i);
                    
                    // For header just write it directly
                    if (i == 0) {
                        bw.write(String.join(",", program));
                    } else {
                        // Handle proper formatting for program data
                        String name = program[1];
                        // Add quotes if name contains commas
                        if (name.contains(",") && !(name.startsWith("\"") && name.endsWith("\""))) {
                            name = "\"" + name + "\"";
                        }
                        bw.write(program[0] + "," + name + "," + program[2]);
                    }
                    bw.newLine();
                }
            }
        }
        
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}
}