/**
 * A thread that simulates sending a friend request between two students
 * in the Longhorn Network. Meant to be executed on multiple threads to simulate 
 * concurrency.
 */
public class FriendRequestThread implements Runnable {
    /**
     * Creates a new {@code FriendRequestThread}
     *
     * @param sender   the student initiating the friend request
     * @param receiver the student receiving the friend request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
    }

    /**
     * Executes the friend request simulation. Must be thread safe.
     */
    @Override
    public void run() {
        // Method signature only
    }
}
