import java.util.*;

/**
 * Runs a roommate-matching process based on the Gale–Shapley algorithm.
 * Each student proposes to the individuals on their preference list, and stable
 * pairings are formed whenever possible.
 */
public class GaleShapley {

    /**
     * Matches students with compatible roommates. The method updates each
     * student's roommate field directly.
     *
     * @param students collection of UniversityStudent objects
     */
    public static void assignRoommates(List<UniversityStudent> students) {

        if (students == null || students.isEmpty()) 
                return;

        // clear any existing roommate links before starting
        for (UniversityStudent s : students) {
            s.setRoommate(null);
        }
        Map<UniversityStudent, UniversityStudent> matches = new HashMap<>();

        Map<UniversityStudent, Integer> proposalIndex = new HashMap<>();

        Map<String, UniversityStudent> lookup = new HashMap<>();

        for (UniversityStudent s : students) {
            lookup.put(s.name, s);
            proposalIndex.put(s, 0);
        }

        // Queue of students still seeking a roommate.
        Queue<UniversityStudent> free = new ArrayDeque<>();
        for (UniversityStudent s : students) {
            if (!s.roommatePreferences.isEmpty()) {
                free.offer(s);
            }
        }

        // --- Main matching loop ---
        while (!free.isEmpty()) {

            UniversityStudent proposer = free.poll();

            // If already matched by a previous step, move on.
            if (proposer.getRoommate() != null) {
                continue;
            }

            int idx = proposalIndex.get(proposer);

            // No one left to propose to.
            if (idx >= proposer.roommatePreferences.size()) {
                continue;
            }

            String preferredName = proposer.roommatePreferences.get(idx);
            proposalIndex.put(proposer, idx + 1);

            UniversityStudent target = lookup.get(preferredName);

            // If the preferred student isn't part of the dataset, skip ahead.
            if (target == null) {
                if (proposalIndex.get(proposer) < proposer.roommatePreferences.size()) {
                    free.offer(proposer);
                }
                continue;
            }

            // ff the target student does not reciprocate interest, reject.
            if (!target.roommatePreferences.contains(proposer.name)) {
                if (proposalIndex.get(proposer) < proposer.roommatePreferences.size()) {
                    free.offer(proposer);
                }
                continue;
            }

            UniversityStudent currPartner = target.getRoommate();

            // ff target has nobody yet, pair them immediately.
            if (currPartner == null) {
                matches.put(proposer, target);
                matches.put(target, proposer);
                proposer.setRoommate(target);
                target.setRoommate(proposer);
            } else {
                // determine which partner the target favors more.
                int currRank = target.roommatePreferences.indexOf(currPartner.name);
                int newRank = target.roommatePreferences.indexOf(proposer.name);

                boolean prefersNew = (newRank != -1) &&
                                     (currRank == -1 || newRank < currRank);

                if (prefersNew) {
                    // break existing partnership.
                    matches.remove(currPartner);
                    currPartner.setRoommate(null);
                    free.offer(currPartner);

                    // form new match
                    matches.put(target, proposer);
                    matches.put(proposer, target);
                    proposer.setRoommate(target);
                    target.setRoommate(proposer);

                } else {
                    // target keeps current partner; proposer tries next option.
                    if (proposalIndex.get(proposer) < proposer.roommatePreferences.size()) {
                        free.offer(proposer);
                    }
                }
            }
        }

        // output matched pairs for debugging
        System.out.println("\nRoommate Pairings (Gale–Shapley):");
        Set<UniversityStudent> shown = new HashSet<>();
        for (UniversityStudent s : matches.keySet()) {
            UniversityStudent p = matches.get(s);
            if (p != null && !shown.contains(s) && !shown.contains(p)) {
                System.out.println(s.name + " paired with " + p.name);
                shown.add(s);
                shown.add(p);
            }
        }
    }
}
