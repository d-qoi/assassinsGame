package server;

import java.io.File;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import playerIn.Message;
import playerIn.PlayerIn;
import testing.FileInout;

public class Server {

	private Map<String, String> username_to_cell;
	private Map<String, String> cell_to_username;
	private Set<String> registered;
	private Set<String> waiting;
	private Map<String, String> waitingRegConf;
	private Set<String> alive;
	private Set<String> dead;
	private Map<String, Integer> kills;
	private Map<String, String> waitingMsgConf;
	private Set<String> leaving;
	
	
	private Map<String, String> privilege;
	
	private PlayerIn playerCommunication;
	
	public Server() {
		username_to_cell = new HashMap<>();
		cell_to_username = new HashMap<>();
		registered = new HashSet<>();
		waiting = new HashSet<>();
		waitingRegConf = new HashMap<>();
		alive = new HashSet<>();
		dead = new HashSet<>();
		kills = new HashMap<>();
		waitingMsgConf = new HashMap<>();
		leaving = new HashSet<>();
		
		privilege = new HashMap<>();
		
		/**
		 * CHANGE THIS WHEN THE TESTING IS OVER
		 */
		File inFile = new File("src/testing/testFile.txt");
		File outFile = new File("src/testing/outfile.txt");
		outFile.delete();
		playerCommunication = new FileInout(inFile, outFile);
		/**
		 * CHANGE THIS WHEN THE TESTING IS OVER
		 */
		
		playerCommunication.start();
		
		Scanner sc = new Scanner(System.in);
		sc.nextLine();
		sc.close();
		playerCommunication.stop();
	}
	
	public String createCode(String id) {
		long number = new Long(id);
		long fullCode = System.currentTimeMillis() ^ number;
		System.out.printf("ID: %s, Code: %s", id, Long.toHexString(fullCode).substring(0, 5));
		return Long.toHexString(fullCode).substring(0, 5);
	}
	
	public void handleRegisterMessage(String id, String[] message) {
		if(waitingRegConf.containsKey(id)) {
			if(waitingRegConf.get(id).equals(message[0].trim())) {
				
			}
		}
	}
	
	public class handleMessages implements Runnable {
		public boolean stop = false;
		
		
		public handleMessages() {
			
		}
		
		public void terminate() {
			stop = true;
		}

		@Override
		public void run() {
			ArrayDeque<Message> messages = new ArrayDeque<>();
			Message message;
			String[] text;
			String id;
			while(!stop) {
				while(!messages.isEmpty()) {
					message = messages.poll();
					text = message.content.split(" ");
					if(registered.contains(message.id)) {
						
					} else {
						handleRegisterMessage(message.id, text);
					}
				}
				messages = playerCommunication.receiveMessages(messages);
			}
			
		}
		
	}
	
}
