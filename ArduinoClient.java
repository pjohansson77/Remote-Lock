package test;

import java.net.*;
import java.io.*;
/**
 * A client class that let's the computer talk to the Arduino server.
 *  @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class ArduinoClient {
	private String serverIP;
	private int serverPort;
	private int message;

	/**
	 * The class constructor that set's the servers IP and port number from the Server class.
	 * @param serverIP is the IP number to the Arduino
	 * @param serverPort is the port number to the Arduino
	 */
	public ArduinoClient(String serverIP, int serverPort) { 
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}

	/**
	 * A method that print the clients message to the arduino.
	 * @param message The message from the client to the arduino.
	 */
	public void TalkToArduino (int message) {
		DataOutputStream output;
		Socket socket = null;

		try {
			socket = new Socket( InetAddress.getByName( serverIP ), serverPort );
			output = new DataOutputStream(socket.getOutputStream());

			output.write( message ); // Message to the Arduino
			
			output.flush();

		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
		try {
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}

	}
}