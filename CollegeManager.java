import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CollegeManager {
    private static final String COLLEGES_FILE = "colleges.csv";
    private static Map<String, List<String>> collegePrograms = new HashMap<>();
    private static Map<String, Map<String, Object>> collegeDetails = new HashMap<>();
// Add these at the class level (with other static maps)
    private static Map<String, String> collegeToAbbr = new HashMap<>(); // From CollegeDataManager
    private static Map<String, String> programToAbbr = new HashMap<>(); // From CollegeDataManager
    private static Map<String, String> abbrToCollege = new HashMap<>(); // From CollegeDataManager
    private static Map<String, String> abbrToProgram = new HashMap<>(); // From CollegeDataManager

// Add these methods to CollegeManager
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

public static List<String> getProgramsForCollegeList(String college) {
    return collegePrograms.getOrDefault(college, new ArrayList<>());
}

// Modify loadColleges() to populate all maps:
public static Map<String, Map<String, Object>> loadColleges() {
    // Clear all maps
    collegeDetails.clear();
    collegeToAbbr.clear();
    programToAbbr.clear();
    abbrToCollege.clear();
    abbrToProgram.clear();
    collegePrograms.clear();
    
    Path path = Paths.get(COLLEGES_FILE);
    
    if (!Files.exists(path)) {
        try {
            initializeDefaultColleges();
        } catch (IOException e) {
            System.err.println("Error creating college file: " + e.getMessage());
        }
    }
    
    try (BufferedReader reader = Files.newBufferedReader(path)) {
        String line;
        String currentCollege = null;
        String currentCollegeAbbr = "";
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            if (line.startsWith("College:")) {
                currentCollege = line.substring(8).trim();
                
                Map<String, Object> details = new HashMap<>();
                details.put("programs", new HashMap<String, String>());
                details.put("abbreviation", "");
                collegeDetails.put(currentCollege, details);
                collegePrograms.put(currentCollege, new ArrayList<>());
            } else if (line.startsWith("Abbreviation:") && currentCollege != null) {
                String abbr = line.substring("Abbreviation:".length()).trim();
                collegeDetails.get(currentCollege).put("abbreviation", abbr);
                collegeToAbbr.put(currentCollege, abbr);
                abbrToCollege.put(abbr, currentCollege);
            } else if (line.startsWith("Program:") && currentCollege != null) {
                String programInfo = line.substring("Program:".length()).trim();
                int separatorPos = programInfo.lastIndexOf(":");
                
                if (separatorPos > 0) {
                    String programName = programInfo.substring(0, separatorPos).trim();
                    String programAbbr = programInfo.substring(separatorPos + 1).trim();
                    
                    @SuppressWarnings("unchecked")
                    Map<String, String> programs = (Map<String, String>) collegeDetails.get(currentCollege).get("programs");
                    programs.put(programName, programAbbr);
                    programToAbbr.put(programName, programAbbr);
                    abbrToProgram.put(programAbbr, programName);
                    collegePrograms.get(currentCollege).add(programName);
                } else {
                    @SuppressWarnings("unchecked")
                    Map<String, String> programs = (Map<String, String>) collegeDetails.get(currentCollege).get("programs");
                    programs.put(programInfo, "");
                    collegePrograms.get(currentCollege).add(programInfo);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading college file: " + e.getMessage());
    }
    
    return collegeDetails;
}
    
    // Save colleges and programs to CSV file
    public static void saveColleges() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(COLLEGES_FILE))) {
            for (Map.Entry<String, Map<String, Object>> collegeEntry : collegeDetails.entrySet()) {
                String collegeName = collegeEntry.getKey();
                Map<String, Object> details = collegeEntry.getValue();
                
                writer.write("College: " + collegeName);
                writer.newLine();
                
                // Write college abbreviation
                writer.write("Abbreviation: " + details.get("abbreviation"));
                writer.newLine();
                
                // Write programs with their abbreviations
                @SuppressWarnings("unchecked")
                Map<String, String> programs = (Map<String, String>) details.get("programs");
                for (Map.Entry<String, String> programEntry : programs.entrySet()) {
                    writer.write("Program: " + programEntry.getKey() + ": " + programEntry.getValue());
                    writer.newLine();
                }
                
                writer.newLine(); // Empty line between colleges
            }
        } catch (IOException e) {
            System.err.println("Error writing college file: " + e.getMessage());
        }
    }
    
    
    // Add a new college
    public static void addCollege(String collegeName, String abbreviation) {
        if (!collegeDetails.containsKey(collegeName)) {
            Map<String, Object> details = new HashMap<>();
            details.put("programs", new HashMap<String, String>());
            details.put("abbreviation", abbreviation);
            collegeDetails.put(collegeName, details);
            saveColleges();
        }
    }
    
    // Add a program to an existing college
    public static boolean addProgram(String collegeName, String programName, String abbreviation) {
        if (collegeDetails.containsKey(collegeName)) {
            @SuppressWarnings("unchecked")
            Map<String, String> programs = (Map<String, String>) collegeDetails.get(collegeName).get("programs");
            if (!programs.containsKey(programName)) {
                programs.put(programName, abbreviation);
                saveColleges();
                return true;
            }
        }
        return false;
    }

    public static String getCollegeAbbreviation(String collegeName) {
        if (collegeDetails.containsKey(collegeName)) {
            return (String) collegeDetails.get(collegeName).get("abbreviation");
        }
        return "";
    }
    
    public static String getProgramAbbreviation(String collegeName, String programName) {
        if (collegeDetails.containsKey(collegeName)) {
            @SuppressWarnings("unchecked")
            Map<String, String> programs = (Map<String, String>) collegeDetails.get(collegeName).get("programs");
            return programs.getOrDefault(programName, "");
        }
        return "";
    }
    
    // In CollegeManager.java
