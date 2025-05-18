/**
 * Simplified Student class to represent a student record with only core fields
 */
public class Student {
    private String idNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String yearLevel;
    private String programCode;
    
    // Constructor with only 6 parameters
    public Student(String idNumber, String firstName, String lastName, 
                  String gender, String yearLevel, String programCode) {
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.yearLevel = yearLevel;
        this.programCode = programCode;
    }
    
    // Getters
    public String getIdNumber() {
        return idNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getGender() {
        return gender;
    }
    
    public String getYearLevel() {
        return yearLevel;
    }
    
    public String getProgramCode() {
        return programCode;
    }
    
    // Setters
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }
    
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }
    
    @Override
    public String toString() {
        return idNumber + " - " + firstName + " " + lastName;
    }
}