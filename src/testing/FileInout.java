package testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import playerIn.Message;
import playerIn.PlayerIn;


public class FileInout extends PlayerIn {
	private File fileIn;
	private File fileOut;
	private ArrayDeque<Message> messages;
	
	public FileInout(File messageFileIn, File messageFileOut) {
		this.fileIn = messageFileIn;
		this.fileOut = messageFileOut;
		messages = new ArrayDeque<>();
		
	}
	
	@Override
	public ArrayDeque<Message> recieveMessages() {
		return messages;
	}

	@Override
	public ArrayDeque<Message> receiveMessages(ArrayDeque<Message> previous) {
		previous.addAll(this.messages);
		this.messages.clear();
		return previous;
	}

	@Override
	public void sendMessages(ArrayDeque<Message> toSend) {
		// TODO Auto-generated method stub
		this.messagesSent += toSend.size();
		
	}
	
	private class ReadFileIn implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

		
		
	}
	
	private class WriteFileOut implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Tailerhandler extends TailerListenerAdapter {
		public void handle(String line) {
			String[] message = line.split(":: ");
			assert(message.length == 2);
			messages.offer(new Message(message[0], message[1]));
		}
	}
	
}
