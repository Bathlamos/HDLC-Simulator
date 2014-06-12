package me.legault.hdlc;

//var t = "";
//$("#ll tr").each(function(tr){
//   if($(this).find("td")[0]){
//      t += "\n/**";
//      t += "\n * <b>" + $(this).find("td")[2].innerHTML + "</b><br />";
//      t += "\n * " + $(this).find("td")[0].innerText;
//      t += "\n*/";
//      t += "\npublic static final short " + $($(this).find("td")[0]).find("b")[0].innerHTML + "= 0x" + 
//      parseInt($(this).find("td")[5].innerText +
//      $(this).find("td")[6].innerText +
//      $(this).find("td")[7].innerText + "0" +
//      $(this).find("td")[8].innerText +
//      $(this).find("td")[9].innerText + "11", 2).toString(16).toUpperCase() +
//      ";\n";
//   }
//});
//console.log(t);

public class UFrame extends Frame {

	/**
	 * <b>Set mode</b><br />
	 * Set normal response SNRM
	*/
	public static final short SNRM= 0x83;

	/**
	 * <b>Set mode; extended</b><br />
	 * Set normal response extended mode SNRME
	*/
	public static final short SNRME= 0xCF;

	/**
	 * <b>Set mode</b><br />
	 * Set asynchronous response SARM
	*/
	public static final short SARM= 0xF;

	/**
	 * <b>Set mode; extended</b><br />
	 * Set asynchronous response extended mode SARME
	*/
	public static final short SARME= 0x4F;

	/**
	 * <b>Set mode</b><br />
	 * Set asynchronous balanced mode SABM
	*/
	public static final short SABM= 0x2F;

	/**
	 * <b>Set mode; extended</b><br />
	 * Set asynchronous balanced extended mode SABME
	*/
	public static final short SABME= 0x6F;

	/**
	 * <b>Initialize link control function in the addressed station</b><br />
	 * Set initialization mode SIM
	*/
	public static final short SIM= 0x0;

	/**
	 * <b>Terminate logical link connection</b><br />
	 * Disconnect DISC
	*/
	public static final short DISC= 0x43;

	/**
	 * <b>Acknowledge acceptance of one of the set-mode commands.</b><br />
	 * Unnumbered Acknowledgment UA
	*/
	public static final short UA= 0x3;

	/**
	 * <b>Responder in Disconnect Mode</b><br />
	 * Disconnect Mode DM
	*/
	public static final short DM= 0xF;

	/**
	 * <b>Solicitation for <b>DISC</b> Command</b><br />
	 * Request Disconnect RD
	*/
	public static final short RD= 0x43;

	/**
	 * <b>Initialization needed</b><br />
	 * Request Initialization Mode RIM
	*/
	public static final short RIM= 0x7;

	/**
	 * <b>Unacknowledged data</b><br />
	 * Unnumbered Information UI
	*/
	public static final short UI= 0x3;

	/**
	 * <b>Used to solicit control information</b><br />
	 * Unnumbered Poll UP
	*/
	public static final short UP= 0x1;

	/**
	 * <b>Used for recovery</b><br />
	 * Reset RSET
	*/
	public static final short RSET= 0x8F;

	/**
	 * <b>Used to Request/Report capabilities</b><br />
	 * Exchange Identification XID
	*/
	public static final short XID= 0x1;

	/**
	 * <b>Exchange identical information fields for testing</b><br />
	 * Test TEST
	*/
	public static final short TEST= 0x3;

	/**
	 * <b>Report receipt of unacceptable frame</b><br />
	 * Frame Reject FRMR
	*/
	public static final short FRMR= 0x0;

	/**
	 * <b>Not standardized</b><br />
	 * Nonreserved 0 NR0
	*/
	public static final short NR0= 0xB;

	/**
	 * <b>Not standardized</b><br />
	 * Nonreserved 1 NR1
	*/
	public static final short NR1= 0x8B;

	/**
	 * <b>Not standardized</b><br />
	 * Nonreserved 2 NR2
	*/
	public static final short NR2= 0x4B;

	/**
	 * <b>Not standardized</b><br />
	 * Nonreserved 3 NR3
	*/
	public static final short NR3= 0xCB;

	/**
	 * <b>Not part of HDLC</b><br />
	 * Configure for test CFGR
	*/
	public static final short CFGR= 0xC7;

	/**
	 * <b>Not part of HDLC</b><br />
	 * Beacon BCN
	*/
	public static final short BCN= 0xEF;
	
	public UFrame(byte type){
		setControl((byte)0, (byte)0, type);
	}

	@Override
	public void setControl(byte nr, byte pf, byte type) {
		// TODO Auto-generated method stub

	}

	@Override
	public byte getCommandType() {
		return (byte) (control & 0xEC);
	}
	
	public static boolean isFrameTypeValid(byte controlByte) {
		return (controlByte & 0x03) == 0x03;
	}

}
