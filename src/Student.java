import java.util.*;


/**
 * Abstract base class for all students in the Longhorn Network.
 * It stores generic profile information that is shared by all
 * concrete student types.
 */
public abstract class Student {
    protected String name;
    protected int age;
    protected String gender;
    protected int year;
    protected String major;
    protected double gpa;
    protected List<String> roommatePreferences;
    protected List<String> previousInternships;

    /**
     * Computes a numeric connection strength between this student and
     * another student in the network. Implementations can choose different parameter
     *
     * @param other another student to compare against.
     * @return a non-negative integer where larger values indicate a stronger
     * connection.
     */
    public abstract int calculateConnectionStrength(Student other);
}