public static boolean removeCollege(String collegeName) {
    if (collegeDetails.containsKey(collegeName)) {
        collegeDetails.remove(collegeName);
        saveColleges();
        
        // Update affected students
        List<Student> students = StudentManager.loadStudents();
        boolean studentsUpdated = false;
        
        for (Student student : students) {
            if (collegeName.equals(student.getCollege())) {
                // Set both college and program to null since the college is gone
                student.setCollege(null);
                student.setProgram(null);
                StudentManager.updateStudent(student.getIdNumber(), student);
                studentsUpdated = true;
            }
        }
        
        return true;
    }
    return false;
}

public static boolean removeProgram(String collegeName, String programName) {
    if (collegeDetails.containsKey(collegeName)) {
        @SuppressWarnings("unchecked")
        Map<String, String> programs = (Map<String, String>) collegeDetails.get(collegeName).get("programs");
        if (programs.remove(programName) != null) {
            saveColleges();
            
            // Update affected students
            List<Student> students = StudentManager.loadStudents();
            boolean studentsUpdated = false;
            
            for (Student student : students) {
                if (collegeName.equals(student.getCollege()) && programName.equals(student.getProgram())) {
                    // Set only program to null since the college still exists
                    student.setProgram(null);
                    StudentManager.updateStudent(student.getIdNumber(), student);
                    studentsUpdated = true;
                }
            }
            
            return true;
        }
    }
    return false;
}
    
    // Get college names as array
    public static String[] getCollegeNames() {
        return collegeDetails.keySet().toArray(new String[0]);
    }
    
    public static String[] getProgramsForCollege(String collegeName) {
        if (collegeDetails.containsKey(collegeName)) {
            @SuppressWarnings("unchecked")
            Map<String, String> programs = (Map<String, String>) collegeDetails.get(collegeName).get("programs");
            return programs.keySet().toArray(new String[0]);
        }
        return new String[0];
    }
    
    // Update initializeDefaultColleges to include abbreviations
    private static void initializeDefaultColleges() throws IOException {
        Map<String, Map<String, Object>> defaultColleges = new HashMap<>();
        
        // College of Computer Studies
        Map<String, Object> ccsDetails = new HashMap<>();
        ccsDetails.put("abbreviation", "CCS");
        Map<String, String> ccsPrograms = new HashMap<>();
        ccsPrograms.put("Bachelor of Science in Computer Science", "BSCS");
        ccsPrograms.put("Bachelor of Science in Information Technology", "BSIT");
        ccsPrograms.put("Bachelor of Science in Information Systems", "BSIS");
        ccsPrograms.put("Bachelor of Science in Computer Application", "BSCA");
        ccsDetails.put("programs", ccsPrograms);
        defaultColleges.put("College of Computer Studies", ccsDetails);
        
        // Add more colleges with abbreviations...
        
        collegeDetails = defaultColleges;
        saveColleges();
    }
    // Add these methods to the CollegeManager class
    public static boolean updateCollegeName(String oldCollegeName, String newCollegeName, String newAbbreviation) {
        if (collegeDetails.containsKey(oldCollegeName) && (oldCollegeName.equals(newCollegeName) || !collegeDetails.containsKey(newCollegeName))) {Map<String, Object> details = collegeDetails.get(oldCollegeName);
            collegeDetails.remove(oldCollegeName);
            
            // Update abbreviation if provided
            if (newAbbreviation != null && !newAbbreviation.isEmpty()) {
                details.put("abbreviation", newAbbreviation);
            }
            
            collegeDetails.put(newCollegeName, details);
            saveColleges();
            return true;
        }
        return false;
    }
    
    public static boolean updateProgramName(
        String collegeName, 
        String oldProgramName, 
        String newProgramName,
        String newAbbreviation
    ) {
        if (collegeDetails.containsKey(collegeName)) {
            @SuppressWarnings("unchecked")
            Map<String, String> programs = (Map<String, String>) collegeDetails.get(collegeName).get("programs");
            
            if (programs.containsKey(oldProgramName)) {
                // Check if new name already exists (unless it's the same as old name)
                if (!oldProgramName.equals(newProgramName) && programs.containsKey(newProgramName)) {
                    return false;
                }
                
                // Preserve old abbreviation if new one is empty
                String abbreviation = newAbbreviation.isEmpty() 
                    ? programs.get(oldProgramName)
                    : newAbbreviation;
                
                programs.remove(oldProgramName);
                programs.put(newProgramName, abbreviation);
                saveColleges();
                return true;
            }
        }
        return false;
    }
    public static boolean addProgram(String collegeName, String programName) {
        // Call the three-parameter version with an empty abbreviation
        return addProgram(collegeName, programName, "");
    }
    public static String getCollegeNameByAbbreviation(String abbreviation) {
        for (Map.Entry<String, Map<String, Object>> entry : collegeDetails.entrySet()) {
            String currentAbbr = (String) entry.getValue().get("abbreviation");
            if (abbreviation.equals(currentAbbr)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
}