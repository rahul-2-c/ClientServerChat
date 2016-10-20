
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

	static Socket socket;
	String name;
	String path;
	String msgFromCLient;
	BufferedReader obj;

	Client(String name) {
		
		
		this.name = name;
		this.path = "E:/Chat/"+this.name;
		new File(this.path).mkdir();
	}

	public void run() {
    System.out.println("Yo");
		String thread_name = Thread.currentThread().getName();

		if (thread_name.contains("sender")) {
			try {

				while (true) {
					System.out.println("Type a message : ");
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
					PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);

					String msg = bufferedReader.readLine();

					if (msg.contains("exit")) {
						socket.close();
						System.out.println(name + " Disconnected ");
						System.exit(0);
					}
					pr.println(name + " :" + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (thread_name.contains("receiver")) {

			// Getting input from server aka client

			try {
				obj = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while ((msgFromCLient = obj.readLine()) != null) {
					System.out.println(msgFromCLient);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Hashmap code 
		}

	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		String name = args[0];
        String path = "E:/Chat/"+name;
		socket = new Socket("localhost", 54655);
		PrintWriter p = new PrintWriter(socket.getOutputStream(), true);
		p.println(name+"-"+path);

		Thread t1 = new Thread(new Client(name));
		Thread t2 = new Thread(new Client(name));
		t1.setName("sender");
		t2.setName("receiver");
		t1.start();
		t2.start();
		t1.join();
		t2.join();

	}
}
