package me.legault.endpoint;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.BitSet;

public abstract class Endpoint extends Thread {
	
	private static final int BUFFER = 1024;

	private String id;
	protected Socket socket;

	Endpoint(String id) {
		this.id = id;
	}
	
	abstract void onConnectionEstablished();

	void openServerSocket(int portNumber) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			socket = serverSocket.accept();
			onConnectionEstablished();
		} catch (IOException e) {
			println("ServerSocket could not be open");
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			closeSocket();
		}
	}

	void openSocket(String hostname, int portNumber) {
		try {
			socket = new Socket(hostname, portNumber);
			onConnectionEstablished();
		} catch (IOException e) {
			println("Socket could not be open");
			e.printStackTrace();
		} finally {
			closeSocket();
		}
	}
	
	private void closeSocket(){
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	void listen(Function<BitSet> listener) {
		if (socket == null) {
			println("Cannot operate with a null socket");
			return;
		} else if (listener == null) {
			println("The listener needs to be set");
			return;
		}

		DataInputStream input;
		byte[] streamData = new byte[BUFFER];
		int bytesRead;
		try {
			input = new DataInputStream(socket.getInputStream());
			while ((bytesRead = input.read(streamData)) != -1) {
				BitSet bitset = BitSet.valueOf(streamData);
				bitset.clear(bytesRead * 8, streamData.length);
				
				if (listener.apply(bitset))
					break;

			}
		} catch (IOException e) {
			println("An exception has occured while reading from the socket");
			e.printStackTrace();
		}
	}

	void send(byte[] data) {
		if (socket == null) {
			println("Cannot operate with a null socket");
			return;
		}

		PrintStream output;
		try {
			output = new PrintStream(socket.getOutputStream());
			output.write(data);
		} catch (IOException e) {
			println("An exception has occured while sending to the socket");
			e.printStackTrace();
		}
	}

	void println(String message) {
		System.out.println("[" + id + "] " + message);
	}

}