
/**
 * Responsible for forming pods of students based on their
 * connection strengths in the underlying StudentGraph.
 * Uses Prim's algorithm to grow pods around the strongest connections.
 */
public class PodFormation {

    private final StudentGraph graph;

    /**
     * Constructs a new PodFormation that will use the given
     * student graph when forming pods.
     *
     * @param graph graph that stores students and their connections
     */
    public PodFormation(StudentGraph graph) {
        this.graph = graph;
    }

    /**
     * Forms pods using a Prim-like algorithm that groups students
     * based on strongest connection strengths in the student graph.
     *
     * @param podSize desired number of students per pod
     */
    public void formPods(int podSize) {
    }
}
