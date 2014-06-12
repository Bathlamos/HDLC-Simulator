package me.legault;

import me.legault.endpoint.PrimaryStation;
import me.legault.endpoint.SecondaryStation;
import me.legault.hdlc.SFrame;

public class EntryPoint {
	
	static final String HOSTNAME = "localhost";
	static final int PORT_NUMBER = 65234;
	
	public static void main(String[] args){
		
		PrimaryStation primaryStation = new PrimaryStation(PORT_NUMBER);
		primaryStation.start();
		
		SecondaryStation stationA = new SecondaryStation("A", HOSTNAME, PORT_NUMBER);
		stationA.start();
		
		primaryStation.sendCommand(new SFrame(SFrame.RR));
	}
}
