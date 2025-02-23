import java.io.*;
import java.util.*;

class StudentManager {
    private static final String FILE_PATH = "students.csv";

    // Save a student to CSV
    public static void saveStudent(Student student) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            pw.println(student.toCSV());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load students from CSV
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    students.add(new Student(data[0], data[1], data[2], data[3], data[4], data[5], data[6]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }
}
