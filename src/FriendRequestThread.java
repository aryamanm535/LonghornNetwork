import java.util.concurrent.Semaphore;

/**
 * A thread that simulates sending a friend request between two students
 * in the Longhorn Network. Meant to be executed on multiple threads to
 * simulate concurrency.
 */
public class FriendRequestThread implements Runnable {

    private final UniversityStudent sender;
    private final UniversityStudent receiver;
    private static final Semaphore lock = new Semaphore(1);

    /**
     * Creates a new FriendRequestThread.
     *
     * @param sender   the student sending the friend request
     * @param receiver the student receiving the friend request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Executes the friend-request simulation. Must be thread safe
     */
    @Override
    public void run() {
        if (sender == null || receiver == null) {
            return;
        }
        try {
            lock.acquire();
            sender.addFriend(receiver);
            receiver.addFriend(sender);
            System.out.println("[FriendRequestThread] " + sender.name + " sent a friend request to " + receiver.name);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("FriendRequestThread interrupted: " + e.getMessage());

        } finally {
            lock.release();
        }
    }
}
