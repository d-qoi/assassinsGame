package server;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import playerIn.PlayerIn;
import testing.FileInout;

public class Server {

	private Map<String, String> username_to_cell;
	private Map<String, String> cell_to_username;
	private Set<String> registered;
	private Set<String> waiting;
	private Set<String> waitingRegConf;
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
		waitingRegConf = new HashSet<>();
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
	
}
