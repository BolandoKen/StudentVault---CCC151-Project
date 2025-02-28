import java.util.HashMap;
import java.util.Map;

public class CollegeAbbreviationConverter {
    private static final Map<String, String> collegeToAbbreviation = new HashMap<>();
    private static final Map<String, String> programToAbbreviation = new HashMap<>();
    private static final Map<String, String> abbreviationToCollege = new HashMap<>();
    private static final Map<String, String> abbreviationToProgram = new HashMap<>();
    
    static {
        initializeColleges();
        
        initializePrograms();
    }
    
    private static void initializeColleges() {
        // Add College mappings
        addCollegeMapping("College of Engineering", "COE");
        addCollegeMapping("College of Science and Mathematics", "CSM");
        addCollegeMapping("College of Computer Studies", "CCS");
        addCollegeMapping("College of Education", "CED");
        addCollegeMapping("College of Arts and Social Sciences", "CAS");
        addCollegeMapping("College of Economics, Business & Accountancy", "CEBA");
        addCollegeMapping("College of Health Sciences", "CHS");
    }
    
    private static void initializePrograms() {
        // Engineering programs
        addProgramMapping("Diploma in Chemical Engineering Technology", "DCET");
        addProgramMapping("Bachelor of Science in Ceramic Engineering", "BSCerE");
        addProgramMapping("Bachelor of Science in Civil Engineering", "BSCE");
        addProgramMapping("Bachelor of Science in Electrical Engineering", "BSEE");
        addProgramMapping("Bachelor of Science in Mechanical Engineering", "BSME");
        addProgramMapping("Bachelor of Science in Chemical Engineering", "BSChE");
        addProgramMapping("Bachelor of Science in Metallurgical Engineering", "BSMetE");
        addProgramMapping("Bachelor of Science in Computer Engineering", "BSCpE");
        addProgramMapping("Bachelor of Science in Mining Engineering", "BSMinE");
        addProgramMapping("Bachelor of Science in Electronics & Communications Engineering", "BSECE");
        addProgramMapping("Bachelor of Science in Environmental Engineering", "BSEnET");
        
        // Science and Mathematics programs
        addProgramMapping("Bachelor of Science in Biology (Botany)", "BSBio-Bot");
        addProgramMapping("Bachelor of Science in Chemistry", "BSChem");
        addProgramMapping("Bachelor of Science in Mathematics", "BSMath");
        addProgramMapping("Bachelor of Science in Physics", "BSPhys");
        addProgramMapping("Bachelor of Science in Biology (Zoology)", "BSBio-Zoo");
        addProgramMapping("Bachelor of Science in Biology (Marine)", "BSBio-Mar");
        addProgramMapping("Bachelor of Science in Biology (General)", "BSBio-Gen");
        addProgramMapping("Bachelor of Science in Statistics", "BSStat");
        
        // Computer Studies programs
        addProgramMapping("Bachelor of Science in Computer Science", "BSCS");
        addProgramMapping("Bachelor of Science in Information Technology", "BSIT");
        addProgramMapping("Bachelor of Science in Information Systems", "BSIS");
        addProgramMapping("Bachelor of Science in Computer Application", "BSCA");
        
        // Education programs
        addProgramMapping("Bachelor of Elementary Education (Science and Mathematics)", "BEEd-SciMath");
        addProgramMapping("Bachelor of Elementary Education (Language Education)", "BEEd-Lang");
        addProgramMapping("Bachelor of Secondary Education (Biology)", "BSEd-Bio");
        addProgramMapping("Bachelor of Secondary Education (Chemistry)", "BSEd-Chem");
        addProgramMapping("Bachelor of Secondary Education (Physics)", "BSEd-Phys");
        addProgramMapping("Bachelor of Secondary Education (Mathematics)", "BSEd-Math");
        addProgramMapping("Bachelor of Physical Education", "BPEd");
        addProgramMapping("Bachelor of Technology and Livelihood Education (Home Economics)", "BTLED-HE");
        addProgramMapping("Bachelor of Technology and Livelihood Education (Industrial Arts)", "BTLed-IA");
        addProgramMapping("Bachelor of Technical-Vocational Teacher Education (Drafting Technology)", "BTVTED-DT");
        
        // Arts and Social Sciences programs
        addProgramMapping("Bachelor of Arts in English Language Studies", "BA-ELS");
        addProgramMapping("Bachelor of Arts in Literary and Cultural Studies", "BA-LCS");
        addProgramMapping("Bachelor of Arts in Filipino", "BA-FIL");
        addProgramMapping("Bachelor of Arts in Panitikan", "BA-PAN");
        addProgramMapping("Bachelor of Arts in Political Science", "BA-POLSCI");
        addProgramMapping("Bachelor of Arts in Psychology", "BA-PSY");
        addProgramMapping("Bachelor of Arts in Sociology", "BA-SOC");
        addProgramMapping("Bachelor of Arts in History (International History Track)", "BA-HIS-IH");
        addProgramMapping("Bachelor of Science in Philosophy", "BS-PHIL-AE");
        addProgramMapping("Bachelor of Science in Psychology", "BS-PSY");
        
        // Economics, Business & Accountancy programs
        addProgramMapping("Bachelor of Science in Accountancy", "BS-ACC");
        addProgramMapping("Bachelor of Science in Business Administration (Business Economics)", "BSBA-BE");
        addProgramMapping("Bachelor of Science in Business Administration (Marketing Management)", "BSBA-MM");
        addProgramMapping("Bachelor of Science in Entrepreneurship", "BS-ENT");
        addProgramMapping("Bachelor of Science in Hospitality Management", "BSHM");
        
        // Health Sciences programs
        addProgramMapping("Bachelor of Science in Nursing", "BSN");
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