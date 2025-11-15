import java.util.*;


/**
 * Uses a graph of students to compute internship referral paths between
 * students in the Longhorn Network.
 */
public class ReferralPathFinder {
    /**
     * Creates a new {@code ReferralPathFinder} that operates on the provided
     * {@link StudentGraph}.
     *
     * @param graph the graph representing students and their connections
     */
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
    }

     /**
     * Finds a referral path from a starting student to any student who has
     * previously interned at the target company.
     * Will use Dijkstra's algorithm over the underlying {@code StudentGraph}.
     *
     * @param start he student from whom the search should begin
     * @param targetCompany name of the company that the caller is interested in
     * @return an ordered list of students representing a referral chain from
     *         {@code start} to a student who can refer them to {@code targetCompany}.
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        // Method signature only
        return new ArrayList<>();
    }
}
