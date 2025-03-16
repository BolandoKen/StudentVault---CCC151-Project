import java.io.*;
import java.util.*;

public class CollegeDataManager {
    private static final String CSV_PATH = "colleges.csv";
    private static Map<String, String> collegeToAbbr = new HashMap<>();
    private static Map<String, String> programToAbbr = new HashMap<>();
    private static Map<String, String> abbrToCollege = new HashMap<>();
    private static Map<String, String> abbrToProgram = new HashMap<>();
    private static Map<String, List<String>> collegePrograms = new HashMap<>();
    
    static {
        loadFromCSV();
    }
    
    public static void loadFromCSV() {
        collegeToAbbr.clear();
        programToAbbr.clear();
        abbrToCollege.clear();
        abbrToProgram.clear();
        collegePrograms.clear(); // Clear existing data
    
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            String currentCollege = null;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.startsWith("College:")) {
                    currentCollege = line.substring("College:".length()).trim();
                    collegePrograms.put(currentCollege, new ArrayList<>()); // Initialize college entry
                } 
                else if (line.startsWith("Abbreviation:")) {
                    if (currentCollege != null) {
                        String abbr = line.substring("Abbreviation:".length()).trim();
                        collegeToAbbr.put(currentCollege, abbr);
                        abbrToCollege.put(abbr, currentCollege);
                    }
                } 
                else if (line.startsWith("Program:")) {
                    if (currentCollege != null) {
                        String programLine = line.substring("Program:".length()).trim();
                        String[] parts = programLine.split(":", 2);
                        if (parts.length >= 2) {
                            String program = parts[0].trim();
                            String abbr = parts[1].trim();
                            programToAbbr.put(program, abbr);
                            abbrToProgram.put(abbr, program);
                            collegePrograms.get(currentCollege).add(program); // Add program to college
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading college data: " + e.getMessage());
        }
    }
    
    
    public static String getCollegeAbbr(String college) {
        return collegeToAbbr.getOrDefault(college, college);
    }
    
    public static String getProgramAbbr(String program) {
        return programToAbbr.getOrDefault(program, program);
    }
    
    public static String getCollegeName(String abbr) {
        return abbrToCollege.getOrDefault(abbr, abbr);
    }
    
    public static String getProgramName(String abbr) {
        return abbrToProgram.getOrDefault(abbr, abbr);
    }
    
    public static List<String> getAllColleges() {
        return new ArrayList<>(collegeToAbbr.keySet());
    }
    
    public static List<String> getProgramsForCollege(String college) {
        return collegePrograms.getOrDefault(college, new ArrayList<>());
    }
}