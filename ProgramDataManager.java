import java.io.*;
import java.util.*;

/**
 * Class to manage program data and handle CSV file operations
 */
public class ProgramDataManager {
    // CSV file path
    private static final String CSV_FILE_PATH = "Programs.csv";
    
    // Program form data
    private static String programName;
    private static String programCode;
    private static String collegeCode;
    
    // Getters and setters
    public static void setProgramName(String value) { programName = value; }
    public static String getProgramName() { return programName; }
    
    public static void setProgramCode(String value) { programCode = value; }
    public static String getProgramCode() { return programCode; }
    
    public static void setCollegeCode(String value) { collegeCode = value; }
    public static String getCollegeCode() { return collegeCode; }
    
    /**
     * Read all programs from CSV file
     * @return List of program data as String arrays [program_code, program_name, college_code]
     */
    public static List<String[]> readCSV() {
        List<String[]> programs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split line by comma, but handle quoted values properly
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Remove quotes from program name if present
                if (values.length >= 2 && values[1].startsWith("\"") && values[1].endsWith("\"")) {
                    values[1] = values[1].substring(1, values[1].length() - 1);
                }
                
                programs.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return programs;
    }
    
    /**
     * Write all programs to CSV file
     * @param programs List of program data to write
     * @return true if successful, false otherwise
     */
    public static boolean writeCSV(List<String[]> programs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            bw.write("program_code,program_name,college_code");
            bw.newLine();
            
            // Write data
            for (String[] program : programs) {
                String name = program[1];
                // Add quotes if name contains commas
                if (name.contains(",")) {
                    name = "\"" + name + "\"";
                }
                bw.write(program[0] + "," + name + "," + program[2]);
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Save program data to the CSV file
     * @return true if successful, false otherwise
     */
    public static boolean saveProgram() {
        if (!validateFields()) {
            return false;
        }
        
        List<String[]> programs = readCSV();
        
        // Check if program code already exists
        for (String[] program : programs) {
            if (program[0].equals(programCode)) {
                return false; // Program code already exists
            }
        }
        
        // Add new program
        programs.add(new String[] {programCode, programName, collegeCode});
        
        // Write back to CSV
        return writeCSV(programs);
    }
    
    /**
     * Retrieves program information from the CSV file based on program code
     * @param code The code of the program to retrieve
     * @return true if program found and data loaded, false otherwise
     */
    public static boolean getProgramByCode(String code) {
        List<String[]> programs = readCSV();
        
        for (String[] program : programs) {
            if (program[0].equals(code)) {
                // Load the retrieved data into the static fields
                programCode = program[0];
                programName = program[1];
                collegeCode = program[2];
                return true; // Program found and data loaded
            }
        }
        
        return false; // Program not found
    }
    
    /**
     * Retrieves the name of a program based on its code
     * @param programCode The code of the program
     * @return The name of the program, or null if not found
     */
    public static String getProgramName(String programCode) {
        List<String[]> programs = readCSV();
        
        for (String[] program : programs) {
            if (program[0].equals(programCode)) {
                return program[1];
            }
        }
        
        return null;
    }
    
    /**
     * Updates existing program information in the CSV file
     * @param oldProgramCode The current code of the program to update
     * @param newProgramName The new name for the program
     * @param newProgramCode The new code for the program
     * @return true if update successful, false otherwise
     */
    public static boolean updateProgram(String oldProgramCode, String newProgramName, String newProgramCode) {
        List<String[]> programs = readCSV();
        boolean updated = false;
        
        // Check if new code exists and is different from old code
        if (!oldProgramCode.equals(newProgramCode)) {
            for (String[] program : programs) {
                if (program[0].equals(newProgramCode)) {
                    return false; // New code already exists
                }
            }
        }
        
        // Update program information
        for (int i = 0; i < programs.size(); i++) {
            String[] program = programs.get(i);
            if (program[0].equals(oldProgramCode)) {
                // Keep the college code the same
                String currentCollegeCode = program[2];
                programs.set(i, new String[] {newProgramCode, newProgramName, currentCollegeCode});
                updated = true;
                break;
            }
        }
        
        // Write back to CSV if updated
        if (updated) {
            return writeCSV(programs);
        }
        
        return false;
    }
    
    /**
     * Adds a new program to the CSV file with the specified details
     * @param programName The name of the program to add
     * @param programCode The code for the program to add
     * @param collegeCode The code of the college to which this program belongs
     * @return true if program was added successfully, false otherwise
     */
    public static boolean addProgram(String programName, String programCode, String collegeCode) {
        // Validate inputs
        if (programName == null || programName.trim().isEmpty() ||
            programCode == null || programCode.trim().isEmpty() ||
            collegeCode == null || collegeCode.trim().isEmpty()) {
            return false;
        }
        
        // Trim inputs
        programName = programName.trim();
        programCode = programCode.trim();
        collegeCode = collegeCode.trim();
        
        // Check for duplicate program code
        if (programCodeExists(programCode)) {
            return false;
        }
        
        // Check for duplicate program name
        if (programNameExists(programName)) {
            return false;
        }
        
        // Set the static fields
        ProgramDataManager.programName = programName;
        ProgramDataManager.programCode = programCode;
        ProgramDataManager.collegeCode = collegeCode;
        
        // Save to CSV
        return saveProgram();
    }
    
    /**
     * Check if all required fields are filled
     * @return true if all fields have values, false otherwise
     */
    public static boolean validateFields() {
        return programName != null && !programName.isEmpty() &&
               programCode != null && !programCode.isEmpty() &&
               collegeCode != null && !collegeCode.isEmpty();
    }
    
    /**
     * Deletes a program from the CSV file based on program code
     * @param code The code of the program to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteProgram(String code) {
        List<String[]> programs = readCSV();
        int initialSize = programs.size();
        
        // Remove the program with the given code
        programs.removeIf(program -> program[0].equals(code));
        
        // Check if any program was removed
        if (programs.size() < initialSize) {
            return writeCSV(programs);
        }
        
        return false; // No program found with the given code
    }
    
    /**
     * Clear all form data
     */
    public static void clearFormData() {
        programName = null;
        programCode = null;
        collegeCode = null;
    }
    
    /**
     * Loads all programs into a map (code -> name)
     * @return Map of program codes to names
     */
    public static Map<String, String> loadProgramMap() {
        Map<String, String> programMap = new HashMap<>();
        List<String[]> programs = readCSV();
        
        for (String[] program : programs) {
            programMap.put(program[0], program[1]);
        }
        
        return programMap;
    }
    
    /**
     * Checks if a program code already exists
     * @param code The code to check
     * @return true if code exists, false otherwise
     */
    public static boolean programCodeExists(String code) {
        List<String[]> programs = readCSV();
        
        for (String[] program : programs) {
            if (program[0].equals(code)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if a program name already exists
     * @param name The name to check
     * @return true if name exists, false otherwise
     */
    public static boolean programNameExists(String name) {
        List<String[]> programs = readCSV();
        
        for (String[] program : programs) {
            if (program[1].equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Loads programs by college code
     * @param collegeCode The college code to filter by
     * @return Map of program codes to names
     */
    public static Map<String, String> loadProgramsByCollege(String collegeCode) {
        Map<String, String> programMap = new HashMap<>();
        List<String[]> programs = readCSV();
        
        for (String[] program : programs) {
            if (program[2].equals(collegeCode)) {
                programMap.put(program[0], program[1]);
            }
        }
        
        return programMap;
    }
}