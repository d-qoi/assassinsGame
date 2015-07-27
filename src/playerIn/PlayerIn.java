package playerIn;

import java.util.ArrayDeque;

/**
 * Specifies the methods that any player interaction should use.
 *
 * 
 * @author HogCat
 *			Created July 26, 2015
 */

public abstract class PlayerIn {
	public long messagesReceived;
	public long messagesSent;
	
	/**
	 * Returns the number of messages received. 
	 * 
	 * @return the number of messages received
	 */
	public long getMessagesReceive() {
		return this.messagesReceived;
	}
	
	/**
	 * Returns the number of messages sent
	 * 
	 * @return number of sent messages
	 */
	public long getMessagesSent() {
		return this.messagesSent;
	}
	
	/**
	 * Returns a queue that contains the messages that needs to be processed.
	 * 
	 * @return The messages that need to be processed.
	 */
	public abstract ArrayDeque<Message> recieveMessages();
	
	/**
	 * To save on initialization of new variables. This will add new messages to the existing queue.
	 * 
	 * @param previous
	 * @return the messages that need to be processed.
	 */
	public abstract ArrayDeque<Message> receiveMessages(ArrayDeque<Message> previous);
	
	/**
	 * Send these to the server
	 * @param toSend
	 */
	public abstract void sendMessages(ArrayDeque<Message> toSend);
	
	/**
	 * Send these to the server
	 * @param message
	 */
	public abstract void sendMessage(Message message);
	
	/**
	 * Start the threads
	 */
	public abstract void start();
	
	/**
	 * Stops the threads
	 */
	public abstract void stop();
}