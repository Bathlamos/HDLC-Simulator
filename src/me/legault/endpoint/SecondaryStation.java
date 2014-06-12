package me.legault.endpoint;

import java.util.BitSet;

import me.legault.hdlc.Frame;

public class SecondaryStation extends Endpoint{

	private String hostname;
	private int portNumber;
	
	public SecondaryStation(String id, String hostname, int portNumber) {
		super("Secondary Station " + id);
		
		this.hostname = hostname;
		this.portNumber = portNumber;
	}
	
	@Override
	public void run() {
		openSocket(hostname, portNumber);
	}

	@Override
	void onConnectionEstablished() {
		listen(new Function<BitSet>(){

			public boolean apply(BitSet stream) {
				Frame frame = Frame.fromByteArray(stream);
				println("Receiving frame " + frame);
				System.out.println(frame.getCommandType());
				return false;
			}
			
		});
	}

}
