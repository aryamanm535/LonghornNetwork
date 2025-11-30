import java.io.*;
import java.util.*;

/**
 * Utility class responsible for reading student data from input files and
 * constructing UniversityStudent objects for use in the Longhorn
 * Network.
 */
public class DataParser {

    /**
     * Parses the given file and creates a list of UniversityStudents.
     * 
     *
     * @param filename path to the input file
     * @return list of parsed students
     * @throws IOException if an I/O error occurs
     */
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {

        List<UniversityStudent> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line;

            // student fields
            String name = null;
            Integer age = null;
            String gender = null;
            Integer year = null;
            String major = null;
            Double gpa = null;
            List<String> roommatePrefs = new ArrayList<>();
            List<String> internships = new ArrayList<>();

            boolean hasAnyField = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Blank line = flush student block
                if (line.isEmpty()) {
                    if (hasAnyField) {
                        addStudentIfComplete(
                                students, name, age, gender, year, major, gpa,
                                roommatePrefs, internships
                        );
                    }

                    // Reset values for next student
                    name = null;
                    age = null;
                    gender = null;
                    year = null;
                    major = null;
                    gpa = null;
                    roommatePrefs = new ArrayList<>();
                    internships = new ArrayList<>();
                    hasAnyField = false;

                    continue;
                }

                hasAnyField = true;

                if (line.equalsIgnoreCase("Student:")) {
                    continue;
                }

                int colon = line.indexOf(':');
                if (colon < 0) 
                    continue; 

                String key = line.substring(0, colon).trim().toLowerCase();
                String value = line.substring(colon + 1).trim();

                switch (key) {
                    case "name":
                        name = value;
                        break;

                    case "age":
                        try { age = Integer.parseInt(value); } catch (Exception ignored) {}
                        break;

                    case "gender":
                        gender = value;
                        break;

                    case "year":
                        try { year = Integer.parseInt(value); } catch (Exception ignored) {}
                        break;

                    case "major":
                        major = value;
                        break;

                    case "gpa":
                        try { gpa = Double.parseDouble(value); } catch (Exception ignored) {}
                        break;

                    case "roommatepreferences":
                    case "roommate_prefs":
                    case "roommate_preferences":
                        roommatePrefs.clear();
                        if (!value.isEmpty()) {
                            for (String token : value.split(",")) {
                                String pref = token.trim();
                                if (!pref.isEmpty()) {
                                    roommatePrefs.add(pref);
                                }
                            }
                        }
                        break;

                    case "previousinternships":
                    case "internships":
                        internships.clear();
                        if (!value.isEmpty()) {
                            for (String token : value.split(",")) {
                                String company = token.trim();
                                if (!company.isEmpty()) {
                                    internships.add(company);
                                }
                            }
                        }
                        break;

                    default:
                        break;
                }
            }

            // Final flush at EOF
            if (hasAnyField) {
                addStudentIfComplete(
                        students, name, age, gender, year, major, gpa,
                        roommatePrefs, internships
                );
            }
        }

        return students;
    }


    /**
     * Helper method used to add a student to the list if at least
     * the name is present (other fields are filled with defaults).
     */
    private static void addStudentIfComplete(
            List<UniversityStudent> students,
            String name,
            Integer age,
            String gender,
            Integer year,
            String major,
            Double gpa,
            List<String> roommatePrefs,
            List<String> internships
    ) {
        if (name == null) return;

        int safeAge = (age == null ? 0 : age);
        int safeYear = (year == null ? 0 : year);
        double safeGpa = (gpa == null ? 0.0 : gpa);

        students.add(new UniversityStudent(
                name,
                safeAge,
                gender == null ? "" : gender,
                safeYear,
                major == null ? "" : major,
                safeGpa,
                new ArrayList<>(roommatePrefs),
                new ArrayList<>(internships)
        ));
    }
}
