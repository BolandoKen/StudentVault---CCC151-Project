class Student {
    private String firstName, lastName, gender, idNumber, yearLevel, college, program;

    public Student(String firstName, String lastName, String gender, String idNumber, String yearLevel, String college, String program) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.idNumber = idNumber;
        this.yearLevel = yearLevel;
        this.college = college;
        this.program = program;
    }

    public String toCSV() {
        return firstName + "," + lastName + "," + gender + "," + idNumber + "," + yearLevel + "," + college + "," + program;
    }
}
