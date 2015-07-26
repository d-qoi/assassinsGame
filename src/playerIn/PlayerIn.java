package playerIn;
/**
 * Specifies the methods that any player interaction should use.
 *
 * 
 * @author HogCat
 *			Created July 26, 2015
 */

public abstract class PlayerIn {
	private long messagesProcessed;
	private long messagesSent;
	
	public long getMessagesProcessed() {
		return this.messagesProcessed;
	}
	
	public long getMessagesSent() {
		return this.messagesSent;
	}
}
