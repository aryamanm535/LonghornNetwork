/**
 * Runnable that simulates a chat message being sent between two students in
 * the Longhorn Network. Meant to be executed on multiple threads to simulate 
 * concurrency.
 */
public class ChatThread implements Runnable {
     /**
     * Creates a new {@code ChatThread} between a sender and a receiver.
     *
     * @param sender   the student initiating the chat message
     * @param receiver the student receiving the chat message
     * @param message  message contents
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        // Constructor
    }


    /**
     * Executes the chat simulation logic. Must be thread safe
     */
    @Override
    public void run() {
        // Method signature only
    }
}
