package me.legault.hdlc;

public class IFrame extends Frame {

	
	public IFrame(CompactBitSet information){
		this.information = information;
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
	public void setControl(byte nr, byte pf, byte ns) {
		control = (byte) ((0xE0 & nr << 7) | (0x1 & pf << 4) | (0x0E & ns << 1));
	}
	
	public static boolean isFrameTypeValid(byte controlByte){
		return (controlByte & 0x01) == 0x0;
	}
	
	@Override
	public byte getCommandType() {
		return (byte) (control & 0x0C);
	}
}
