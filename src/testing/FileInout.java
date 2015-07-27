package testing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Date;
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
	private Thread tailer;
	private Thread writer;

	public FileInout(File messageFileIn, File messageFileOut) {
		this.fileIn = messageFileIn;
		this.fileOut = messageFileOut;
		messagesIn = new ArrayDeque<>();
		messagesOut = new ArrayDeque<>();
		date = new Date();

		TailerListener listener = new Tailerhandler();
		tailer = new Thread(new Tailer(fileIn, listener));
		tailer.setDaemon(true);

		try {
			writer = new Thread(new WriteFileOut());
		} catch (IOException e) {
			stop();
			e.printStackTrace();
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
		this.messagesSent += toSend.size();

	}

	private class WriteFileOut implements Runnable {
		private FileWriter file;
		private Message temp;

		public WriteFileOut() throws IOException {
			file = new FileWriter(fileOut, true);
		}

		@Override
		public void run() {
			while (true) {
				while (!messagesOut.isEmpty()) {
					temp = messagesOut.poll();
					if (temp != null)
						try {
							file.write(String.format("%S \t %S%N",
									date.getTime(), temp.toString()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private class Tailerhandler extends TailerListenerAdapter {
		public void handle(String line) {
			String[] message = line.split(":: ");
			assert (message.length == 2);
			messagesIn.offer(new Message(message[0], message[1]));
		}
	}

	@Override
	public void start() {
		this.writer.start();
		this.tailer.start();
	}

	@Override
	public void stop() {
		this.tailer.interrupt();
		this.writer.interrupt();
	}

}
