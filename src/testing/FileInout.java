package testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

import playerIn.Message;
import playerIn.PlayerIn;

public class FileInout extends PlayerIn {
	private File fileIn;
	private File fileOut;
	private ArrayDeque<Message> messagesIn;
	private ArrayDeque<Message> messagesOut;
	private Date date;
	private Tailer tailer;
	TailerListener listener;
	private WriteFileOut writeRun;
	private Thread writer;

	public FileInout(File messageFileIn, File messageFileOut) {
		this.fileIn = messageFileIn;
		this.fileOut = messageFileOut;
		messagesIn = new ArrayDeque<>();
		messagesOut = new ArrayDeque<>();
		date = new Date();

		listener = new Tailerhandler();

		try {
			this.writeRun = new WriteFileOut();
			this.writer = new Thread(writeRun);
		} catch (IOException e) {
			e.printStackTrace();
			stop();
			System.exit(1);
		}

	}

	@Override
	public ArrayDeque<Message> recieveMessages() {
		return messagesIn;
	}

	@Override
	public ArrayDeque<Message> receiveMessages(ArrayDeque<Message> previous) {
		previous.addAll(this.messagesIn);
		this.messagesIn.clear();
		return previous;
	}

	@Override
	public void sendMessages(ArrayDeque<Message> toSend) {
		// TODO Auto-generated method stub
		this.messagesOut.addAll(toSend);
		toSend.clear();
		

	}
	
	@Override
	public void sendMessage(Message message) {
		this.messagesOut.add(message);
		
	}

	public class WriteFileOut implements Runnable {
		private FileWriter file;
		private Message temp;
		private boolean running = true;

		public WriteFileOut() throws IOException {
			file = new FileWriter(fileOut, true);
			
		}
		
		public void terminate() {
			running = false;
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (running) {
				while (!messagesOut.isEmpty()) {
					temp = messagesOut.poll();
					if (temp != null) {
						try {
							file.write(String.format("%S    %S%n",
									date.toString(), temp.toString()));
							//System.out.printf("writing %S   %S\n",
							//		date.getTime(), temp.toString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally {
							//System.out.println("writing");
							messagesSent++;
							date = new Date();
						}
					}
				}
				try {
					Thread.sleep(5000);
					//System.out.println("LOOP");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
			}

		}

	}

	public class Tailerhandler extends TailerListenerAdapter {
		public void handle(String line) {
			String[] message = line.split(":: ");
			assert (message.length == 2);
			messagesIn.offer(new Message(message[0], message[1]));
			messagesReceived++;
			//System.out.println("got");
		}
	}

	@Override
	public void start() {
		this.writer.start();
		this.tailer = Tailer.create(fileIn, listener);
		System.out.println(tailer.getFile() + " " + writer.isAlive());
		
	}

	@Override
	public void stop() {
		this.tailer.stop();
		this.writer.interrupt();
		this.writeRun.terminate();
		System.out.println(this.writer.isAlive());
	}

	

}
