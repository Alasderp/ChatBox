package ChatApp;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ChatApp.ClientThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
	
	private static ArrayList<InetAddress> clientIPs = new ArrayList<>();
	private	int port = 8181;
	private static int counter = 0;
	
	private static ArrayList<ArrayList<String>> chatQueue;
	private static ReadWriteLock chatLock = new ReentrantReadWriteLock();
	private static Lock readChatLock = chatLock.readLock();
	private static Lock writeChatLock = chatLock.readLock();

	public static void main(String[] args) {

		SpringApplication.run(Server.class, args);

		chatQueue = new ArrayList<ArrayList<String>>();
		
		try {
			
			int port = 8181;
			ServerSocket socket = new ServerSocket(port);
			System.out.println("Server Socket is operating on port: " + socket.getLocalPort());
			
			while (true) {
				
					Thread.sleep(100);
				
					Socket clientSocket = socket.accept();
					chatQueue.add(new ArrayList<String>());
					ClientThread player = new ClientThread(clientSocket, counter);
					counter++;
					player.start();
					System.out.println(clientSocket.getInetAddress() + " connected");
					clientIPs.add(clientSocket.getInetAddress());
			
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		

	}
	
	public static void addChat(String message) {
		
		try {
			writeChatLock.lock();
			for(int x = 0;x < chatQueue.size();x++) {
				chatQueue.get(x).add(message);
			}
		}
		finally {
			writeChatLock.unlock();
		}
		
	}
	
	public static String getChat(int playerNum) {
		try {
			writeChatLock.lock();
			String msg = chatQueue.get(playerNum).get(0);
			chatQueue.get(playerNum).remove(0);
			return msg;
		}
		finally {
			writeChatLock.unlock();
		}
	}
	
	public static boolean isChatEmpty(int playerNum) {
		
		try {
			readChatLock.lock();
			if(chatQueue.get(playerNum).size() == 0) {
				return true;
			}
			else {
				return false;
			}
		}
		finally {
			readChatLock.unlock();
		}
		
	}

}
