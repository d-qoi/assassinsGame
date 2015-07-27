package testing;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Scanner;

import playerIn.Message;
import playerIn.PlayerIn;

public class TestRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PlayerIn repeate = new FileInout(new File("src/testing/testFile.txt"), new File("src/testing/outfile.txt"));
		repeate.start();
		boolean stop = false;
		ArrayDeque<Message> messages = new ArrayDeque<>();
		Scanner sc = new Scanner(System.in);
		
		
		System.out.println("P: process, L:list, Q:quit, C:count, a:print out file");
		while(!stop) {
			System.out.print(">>");
			String line = sc.nextLine();
			if(line == "")
				line = " ";
			switch(line.toLowerCase().charAt(0)) {
				case 'q': stop = true; break;
				case 'l': messages = repeate.receiveMessages(messages);
					System.out.println(messages.toString());
					break;
				case 'p': messages = repeate.receiveMessages(messages);
					repeate.sendMessages(messages);
					messages.clear();
					break;
				case 'c':
					System.out.println(repeate.getMessagesReceive() + " " + repeate.getMessagesSent());
					break;
			}
		}
		sc.close();
		repeate.stop();
	}

}
