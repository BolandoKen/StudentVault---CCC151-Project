import java.io.*;
import java.util.*;

public class CollegeAbbreviationConverter {
    private static final Map<String, String> collegeToAbbreviation = new HashMap<>();
    private static final Map<String, String> programToAbbreviation = new HashMap<>();
    private static final Map<String, String> abbreviationToCollege = new HashMap<>();
    private static final Map<String, String> abbreviationToProgram = new HashMap<>();
    
    private static final String COLLEGES_CSV_PATH = "colleges.csv";
    
    static {
        // Load colleges and programs from CSV instead of hardcoding
        loadMappingsFromCSV();
        
        // Still initialize hardcoded programs as fallback
        initializePrograms();
    }
    
    private static void loadMappingsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(COLLEGES_CSV_PATH))) {
            String line;
            String currentCollege = null;
            String currentAbbreviation = null;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                if (line.startsWith("College:")) {
                    // New college entry
                    currentCollege = line.substring("College:".length()).trim();
                } else if (line.startsWith("Abbreviation:")) {
                    // College abbreviation
                    String abbr = line.substring("Abbreviation:".length()).trim();
                    if (!abbr.isEmpty() && currentCollege != null) {
                        currentAbbreviation = abbr;
                        addCollegeMapping(currentCollege, currentAbbreviation);
                    }
                } else if (line.startsWith("Program:")) {
                    // Program entry
                    String programLine = line.substring("Program:".length()).trim();
                    // Parse program name and abbreviation (Format: "Program Name: ABBR")
                    if (programLine.contains(":")) {
                        String[] parts = programLine.split(":");
                        if (parts.length >= 2) {
                            String programName = parts[0].trim();
                            String programAbbr = parts[1].trim();
                            if (!programName.isEmpty() && !programAbbr.isEmpty()) {
                                addProgramMapping(programName, programAbbr);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading college mappings from CSV: " + e.getMessage());
            // Fall back to hardcoded mappings
        }
    }
    
    private static void initializePrograms() {
        // Keep the fallback program mappings
        // Only programs not defined in the CSV file will use these
        
        // Engineering programs
        addProgramMapping("Bachelor of Science in Mechanical Engineering", "BSME");
        addProgramMapping("Bachelor of Science in Computer Science", "BSCS");
        addProgramMapping("Bachelor of Science in Computer Applications", "BSCA");
        addProgramMapping("Bachelor of Science in Magic", "BSM");
        
        // Add more fallback program mappings if needed
    }
    
    private static void addCollegeMapping(String collegeName, String abbreviation) {
        collegeToAbbreviation.put(collegeName, abbreviation);
        abbreviationToCollege.put(abbreviation, collegeName);
    }
    
    private static void addProgramMapping(String programName, String abbreviation) {
        programToAbbreviation.put(programName, abbreviation);
        abbreviationToProgram.put(abbreviation, programName);
    }
    
    public static String getCollegeAbbreviation(String collegeName) {
        return collegeToAbbreviation.getOrDefault(collegeName, collegeName);
    }
    
    public static String getProgramAbbreviation(String programName) {
        return programToAbbreviation.getOrDefault(programName, programName);
    }
    
    public static String getFullCollegeName(String abbreviation) {
        return abbreviationToCollege.getOrDefault(abbreviation, abbreviation);
    }
    
    public static String getFullProgramName(String abbreviation) {
        return abbreviationToProgram.getOrDefault(abbreviation, abbreviation);
    }
}