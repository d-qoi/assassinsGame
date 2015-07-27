package playerIn;

public class Message {
	public String id;
	public String content;
	
	public Message(String id, String content) {
		this.id = id;
		this.content = content;
	}
	
	public String toString() {
		return "id:"+this.id+", content:"+this.content;
	}
}
