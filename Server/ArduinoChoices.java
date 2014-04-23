package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * A class that sends the clients choices to the arduino and sends a confirmation back to the client.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class ArduinoChoices {
	private String message;
	private int num;
	private boolean connected = true;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ServerGUI gui;

	/**
	 * A class constructor that gets the current socket and streams.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui The server GUI.
	 */
	public ArduinoChoices( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
	}

	/**
	 * A method that listens to the clients choices and sends it to the talkToArduino method.
	 */
	public void listenToArduinoChoices() {
		try{
			while( connected ) {

				message = input.readUTF();

				num = Integer.parseInt( message );
				talkToArduino( num );

				if( num == 0 ) {
					connected = false;
				} else {
					gui.showText( "IP-adress: " + socket.getInetAddress().getHostAddress() + " sent: " + message + "\n" );
					if( num == 1 ) {
						output.writeUTF( "Lampa1" );
					}
					else if( num == 2 ) {
						output.writeUTF( "Lampa2" );
					}
					else if( num == 4 ) {
						output.writeUTF( "Disco" );
					} 
					else {
						output.writeUTF("Fel val");
					}
					output.flush();
				} 
			} 
		} catch(IOException e) {}
	}
	
	/**
	 * A method that sends the clients choices to the arduino.
	 * 
	 * @param message The message from the client to the arduino.
	 */
	private void talkToArduino( int message ) {
		DataOutputStream output;
		Socket socket = null;

		// Avmarkera nedanstående javakod ifall Arduino inte är inkopplad
//		try {
//			socket = new Socket( InetAddress.getByName( "169.254.146.12" ), 6666 );
//			output = new DataOutputStream( socket.getOutputStream() );
//
//			output.write( message ); // Message to the Arduino
//			output.flush();
//		} catch(Exception e1 ) {
//			System.out.println( e1 );
//		}
//		try {
//			socket.close();
//		} catch( IOException e ) {
//			System.out.println( e );
//		}
	}
}
