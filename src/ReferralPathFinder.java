import java.util.*;

public class ReferralPathFinder {

    private StudentGraph graph;

    public ReferralPathFinder(StudentGraph g) {
        this.graph = g;
    }

    /**
     * Runs a Dijkstra search to locate the closest student (in terms of
     * strongest connection path) who has interned at the target company.
     *
     * A stronger edge weight = better connection, so we treat the “distance”
     * as the reciprocal of the weight.
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {

        // distance and prev tracking
        Map<UniversityStudent, Double> dist = new HashMap<>();
        Map<UniversityStudent, UniversityStudent> prev = new HashMap<>();

        // keep track of visited nodes
        Set<UniversityStudent> visited = new HashSet<>();

        // initialize everything as infinitely far
        for (UniversityStudent s : graph.getAllNodes()) {
            dist.put(s, Double.MAX_VALUE);
            prev.put(s, null);
        }
        dist.put(start, 0.0);

        PriorityQueue<UniversityStudent> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            UniversityStudent u = pq.poll();
            if (visited.contains(u)) continue;
            visited.add(u);

            // Check internship match
            for (String comp : u.previousInternships) {
                if (comp.equalsIgnoreCase(targetCompany)) {

                    List<UniversityStudent> path = new ArrayList<>();
                    UniversityStudent cur = u;

                    while (cur != null) {
                        path.add(cur);
                        cur = prev.get(cur);
                    }

                    Collections.reverse(path);
                    return path;
                }
            }

            // relax edges
            for (StudentGraph.Edge e : graph.getNeighbors(u)) {
                UniversityStudent v = e.neighbor;
                if (visited.contains(v)) {
                    continue;
                }

                double newDist = dist.get(u) + (1.0 / e.weight);

                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }

        // no valid referral path found
        return new ArrayList<>();
    }
}
