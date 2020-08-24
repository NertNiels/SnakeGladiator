package nl.nertniels.snakegladiator.net;

public class Packets {
	
	public static final String PING = "00";
	public static final String PONG =  "01";
	public static final String CONNECT = "02";
	public static final String DISCONNECT = "03";
	public static final String SEND_CHAT = "04";
	public static final String READY = "05";
	public static final String START_ARENA = "06";
	public static final String UPDATE_ARENA = "07";
	public static final String STOP_ARENA = "08";
	
	/*
	 * PING:
	 * 		Only ping:		00
	 * PONG:
	 * 		Only pong:		01
	 * CONNECT:
	 * 		To server:		02username
	 * 		To client:		02id
	 * DISCONNECT:
	 * 		To server:		03id
	 * SEND_CHAT:
	 * 		To everyone:	04messageid
	 * READY:
	 * 		To server:		05id
	 * START_ARENA:
	 * 		To client:		06width*height*nosnakes*id1*color1*name1*x*y*...
	 * UPDATE_ARENA:
	 * 		To client:		07id*direction*grow*die*...
	 * 		To server:		07id
	 * STOP_ARENA
	 * 		To client:		08
	 */
	
}
