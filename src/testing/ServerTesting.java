package testing;

import java.io.File;
import java.util.Scanner;

import server.Server;

public class ServerTesting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File inFile = new File("src/testing/testFile.txt");
		File outFile = new File("src/testing/outfile.txt");
		outFile.delete();
		FileInout playerCommunication = new FileInout(inFile, outFile);
		Server server = new Server(playerCommunication);
		
		server.startServer();
		System.out.println("STARTED");
		Scanner sc = new Scanner(System.in);
		System.out.println("Hit enter to close");
		System.out.println(sc.next());
		System.out.println("CLOSING");
		sc.close();
		server.stopServer();

	}

}
