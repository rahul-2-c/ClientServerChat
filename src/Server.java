import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server implements Runnable {

	static final int PORT = 54655;
	private static Map<String, Socket> clientMap = new HashMap<String, Socket>();
	private static Map<String, String> clientDir = new HashMap<String, String>();
	Socket client;
	String name_of_client, msgFromCLient;
	
	

	Server(Socket client) {
		this.client = client;
	}
	void sendMessage(String msg) throws IOException{
		
		 

		//	Socket sendSocket = clientMap.get("user1");

		//	PrintWriter p = new PrintWriter(sendSocket.getOutputStream(), true);
			//p.println("Message sent to User1 from " + name_of_client + " " + msg);
			if (msg.contains("broadcast")){
				
				Iterator<String> iterator = clientMap.keySet().iterator();
				while (iterator.hasNext()) {
					   String key = iterator.next().toString();
					  // Socket value = clientMap.get(key);
					   System.out.println("Hello ");
					   Socket sendSocket = clientMap.get(key);
					   PrintWriter p = new PrintWriter(sendSocket.getOutputStream(), true);
					   String a = msg.replace(name_of_client + " :" +"broadcast ", "");
					   System.out.println(a);
					   p.println("Message sent from " + name_of_client + " : " + a);
					   
			}
		
	}
	}
	public void run() {
		BufferedReader ob = null;
		
		String path=null;

		try {
			ob = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
			while ((name_of_client = ob.readLine()) != null) {
				String NamePath[] = name_of_client.split("-");
	            name_of_client = NamePath[0];
	             path = NamePath[1];
			    
				System.out.println(name_of_client+ " connected");
				break;
			}
			
			
			// Hash map code
			clientMap.put(name_of_client, this.client);
			clientDir.put(name_of_client, path);
			
		   Iterator<String> iterator = clientDir.keySet().iterator();

			while (iterator.hasNext()) {
			   String key = iterator.next().toString();
			   String value = clientDir.get(key);

			   System.out.println(key + " " + value);
			}

			try {
				while ((msgFromCLient = ob.readLine()) != null) {
					System.out.println(msgFromCLient);
				//	Send to the proc which decides the deatination and forwards it
					sendMessage(msgFromCLient);
					
				}
				System.out.println(name_of_client + " disconnected ");
				// client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		ServerSocket server;
		server = new ServerSocket(PORT);
		System.out.println(" Server socket created ");
		try {
			for (;;) {
				Socket client_socket = server.accept();
				new Thread(new Server(client_socket)).start();

			}
		} finally {
			server.close();
		}
	}
}
