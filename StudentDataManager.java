import java.io.*;
import java.util.*;

public class StudentDataManager {
    private static final String CSV_FILE_PATH = "Students.csv";
    
    // Student fields to hold current student data
    private static String idNumber;
    private static String firstName;
    private static String lastName;
    private static String gender;
    private static String yearLevel;
    private static String programCode;
    
    // Getters and setters for student fields
    public static void setIdNumber(String value) { idNumber = value; }
    public static String getIdNumber() { return idNumber; }
    
    public static void setFirstName(String value) { firstName = value; }
    public static String getFirstName() { return firstName; }
    
    public static void setLastName(String value) { lastName = value; }
    public static String getLastName() { return lastName; }
    
    public static void setGender(String value) { gender = value; }
    public static String getGender() { return gender; }
    
    public static void setYearLevel(String value) { yearLevel = value; }
    public static String getYearLevel() { return yearLevel; }
    
    public static void setProgramCode(String value) { programCode = value; }
    public static String getProgramCode() { return programCode; }
    
    /**
     * Read all students from CSV file
     * @return List of string arrays containing student data
     */
    private static List<String[]> readCSV() {
        List<String[]> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split line by comma, but handle quoted values properly
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                // Remove quotes from fields if present
                for (int i = 0; i < values.length; i++) {
                    if (values[i].startsWith("\"") && values[i].endsWith("\"")) {
                        values[i] = values[i].substring(1, values[i].length() - 1);
                    }
                }
                
                students.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    /**
     * Write all students to CSV file
     * @param students List of student data arrays to write
     * @return true if successful, false otherwise
     */
    private static boolean writeCSV(List<String[]> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            bw.write("id_number,firstname,lastname,gender,year_level,program_code");
            bw.newLine();
            
            // Write data
            for (String[] student : students) {
                StringBuilder line = new StringBuilder();
                
                // Build CSV line with proper handling of commas
                for (int i = 0; i < student.length; i++) {
                    String value = student[i];
                    
                    // Add quotes if value contains commas
                    if (value.contains(",")) {
                        value = "\"" + value + "\"";
                    }
                    
                    line.append(value);
                    
                    // Add comma if not the last field
                    if (i < student.length - 1) {
                        line.append(",");
                    }
                }
                
                bw.write(line.toString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Save current student data to CSV
     * @return true if successful, false otherwise
     */
    public static boolean saveStudent() {
        if (!validateFields()) {
            return false;
        }
        
        List<String[]> students = readCSV();
        
        // Check if student ID already exists
        for (String[] student : students) {
            if (student[0].equals(idNumber)) {
                return false; // Student ID already exists
            }
        }
        
        // Add new student
        String[] newStudent = {
            idNumber, firstName, lastName, gender, yearLevel, programCode
        };
        students.add(newStudent);
        
        // Write back to CSV
        return writeCSV(students);
    }
    
    /**
     * Load student data by ID
     * @param id Student ID to search for
     * @return true if student found, false otherwise
     */
    public static boolean getStudentById(String id) {
        List<String[]> students = readCSV();
        
        for (String[] student : students) {
            if (student[0].equals(id)) {
                // Load the retrieved data into the static fields
                if (student.length >= 6) { // Ensure we have all required fields
                    idNumber = student[0];
                    firstName = student[1];
                    lastName = student[2];
                    gender = student[3];
                    yearLevel = student[4];
                    programCode = student[5];
                    return true; // Student found and data loaded
                }
            }
        }
        
        return false; // Student not found
    }
    
    /**
     * Add a new student with provided details
     * @param id Student ID
     * @param first First name
     * @param last Last name
     * @param gen Gender
     * @param year Year level
     * @param progCode Program code
     * @return true if successful, false otherwise
     */
    public static boolean addStudent(String id, String first, String last, String gen, 
                                   String year, String progCode) {
        // Validate input
        if (id == null || id.trim().isEmpty() || 
            first == null || first.trim().isEmpty() ||
            last == null || last.trim().isEmpty()) {
            return false; // Invalid input for required fields
        }

        // Trim all inputs to remove any leading/trailing whitespace
        id = id.trim();
        first = first.trim();
        last = last.trim();
        gen = gen != null ? gen.trim() : "";
        year = year != null ? year.trim() : "";
        progCode = progCode != null ? progCode.trim() : "";

        // Check if student ID already exists
        if (studentIdExists(id)) {
            return false; // Student ID already exists
        }
        
        // Set the static fields
        idNumber = id;
        firstName = first;
        lastName = last;
        gender = gen;
        yearLevel = year;
        programCode = progCode;
        
        // Save to CSV
        return saveStudent();
    }
    
    /**
     * Update an existing student's information
     * @param oldId Original student ID to update
     * @param newId New student ID
     * @param first First name
     * @param last Last name
     * @param gen Gender
     * @param year Year level
     * @param progCode Program code
     * @return true if successful, false otherwise
     */
    public static boolean updateStudent(String oldId, String newId, String first, String last, 
                                      String gen, String year, String progCode) {
        List<String[]> students = readCSV();
        boolean updated = false;
        
        // Check if new ID exists and is different from old ID
        if (!oldId.equals(newId) && studentIdExists(newId)) {
            return false; // New ID already exists
        }
        
        // Update student information
        for (int i = 0; i < students.size(); i++) {
            String[] student = students.get(i);
            if (student[0].equals(oldId)) {
                String[] updatedStudent = {
                    newId, first, last, gen, year, progCode
                };
                students.set(i, updatedStudent);
                updated = true;
                break;
            }
        }
        
        // Write back to CSV if updated
        if (updated) {
            return writeCSV(students);
        }
        
        return false;
    }
    
    /**
     * Delete a student by ID
     * @param id Student ID to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteStudent(String id) {
        List<String[]> students = readCSV();
        int initialSize = students.size();
        
        // Remove the student with the given ID
        students.removeIf(student -> student[0].equals(id));
        
        // Check if any student was removed
        if (students.size() < initialSize) {
            return writeCSV(students);
        }
        
        return false; // No student found with the given ID
    }
    
    /**
     * Load all students as Student objects
     * @return List of Student objects
     */
    public static List<Student> loadStudents() {
        List<Student> studentList = new ArrayList<>();
        List<String[]> studentsData = readCSV();
        
        for (String[] data : studentsData) {
            if (data.length >= 6) { // Ensure we have all required fields
                Student student = new Student(
                    data[0], data[1], data[2], data[3], data[4], data[5]
                );
                studentList.add(student);
            }
        }
        
        return studentList;
    }
    
    /**
     * Check if a student ID already exists
     * @param id Student ID to check
     * @return true if exists, false otherwise
     */
    public static boolean studentIdExists(String id) {
        List<String[]> students = readCSV();
        
        for (String[] student : students) {
            if (student[0].equals(id)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate that all required fields are present
     * @return true if valid, false otherwise
     */
    public static boolean validateFields() {
        return idNumber != null && !idNumber.isEmpty() &&
               firstName != null && !firstName.isEmpty() &&
               lastName != null && !lastName.isEmpty();
    }
    
    /**
     * Get all students for a specific program
     * @param progCode Program code to filter by
     * @return List of Student objects
     */
    public static List<Student> getStudentsByProgram(String progCode) {
        List<Student> filteredStudents = new ArrayList<>();
        List<String[]> studentsData = readCSV();
        
        for (String[] data : studentsData) {
            if (data.length >= 6 && data[5].equals(progCode)) {
                Student student = new Student(
                    data[0], data[1], data[2], data[3], data[4], data[5]
                );
                filteredStudents.add(student);
            }
        }
        
        return filteredStudents;
    }
    
    /**
     * Get all students for a specific year level
     * @param year Year level to filter by
     * @return List of Student objects
     */
    public static List<Student> getStudentsByYearLevel(String year) {
        List<Student> filteredStudents = new ArrayList<>();
        List<String[]> studentsData = readCSV();
        
        for (String[] data : studentsData) {
            if (data.length >= 6 && data[4].equals(year)) {
                Student student = new Student(
                    data[0], data[1], data[2], data[3], data[4], data[5]
                );
                filteredStudents.add(student);
            }
        }
        
        return filteredStudents;
    }
    
    /**
     * Get student count by program code
     * @return Map of program codes to student counts
     */
    public static Map<String, Integer> getStudentCountByProgram() {
        Map<String, Integer> counts = new HashMap<>();
        List<String[]> studentsData = readCSV();
        
        for (String[] data : studentsData) {
            if (data.length >= 6) {
                String progCode = data[5];
                counts.put(progCode, counts.getOrDefault(progCode, 0) + 1);
            }
        }
        
        return counts;
    }
}