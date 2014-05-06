package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A class that sends the clients choices to the arduino and sends a confirmation back to the client.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class ArduinoChoices implements Runnable {
	private String message, id, clientpassword, newClientPassword, status;
	private int num, arduinoStatus;
	private boolean connected = true;
	private Socket clientSocket, arduinoSocket;
	private DataInputStream clientInput, arduinoInput;
	private DataOutputStream clientOutput, arduinoOutput;
	private ServerGUI gui;
	private HashtableOH<String, User> table;
	private ListenForClients server;

	/**
	 * A class constructor that gets the current socket, current streams and a reference to the server gui.
	 * 
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ServerGUI class.
	 * @param table A hashtable that stores users.
	 * @param id A user id.
	 */
	public ArduinoChoices( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String id, ListenForClients server ) {
		this.clientSocket = socket;
		this.clientInput = input;
		this.clientOutput = output;
		this.gui = gui;
		this.table = table;
		this.id = id;
		this.server = server;
	}

	/**
	 * A function that makes a connection to the arduino.
	 */
	public void run() {
		try {
//			arduinoSocket = new Socket( InetAddress.getByName( "169.254.245.225" ), 6666 );
//
//			arduinoOutput = new DataOutputStream( arduinoSocket.getOutputStream() );
//			arduinoInput = new DataInputStream( arduinoSocket.getInputStream() );
//
//			arduinoOutput.write( 8 ); // Message to Arduino
//			arduinoOutput.flush();

			arduinoStatus = 1; // Tas bort sen!
			statusToClient();
			listenToArduinoChoices();
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	/**
	 * A private function that sends the arduinostatus to the client.
	 */
	private void statusToClient() {
		try{
//			arduinoStatus = arduinoInput.read(); // Message from Arduino

			if( arduinoStatus == 1 ) {
				status = "unlocked";
			} else if( arduinoStatus == 2 ) {
				status = "locked";
			}else if( arduinoStatus == 3 ) {
				status = "open";
			}
			clientOutput.writeUTF( status );
			clientOutput.flush();
		} catch(IOException e) {}
	}

	/**
	 * A function that listens to the clients choices and sends it to the talkToArduino method.
	 */
	public void listenToArduinoChoices() {
		try{			
			while( connected ) {

				message = clientInput.readUTF();
				if( message.length() > 2 ) {
					splitInfo( message );
					if( message.equals( "changepassword" ) && table.get( id ).getPassword().equals( clientpassword ) ) {
						changePassword();
					} else {
						clientOutput.writeUTF( "wrongpassword" );
						clientOutput.flush();
					} 
				} else {
					num = Integer.parseInt( message );
					if( message.equals( "0" ) ) {
//						arduinoOutput.write( num ); // Message to Arduino
//						arduinoOutput.flush();
						connected = false;
						status = "disconnect";
					}
					if( message.equals( "1" ) ) {				
//						arduinoOutput.write( num ); // Message to Arduino
//						arduinoOutput.flush();
						arduinoStatus = 1; // Tas bort sen!
						statusToClient();
					} 
					if( message.equals( "2" ) ) {
//						arduinoOutput.write( num ); // Message to Arduino
//						arduinoOutput.flush();
						arduinoStatus = 2; // Tas bort sen!
						statusToClient();
					} 
					gui.showText( "Status: User " + table.get( id ).getName() + " sent " + status + "\n" );
				} 
			}
			gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + clientSocket.getInetAddress().getHostAddress() + "\n" );
			clientSocket.close();

//			arduinoSocket.close();
//			arduinoOutput.close();
//			arduinoInput.close();
		} catch(IOException e) {}
	}

	/**
	 * A private function that changes the user password.
	 */
	private void changePassword() {
		try{
			clientOutput.writeUTF( "sendnewpassword" );
			clientOutput.flush();

			newClientPassword = clientInput.readUTF();
			table.get( id ).setPassword( newClientPassword );

			MySQL.updateMySQL( newClientPassword, id );
			gui.showText( "Status: User " + table.get( id ).getName() + " changed password\n" );

			clientOutput.writeUTF( "Password changed" );
			clientOutput.flush();
		} catch(IOException e) {}
	}

	/**
	 * A private function that splits a string.
	 * 
	 * @param str The string that will be split.
	 */
	private void splitInfo( String str ) {
		String[] values;
		values = str.split( ";" );
		message = values[ 0 ];
		clientpassword = values[ 1 ];
	}
}