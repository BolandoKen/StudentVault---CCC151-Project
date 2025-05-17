import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

class StudentManager {
    private static final String FILE_PATH = "students.csv";
    
    // Save a student to CSV
    public static void saveStudent(Student student) {
        // Load existing students first
        List<Student> existingStudents = loadStudents();
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Write header
            pw.println("First Name,Last Name,Gender,ID Number,Year Level,College,Program");
            
            // Write the NEW student first
            pw.println(student.toCSV());
            
            // Write all existing students after the new one
            for (Student existingStudent : existingStudents) {
                pw.println(existingStudent.toCSV());
            }
            
            // Reload college data to ensure mappings are updated
            CollegeManager.loadColleges();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error saving student: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load students from CSV
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        // Check if file exists
        if (!file.exists()) {
            return students;  // Return empty list if file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;  // Skip header line
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                try {
                    Student student = Student.fromCSV(line);
                    students.add(student);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line);
                    System.err.println("Error message: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error loading students: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return students;
    }
    
    // Delete a student by ID
    public static boolean deleteStudent(String idNumber) {
        List<Student> students = loadStudents();
        boolean found = false;
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Write header
            pw.println("First Name,Last Name,Gender,ID Number,Year Level,College,Program");
            
            // Write all students except the one to delete
            for (Student student : students) {
                if (!student.getIdNumber().equals(idNumber)) {
                    pw.println(student.toCSV());
                } else {
                    found = true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error deleting student: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return found;
    }
    
    // Update a student
    public static boolean updateStudent(String idNumber, Student updatedStudent) {
        List<Student> students = loadStudents();
        boolean found = false;
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Write header
            pw.println("First Name,Last Name,Gender,ID Number,Year Level,College,Program");
            
            // Write all students, replacing the one to update
            for (Student student : students) {
                if (!student.getIdNumber().equals(idNumber)) {
                    pw.println(student.toCSV());
                } else {
                    pw.println(updatedStudent.toCSV());
                    found = true;
                }
            }
            
            // Reload college data after updating
            CollegeManager.loadColleges();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error updating student: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return found;
    }
    
    // Search for students by any field
    public static List<Student> searchStudents(String query) {
        List<Student> allStudents = loadStudents();
        List<Student> results = new ArrayList<>();
        query = query.toLowerCase().trim();
        
        for (Student student : allStudents) {
            if (student.getFirstName().toLowerCase().contains(query) ||
                student.getLastName().toLowerCase().contains(query) ||
                student.getIdNumber().toLowerCase().contains(query) ||
                student.getCollege().toLowerCase().contains(query) ||
                student.getProgram().toLowerCase().contains(query)) {
                results.add(student);
            }
        }
        return results;
    }
    
    // Get total number of students
    public static int getStudentCount() {
        return loadStudents().size();
    }
    
    // Check if a student ID already exists
    public static boolean isIdExists(String idNumber) {
        List<Student> students = loadStudents();
        return students.stream().anyMatch(s -> s.getIdNumber().equals(idNumber));
    }
}