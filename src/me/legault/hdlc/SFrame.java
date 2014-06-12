package me.legault.hdlc;

public class SFrame extends Frame {
	
	/**
	 * <b>Receive Ready</b><br />
	 * Indicate that the sender is ready to receive more data (cancels the
	 * effect of a previous RNR).
	 */
	public static final byte RR = 0x00;

	/**
	 * <b>Receive Not Ready</b><br />
	 * Acknowledge some packets and request no more be sent until further
	 * notice.
	 */
	public static final byte RNR = 0x04;

	/**
	 * <b>Reject</b><br />
	 * Requests immediate retransmission starting with N(R).
	 */
	public static final byte REJ = 0x04;

	/**
	 * <b>Selective Reject</b><br />
	 * Requests retransmission of only the frame N(R).
	 */
	public static final byte SREJ = 0x0C;
	
	public SFrame(byte type){
		setControl((byte)0, (byte)0, type);
	}

	//@formatter:off
	/**
	 * Control has format
	 *  ---------------------------------
	 * |  N(R)  |  P/F  |  Type  | 0 | 1 |
	 * | 3 bits | 1 bit | 2 bits | 0 | 1 |
	 *  ---------------------------------
	 */
	//@formatter:on
	public void setControl(byte nr, byte pf, byte type) {
		control = (byte) ((0xE0 & nr << 7) | (0x1 & pf << 4) | (0x0C & type) | 0x01);
	}
	
	public static boolean isFrameTypeValid(byte controlByte){
		return (controlByte & 0x03) == 0x01;
	}
	
	@Override
	public byte getCommandType() {
		return (byte) (control & 0x0C);
	}
}
