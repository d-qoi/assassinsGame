package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import playerIn.Message;
import playerIn.PlayerIn;

public class Server {
	
	public File configFile = new File("config.conf");

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

	private Map<String, String> admins;

	private PlayerIn playerCommunication;
	private Text text;

	public boolean acceptingPlayers = true;

	public HandleMessages mainLoopRunnable;
	public Thread mainLoop;

	public Server(PlayerIn playerCommunication) {
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

		admins = new HashMap<>();

		this.playerCommunication = playerCommunication;

		mainLoopRunnable = new HandleMessages();
		mainLoop = new Thread(mainLoopRunnable);

		text = new Text();

	}

	public void startServer() {
		try {
			readInConfig();
		} catch (FileNotFoundException e) {
			System.out.println("Config not found");
			//e.printStackTrace();
		}
		mainLoopRunnable.stop = false;
		playerCommunication.start();
		mainLoop.start();
	}

	public void stopServer() {
		playerCommunication.stop();
		mainLoopRunnable.terminate();
	}
	
	public void readInConfig() throws FileNotFoundException {
		Scanner scan = new Scanner(configFile);
		while(scan.hasNext()) {
			String[] line = scan.nextLine().toLowerCase().split(",");
			if(line[0].contains("#"))
				continue;
			for(int i = 0; i<line.length; i++) {
				line[i] = line[i].trim();
			}
			if(line[0].equals("admin")) {
				if(line.length == 4) {
					this.registered.add(line[1]);
					this.username_to_cell.put(line[2], line[1]);
					this.cell_to_username.put(line[1], line[2]);
					this.admins.put(line[1], line[3]);
				} else {
					System.out.println("Admin line incorrect " + Arrays.toString(line));
				}
			} else if(line[0].equals("register")) {
				
			}
		}
		scan.close();
		
	}

	public String createCode() {
		String out = "";
		for (int i = 0; i < 6; i++) {
			out = out
					+ text.APLHANUMERIC.charAt((int) Math.random()
							* text.APLHANUMERIC.length());
		}
		return out;
	}

	public void handleRegisterMessage(String id, String[] message) {
		// System.out.println("Called"); //debug message
		// System.out.println(id + " " + Arrays.toString(message)); //debug
		// message
		if (waitingRegConf.containsKey(id)
				&& waitingRegConf.get(id).equals(message[0].trim())
				&& message.length == 2 && !cell_to_username.containsKey(id)
				&& !username_to_cell.containsKey(message[1])) {
			registered.add(id);
			username_to_cell.put(message[1].trim(), id);
			cell_to_username.put(id, message[1].trim());
			waitingRegConf.remove(id);
			String context = text.VERIFYTHANKS;
			Message toSend = new Message(id, context);
			playerCommunication.sendMessage(toSend);

		} else if (!waitingRegConf.containsKey(id)
				&& message[0].toLowerCase().equals("verify")
				&& !cell_to_username.containsKey(id)) {
			String code = createCode();
			waitingRegConf.put(id, code);
			String context = text.VERIFYMSG.replace("{{code}}", code);
			Message toSend = new Message(id, context);
			playerCommunication.sendMessage(toSend);
		}

	}

	public class HandleMessages implements Runnable {
		public boolean stop = false;

		public HandleMessages() {

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
			while (!stop) {
				while (!messages.isEmpty()) {

					message = messages.poll();
					System.out.println("Message " + message.toString());

					text = message.content.split(" ");
					if (registered.contains(message.id)) {

					} else if (acceptingPlayers) {
						handleRegisterMessage(message.id, text);
					}
				}
				messages = playerCommunication.receiveMessages(messages);
			}

		}

	}

}
