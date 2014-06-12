package me.legault.endpoint;

import java.util.ArrayDeque;
import java.util.Queue;

import me.legault.hdlc.Frame;

public class PrimaryStation extends Endpoint {

	private int portNumber;
	private Queue<Frame> queue = new ArrayDeque<Frame>();
	
	public PrimaryStation(int portNumber) {
		super("Primary Station");

		this.portNumber = portNumber;
	}

	@Override
	public void run() {
		openServerSocket(portNumber);
	}

	public void sendCommand(Frame frame) {
		queue.add(frame);
	}

	@Override
	void onConnectionEstablished() {
		while(true){
			if(!queue.isEmpty()){
				Frame frame = queue.poll();
				println("Sending frame " + frame);
				send(frame.toByteArray());
			}
		}
	}

}
