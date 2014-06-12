package ca.uottawa.hdlc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SecondaryStation {

	public static void main(String[] args) {
		// declaration section:
		// os: output stream
		// is: input stream
		Socket clientSocket = null;
		PrintStream os = null;
		DataInputStream is = null;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String id = null;
		String information = null;
		String control = null;
		String flag = "01111110";
		String address = null;
		String response = null; // control field of the socket input
		int ns = -1; // send sequence number
		int nr = 0; // receive sequence number

		//
		String answer = null; // input using keyboard
		//

		//
		// Initialization section:
		// Try to open a socket on port 4444
		// Try to open input and output streams
		try {
			clientSocket = new Socket("127.0.0.1", 4444);
			os = new PrintStream(clientSocket.getOutputStream());
			is = new DataInputStream(clientSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: hostname");
		}

		if (clientSocket != null && os != null && is != null) {

			try {

				String responseLine;
				responseLine = is.readLine();

				// receive client address from the primary station
				//
				id = responseLine;
				System.out.println("client address: " + id);

				responseLine = is.readLine();
				response = responseLine.substring(16, 24);

				// recv SNRM msg
				if (response.equals("11000001") || response.equals("11001001")) {
					// ===========================================================
					// insert codes here to send the UA msg
					// ===========================================================
					os.println(flag + "00000000" + "11000110");
					System.out.println("sent UA msg");
				}

				// main loop; recv and send data msgs
				while (true) {
					responseLine = is.readLine();
					response = responseLine.substring(16, 24);

					System.out.println("recvmsg -- control " + response);

					// recv ??RR,*,P?? msg
					if (response.substring(0, 5).equals("10001")) {

						// enter data msg using keyboard
						System.out
								.println("Is there any message to send? (y/n)");
						answer = in.readLine();

						if (answer.toLowerCase().equals("y")
								|| answer.toLowerCase().equals("yes")) {
							System.out
									.println("Please enter the destination address using 8-bits binary string (e.g. 00000001):");
							address = in.readLine();

							System.out
									.println("Please enter the message to send?");
							answer = in.readLine();
							// ===========================================================
							// insert codes here to send an I msg;
							os.println(flag + address + "01110000" + answer);
							// ===========================================================

						} else {
							// ===========================================================
							// insert codes here to send ??RR,*,F??
							os.println(flag + "00000000" + "10000000");
							// ===========================================================
						}
					}

					// recv an I frame
					if (response.substring(0, 1).equals("0")) {
						String data = responseLine.substring(24,
								responseLine.length());
						System.out.println("");
						System.out.println("Received data: " + data);

						nr = Integer.parseInt(response.substring(1, 4), 2) + 1;
						System.out.println("nr: " + nr);
					}
				}
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException: " + e);
			}
		}
	}

}// end of class SecondaryStation
