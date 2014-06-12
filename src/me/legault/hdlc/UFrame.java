package me.legault.hdlc;

public class UFrame extends Frame{
	
	/**
	 * <b>Receive Ready</b>
	 * Indicate that the sender is ready to receive more data (cancels the effect of a previous RNR).
	 */
	public static final short RR = 0x00;
	
	/**
	 * <b>Receive Not Ready</b>
	 * Acknowledge some packets and request no more be sent until further notice.
	 */
	public static final short RNR = 0x04;
	
	/**
	 * <b>Reject</b>
	 * Requests immediate retransmission starting with N(R).
	 */
	public static final short REJ = 0x04;
	
	/**
	 * <b>Selective Reject</b>
	 * Requests retransmission of only the frame N(R).
	 */
	public static final short SREJ = 0x0C;

	@Override
	public void setControl(byte nr, byte pf, byte type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte getCommandType() {
		// TODO Auto-generated method stub
		return 0;
	}


}
