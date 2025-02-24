public class Student {
    private String firstName;
    private String lastName;
    private String gender;
    private String idNumber;
    private String yearLevel;
    private String college;
    private String program;

    // Constructor
    public Student(String firstName, String lastName, String gender, String idNumber, 
                  String yearLevel, String college, String program) {
        // Validate inputs
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (idNumber == null || idNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("ID number cannot be empty");
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.gender = gender;
        this.idNumber = idNumber.trim();
        this.yearLevel = yearLevel;
        this.college = college;
        this.program = program;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public String getIdNumber() { return idNumber; }
    public String getYearLevel() { return yearLevel; }
    public String getCollege() { return college; }
    public String getProgram() { return program; }

    // Setters with validation
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.lastName = lastName.trim();
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIdNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("ID number cannot be empty");
        }
        this.idNumber = idNumber.trim();
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    // CSV methods
    public String toCSV() {
        // Escape special characters and wrap fields in quotes to handle commas in fields
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
            escapeCSV(firstName),
            escapeCSV(lastName),
            escapeCSV(gender),
            escapeCSV(idNumber),
            escapeCSV(yearLevel),
            escapeCSV(college),
            escapeCSV(program));
    }

    private String escapeCSV(String field) {
        if (field == null) return "";
        return field.replace("\"", "\"\""); // Escape quotes with double quotes
    }

    // Create student from CSV line
    public static Student fromCSV(String csvLine) {
        // Basic CSV parsing (you might want to use a CSV library for more robust parsing)
        String[] fields = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (fields.length != 7) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        
        // Remove quotes and unescape
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].replaceAll("^\"|\"$", "").replace("\"\"", "\"");
        }

        return new Student(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
    }

    @Override
    public String toString() {
        return String.format("%s %s (ID: %s) - %s, %s, %s, %s",
            firstName, lastName, idNumber, gender, yearLevel, college, program);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return idNumber.equals(student.idNumber); // Using ID number as unique identifier
    }

    @Override
    public int hashCode() {
        return idNumber.hashCode();
    }
}