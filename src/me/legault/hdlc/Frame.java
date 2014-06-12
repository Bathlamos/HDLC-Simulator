package me.legault.hdlc;

import java.util.BitSet;

import sun.misc.CRC16;

//@formatter:off
/**
 * The structure of a frame is
 *  -------------------------------------------------------------
 * |  Flag  | Address | Control | Information |   FCS   |  Flag  |
 * | 8 bits | 8+ bits | 8 bits  | Variable    | 16 bits | 8 bits |
 *  -------------------------------------------------------------
 */
//@formatter:on
public abstract class Frame {

	public static final CompactBitSet FLAG = CompactBitSet.fromBytes((byte) 0x7E); // 01111110

	private static final CRC16 CRC_VALIDATOR = new CRC16();

	protected byte control = 0x0;
	protected byte address = 0x0; // No address -> The primary station
	protected CompactBitSet information = new CompactBitSet();

	public byte getControl() {
		return control;
	}

	public byte getAddress() {
		return address;
	}

	public abstract void setControl(byte nr, byte pf, byte type);

	public abstract byte getCommandType();

	public byte[] toByteArray() {
		CompactBitSet crcComputable = new CompactBitSet();
		crcComputable.append(address, control);
		crcComputable.append(information);

		// Compute the CRC
		CompactBitSet result = new CompactBitSet();
		result.append(FLAG);
		result.append(crcComputable);
		result.append(computeCRC(crcComputable));
		result.append(FLAG);

		return result.toByteArray();
	}

	public String toString() {
		return CompactBitSet.fromBytes(toByteArray()).toString();
	}

	public static Frame fromByteArray(BitSet data) throws InvalidFrameException {
		// Find the start and end of the frame
		int start = findNextPatternOccurence(data, FLAG, 0);
		if (start == -1)
			throw new InvalidFrameException("Invalid start flag");
		
		int end = findNextPatternOccurence(data, FLAG, start + FLAG.getNumberOfBits() - 1);
		if (end == -1)
			throw new InvalidFrameException("Invalid end flag");
		end += FLAG.getNumberOfBits();

		data = data.get(start, end);

		Frame frame = null;
		byte[] header = data.get(8, 23).toByteArray();
		byte controlByte = header[1];
		if (SFrame.isFrameTypeValid(controlByte))
			frame = new SFrame((byte) 0);
		else
			throw new InvalidFrameException(
					"Frame type not recognize from the control byte: "
							+ controlByte);

		frame.address = header[0];
		frame.control = controlByte;
		frame.information = new CompactBitSet(end - start - 48);
		
		CompactBitSet crcComputable = new CompactBitSet();
		crcComputable.append(frame.address, frame.control);
		crcComputable.append(frame.information);

		short computedCRC = computeCRC(crcComputable);
		short dataCRC = CompactBitSet.fromBitSet(data, end - 24, end - 8).toShortArray()[0];
		if (computedCRC != dataCRC)
			throw new InvalidFrameException("Invalid CRC");

		return frame;
	}

	private static int findNextPatternOccurence(BitSet data, CompactBitSet pattern,
			int firstIndex) {
		int matched = 0;
		int length = data.size();
		int patternLength = pattern.getNumberOfBits();
		while (firstIndex < length)
			if (data.get(firstIndex + matched) == pattern.get(matched)) {
				matched++;
				if (matched == patternLength)
					return firstIndex;
			} else {
				matched = 0;
				firstIndex++;
			}
		return -1;
	}

	private static short computeCRC(CompactBitSet data) {
		CRC_VALIDATOR.reset();
		byte[] byteArray = data.toByteArray();
		for (int i = 0; i < byteArray.length; i++)
			CRC_VALIDATOR.update(byteArray[i]);
		return (short) CRC_VALIDATOR.value;
	}
}
