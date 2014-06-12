package ca.uottawa.hdlc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class PrimaryStation {

	public static void main(String[] args) throws IOException {

		//
		// sockets and other variables declaration
		//
		// maximum number of clients connected: 10
		//

		ServerSocket serverSocket = null;
		Socket[] client_sockets;
		client_sockets = new Socket[10];
		PrintWriter[] s_out;
		s_out = new PrintWriter[10];
		BufferedReader[] s_in;
		s_in = new BufferedReader[10];

		int[] ns; // send sequence number
		ns = new int[10];

		int[] nr; // receive sequence number
		nr = new int[10];

		String inputLine = null;
		String outputLine = null;

		//
		// get port number from the command line
		//
		int nPort = 4444; // default port number
		// nPort = Integer.parseInt(args[0]);

		String flag = "01111110";
		String[] address;
		address = new String[10];
		int[] clientID;
		clientID = new int[10];

		String control = null;
		String information = "";

		boolean bListening = true;

		String[] sMessages; // frame buffer
		sMessages = new String[20];
		int nMsg = 0;

		boolean bAlive = false;

		String response = null; // control field of the input
		//
		// initialize some var's for array handling
		//
		int s_count = 0;
		int i = 0;

		//
		// create server socket
		//
		try {
			serverSocket = new ServerSocket(nPort);

		} catch (IOException e) {
			System.err.println("Could not listen on port: " + args[0]);
			System.exit(-1);
		}

		//
		// this variable defines how many clients are connected
		//
		int nClient = 0;

		//
		// set timeout on the socket so the program does not
		// hang up
		//
		serverSocket.setSoTimeout(1000);

		//
		// main server loop
		//
		while (bListening) {

			try {
				//
				// trying to listen to the socket to accept
				// clients
				// if there is nobody to connect, exception will be
				// thrown - set by setSoTimeout()
				//
				client_sockets[s_count] = serverSocket.accept();

				//
				// connection got accepted
				//

				if (client_sockets[s_count] != null) {

					System.out.println("Connection from "
							+ client_sockets[s_count].getInetAddress()
							+ " accepted.");

					System.out.println("accepted client");
					s_out[s_count] = new PrintWriter(
							client_sockets[s_count].getOutputStream(), true);
					s_in[s_count] = new BufferedReader(new InputStreamReader(
							client_sockets[s_count].getInputStream()));

					clientID[s_count] = s_count + 1;

					address[s_count] = "00000000"
							+ Integer.toBinaryString(clientID[s_count]);
					int len = address[s_count].length();
					address[s_count] = address[s_count].substring(len - 8);

					System.out.println("client address: " + address[s_count]);

					// send client address to the new client
					s_out[s_count].println(address[s_count]);

					//
					// initialization
					//

					// ===========================================================
					// insert codes here to send SNRM message
					//

					s_out[s_count]
							.println(flag + address[s_count] + "11001001");

					System.out.println("Sent SNRM to station "
							+ clientID[s_count]);
					// ===============================================================

					// recv UA message
					inputLine = s_in[s_count].readLine();
					response = inputLine.substring(16, 24);

					if (response.equals("11000110")
							|| response.equals("11001110")) {
						System.out.println("Received UA from station "
								+ clientID[s_count]);
					} else {
						System.out.println("UA error -- station "
								+ clientID[s_count]);
					}

					// initialize ns and nr
					ns[s_count] = -1;
					nr[s_count] = 0;

					//
					// increment count of clients
					//
					s_count++;
					nClient = s_count;
					bAlive = true;
				}
			} catch (InterruptedIOException e) {
			}

			for (i = 0; i < s_count; i++) {

				// ==============================================================
				// insert codes here to send ìRR,*,Pîmsg

				s_out[i].println(flag + address[i] + "10001000"); // NS one bit
																	// and NO
																	// FCS and
																	// MGMT
																	// Data?

				System.out.println("Sent < RR,*,P > to station " + clientID[i]);
				// ==============================================================

				// recv response from the client
				inputLine = s_in[i].readLine();

				if (inputLine != null) {

					// get control field of the response frame
					response = inputLine.substring(16, 24);

					if (response.substring(0, 4).equals("1000")) {
						// recvìRR,*,Fî, no data to send from B
						System.out.println("Receive RR, *, F from station "
								+ clientID[i]);
					} else if (response.substring(0, 1).equals("0")) {
						// ==============================================================
						// insert codes here to handle the frame ìI, *, *î
						// received

						// if the frame is to the primary station; consume it
						if (inputLine.substring(8, 16).equals("00000000")) {
							System.out.println("Message Received from:"
									+ address[i]
									+ ":\n"
									+ inputLine.substring(24,
											inputLine.length()));
						} else {
							// if the frame is to the secondary station; buffer
							// the frame to send
							sMessages[i] = inputLine.substring(8, 16)
									+ inputLine.substring(24,
											inputLine.length()); // MESSAGE
																	// LENGTH?
																	// NO FCS?
																	// No
																	// address
																	// for
																	// Primary?
						}
						// ==============================================================
					}
				}
			}

			// ==============================================================
			// insert codes here to send frames in the buffer

			// send I frame
			for (i = 0; i < s_count; i++) {
				for (int j = 0; j < s_count; j++) {
					try {
						if (sMessages[i].substring(0, 8).equals(address[j])) {
							s_out[j].println(flag
									+ address[j]
									+ "00000000"
									+ sMessages[i].substring(8,
											sMessages[i].length()));
						}
					} catch (NullPointerException e) {
						continue;
					}
				}
			}
			// ==============================================================

			//
			// stop server automatically when
			// all clients disconnect
			//
			// no active clients
			//
			if (!bAlive && s_count > 0) {
				System.out.println("All clients are disconnected - stopping");
				bListening = false;
			}

		}// end of while loop

		//
		// close all sockets
		//

		for (i = 0; i < s_count; i++) {
			client_sockets[i].close();
		}

		serverSocket.close();

	}// end main
}// end of class PrimaryStation
