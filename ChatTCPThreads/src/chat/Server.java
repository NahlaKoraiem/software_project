

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

	// Create static object from ServerSocket.
	static ServerSocket serverSocket;
        List<String>  array; 
	public static void main(String[] args) {
		try {
			// Initialize Server Socket with port number as parameter.
			serverSocket = new ServerSocket(4000);
			System.out.println("Server is booted up and is waiting for clients to connect.");

			while (true) {
				// Accept any Client wants to connect to the server.
				Socket clientSocket = serverSocket.accept();
				System.out.println("A new client [" + clientSocket + "] is connected to the server.");

				// Create a new thread for this client.
				Thread client = new ClientConnection(clientSocket);

				// Start thread.
				client.start();

			}
		} catch (IOException e) {
			System.out.println("Problem with Server Socket.");
		}
	}

	static class ClientConnection extends Thread {

		final private Socket clientSocket;
                final private List<String>  array; 

		public ClientConnection(Socket clientSocket, List<String> array) {
			this.clientSocket = clientSocket;
			this.array = new ArrayList<String>(array);
		}

		public void run() {
			try {

				// Takes input from the client socket.
				DataInputStream input = new DataInputStream(clientSocket.getInputStream());

				// Print output to the client socket.
				DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

				// Send to the client that it is now connected.
				output.writeUTF("connected.");

				// Start communication with client.
				while (true) {
					output.writeUTF("Send your request or 'close' to close the connection.");

					// Read the request from the client and output it to Server's console.
					String request = input.readUTF();
					System.out.println("Client [ " + clientSocket + " ]: " + request);

					// Close the connection with this client.
					if (request.equals("close")) {
						System.out.println("Closing connection with this client....");
						System.out.println("Connection with this client [" + clientSocket + "] is closed.");
						clientSocket.close();
						break;
					}
					
					// Take input from console.
					Scanner scanner = new Scanner(System.in);
					
					// Write the reply of the server and send it to the client.
					String reply = scanner.nextLine();
					output.writeUTF(reply);
					
				}

				// Close the objects.
				input.close();
				output.close();

			} catch (IOException e) {
				System.out.println("Connection with this client [" + clientSocket + "] is terminated.");
			}
		}
	}

}
