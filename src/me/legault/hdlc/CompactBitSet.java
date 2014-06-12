package me.legault.hdlc;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class CompactBitSet extends BitSet {

	private static final long serialVersionUID = 6537103327202107868L;

	private int numberOfBits = 0;

	public CompactBitSet() {
		this(0);
	}

	public CompactBitSet(int numberOfBits) {
		this.numberOfBits = numberOfBits;
	}

	public int getNumberOfBits() {
		return numberOfBits;
	}

	public void setNumberOfBits(int numberOfBits) {
		this.numberOfBits = numberOfBits;
	}

	public void append(CompactBitSet bitset) {
		for (int i = 0; i < bitset.numberOfBits; i++)
			set(numberOfBits++, bitset.get(i));
	}

	public void append(short shortData) {
		append(ByteBuffer.allocate(2).putShort(shortData).array());
	}

	public void append(short... shortData) {
		for (int i = 0; i < shortData.length; i++)
			append(shortData[i]);
	}

	public void append(byte byteData) {
		for (int i = 0; i < 8; i++)
			set(numberOfBits++, (byteData >> i & 0x01) > 0);
	}

	public void append(byte... byteData) {
		for (int i = 0; i < byteData.length; i++)
			append(byteData[i]);
	}

	@Override
	public byte[] toByteArray() {
		byte[] byteArray = super.toByteArray();
		byte[] result = new byte[(numberOfBits + 7) / 8];
		for(int i = 0; i < result.length; i++)
			if(i < byteArray.length)
				result[i] = byteArray[i];
			else
				result[i] = 0;
		return result;
	}

	public short[] toShortArray() {
		byte[] bytes = toByteArray();
		short[] shorts = new short[(bytes.length + 1) / 2];
		ByteBuffer.wrap(bytes).asShortBuffer().get(shorts);
		return shorts;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		byte[] byteArray = super.toByteArray();
		for (int i = 0; i < byteArray.length; i++)
			sb.append(String.format("%02X ", byteArray[i]));
		return sb.toString();
	}

	public static CompactBitSet fromBytes(byte... byteData) {
		CompactBitSet bitSet = new CompactBitSet();
		bitSet.append(byteData);
		return bitSet;
	}

	public static CompactBitSet fromBitSet(BitSet bitSet, int startIndex,
			int endIndex) {
		CompactBitSet result = new CompactBitSet();
		for (int i = startIndex; i < endIndex; i++)
			result.set(result.numberOfBits++, bitSet.get(i));
		return result;
	}

}
