import java.util.*;

/**
 * Represents a weighted undirected graph of university students.
 * Each student is a node, and each edge weight represents the
 * connection strength between two students.
 */
public class StudentGraph {

    private final Map<UniversityStudent, List<Edge>> adjacencyList;

    /**
     * Constructor
     *
     * @param students list of students to include as nodes in the graph
     */
    public StudentGraph(List<UniversityStudent> students) {
        this.adjacencyList = new HashMap<>();
        if (students == null) {
            return;
        }

        // initialize each node with an empty neighbor list
        for (UniversityStudent s : students) {
            if (s != null && !adjacencyList.containsKey(s)) {
                adjacencyList.put(s, new ArrayList<>());
            }
        }

        // build edges between all distinct pairs using connection strength.
        for (int i = 0; i < students.size(); i++) {
            UniversityStudent a = students.get(i);
            if (a == null) 
                    continue;
            for (int j = i + 1; j < students.size(); j++) {
                UniversityStudent b = students.get(j);
                if (b == null) 
                        continue;
                int weight = a.calculateConnectionStrength(b);
                if (weight > 0) {
                    addEdge(a, b, weight);
                }
            }
        }
    }

    /**
     * Adds an undirected, weighted edge between two students in both directions
     *
     * @param a      first student
     * @param b      second student
     * @param weight 
     */
    public void addEdge(UniversityStudent a, UniversityStudent b, int weight) {
        if (a == null || b == null) 
            return;
        adjacencyList.get(a).add(new Edge(b, weight));
        adjacencyList.get(b).add(new Edge(a, weight));
    }

    /**
     * Returns the neighboring edges for the given student.
     *
     * @param student the student whose neighbors should be returned
     * @return list of edges connected to the given student
     */
    public List<Edge> getNeighbors(UniversityStudent student) {
        List<Edge> edges = adjacencyList.get(student);
        if (edges == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(edges);
    }

    /**
     * @return a set of all student nodes in the graph
     */
    public Set<UniversityStudent> getAllNodes() {
        return new HashSet<>(adjacencyList.keySet());
    }

    /**
     * helper method used by Main to verify the graph.
     */
    public void displayGraph() {
        System.out.println("\nStudent Graph:");

        for (UniversityStudent s : adjacencyList.keySet()) {
            System.out.println(s.name + " -> " + adjacencyList.get(s));
        }
    }


    /**
     * Represents an edge in the graph. Each edge stores a
     * reference to a neighboring student and the weight of
     * the connection.
     */
    public static class Edge {

        public final UniversityStudent neighbor;
        public final int weight;

        /**
         * Constructor
         *
         * @param neighbor the student this edge leads to
         * @param weight   the weight of the edge
         */
        public Edge(UniversityStudent neighbor, int weight) {
            this.neighbor = neighbor;
            this.weight = weight;
        }

        /**
         * @return the neighboring UniversityStudent
         */
        public UniversityStudent getNeighbor() {
            return neighbor;
        }

        /**
         * @return the edge weight
         */
        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "(" + neighbor.name + ", w=" + weight + ")";
        }

    }
}
