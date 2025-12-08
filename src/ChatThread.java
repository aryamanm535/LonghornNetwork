import java.util.concurrent.Semaphore;

/**
 * Runnable that simulates a chat message being sent between two students in
 * the Longhorn Network. Meant to be executed on multiple threads to simulate
 * concurrency.
 */
public class ChatThread implements Runnable {

    private final UniversityStudent sender;
    private final UniversityStudent receiver;
    private final String message;
    private static final Semaphore lock = new Semaphore(1);

    /**
     * Creates a new ChatThread between a sender and a receiver.
     *
     * @param sender   the student sending the chat message
     * @param receiver the student receiving the chat message
     * @param message  message contents
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Executes the chat simulation logic. Must be thread safe
     * The implementation logs the message to the console and records it in the
     * chat history for both the sender and the receiver.
     */
    @Override
    public void run() {
        try {
            lock.acquire();
            //I assumed that chatting creates a friendship
            sender.addFriend(receiver);
            receiver.addFriend(sender);
            String tagged = "[" + sender.getName() + "] " + message;
            sender.addChatMessage(receiver, tagged);
            receiver.addChatMessage(sender, tagged);
            System.out.println("[ChatThread] " + sender.name + " sent message to " + receiver.name + ": \"" + message + "\"");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("ChatThread interrupted: " + e.getMessage());
        } finally {
            lock.release();
        }
    }
}

