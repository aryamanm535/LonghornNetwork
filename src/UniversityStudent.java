import java.util.*;

/**
 * Student that represents a university student participating
 * in the Longhorn Network. A UniversityStudent can be matched with
 * roommates, placed into pods, and used in referral-path searches.
 */
public class UniversityStudent extends Student {

    private UniversityStudent roommate;

    private final Set<UniversityStudent> friends = Collections.synchronizedSet(new HashSet<>());

    private final Map<UniversityStudent, List<String>> chatHistory = Collections.synchronizedMap(new HashMap<>());

    /**
     * Constructor
     *
     * @param name              
     * @param age                 
     * @param gender         
     * @param year      
     * @param major       
     * @param gpa            
     * @param roommatePreferences
     * @param previousInternships 
     */
    public UniversityStudent(String name, int age, String gender, int year, String major, double gpa, List<String> roommatePreferences, List<String> previousInternships) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        this.roommatePreferences = roommatePreferences;
        this.previousInternships = previousInternships;
    }

    /**
     * Returns this student's current roommate, or nullif unpaired.
     *
     * @return the roommate or null
     */
    public UniversityStudent getRoommate() {
        return roommate;
    }

    /**
     * Sets this student's roommate.
     *
     * @param roommate the roommate to assign
     */
    public void setRoommate(UniversityStudent roommate) {
        this.roommate = roommate;
    }

    /**
     * Adds a friend to this student's friend
     *
     * @param other the student to add as a friend
     */
    public void addFriend(UniversityStudent other) {
        if (other == null) {
            return;
        }
        friends.add(other);
    }

    /**
     * Returns a snapshot of the current friend set.
     *
     * @return a new set containing all current friends
     */
    public Set<UniversityStudent> getFriends() {
        synchronized (friends) {
            return new HashSet<>(friends);
        }
    }

    /**
     * Adds a chat message to the conversation with the given partner
     *
     * @param partner the other texter
     * @param message the message text
     */
    public void addChatMessage(UniversityStudent partner, String message) {
        if (partner == null || message == null) {
            return;
        }
        synchronized (chatHistory) {
            List<String> messages = chatHistory.get(partner);
            if (messages == null) {
                messages = new ArrayList<>();
                chatHistory.put(partner, messages);
            }
            messages.add(message);
        }
    }

    /**
     * Returns all chat messages with the given partner
     *
     * @param partner the other participant
     * @return list of messages
     */
    public List<String> getChatHistoryWith(UniversityStudent partner) {
        if (partner == null) {
            return Collections.emptyList();
        }
        synchronized (chatHistory) {
            List<String> messages = chatHistory.get(partner);
            return messages == null ? Collections.emptyList() : new ArrayList<>(messages);
        }
    }

    /**
     * Computes the connection strength between this student and another student.
     *
     * @param other another student to compare against
     * @return a non-negative integer
     */
    @Override
    public int calculateConnectionStrength(Student other) {
        if (other == null || !(other instanceof UniversityStudent)) {
            return 0;
        }
        UniversityStudent o = (UniversityStudent) other;
        int strength = 0;

        // roommates
        if (this.roommate != null && this.roommate.equals(o)) {
            strength += 4;
        }

        // shared internships 
        Set<String> myInterns = new HashSet<>();
        for (String company : this.previousInternships) {
            if (company != null) {
                String trimmed = company.trim();
                if (!trimmed.isEmpty() && !trimmed.equalsIgnoreCase("none")) {
                    myInterns.add(trimmed);
                }
            }
        }
        for (String company : o.previousInternships) {
            if (company != null) {
                String trimmed = company.trim();
                if (!trimmed.isEmpty() && !trimmed.equalsIgnoreCase("none")
                        && myInterns.contains(trimmed)) {
                    strength += 3;
                }
            }
        }

        // same major
        if (this.major != null && this.major.equals(o.major)) {
            strength += 2;
        }

        // same age
        if (this.age == o.age) {
            strength += 1;
        }

        return strength;
    }

    @Override
    public String toString() {
        return "UniversityStudent{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", year=" + year +
                ", major='" + major + '\'' +
                ", gpa=" + gpa +
                ", roommate=" + (roommate == null ? "none" : roommate.name) +
                '}';
    }

}
