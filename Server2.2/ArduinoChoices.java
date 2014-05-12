package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A class that sends the clients choices to the arduino and sends a confirmation back to the client.
 * 
 * @author Peter Johansson, Jesper Hansen, Andree Höög
 */
public class ArduinoChoices implements Runnable {
	private String message, id, clientpassword, status;
	private int num = 0, arduinoStatus;
	private boolean connected = true;
	private Socket clientSocket, arduinoSocket;
	private DataInputStream clientInput, arduinoInput;
	private DataOutputStream clientOutput, arduinoOutput;
	private ServerGUI gui;
	private HashtableOH<String, User> table;
	private DoorStatus doorStatus;

	/**
	 * A class constructor that gets the current socket, current streams, a reference 
	 * to a hashtable, a reference to a user id and a reference to the ServerGUI class.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ServerGUI class.
	 * @param table A hashtable that stores users.
	 * @param id A user id.
	 */
	public ArduinoChoices( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String id ) {
		this.clientSocket = socket;
		this.clientInput = input;
		this.clientOutput = output;
		this.gui = gui;
		this.table = table;
		this.id = id;
	}

	/**
	 * A function that gets the current arduino status and then starts a new 
	 * thread that monitors the arduino status for changes and informs the client.
	 */
	public void run() {
		talkToArduino( "8" );
		Thread Status = new Thread( doorStatus = new DoorStatus( clientSocket, clientOutput, this ) );
		Status.start();
		statusToClient();
		listenToArduinoChoices();
	}

	/**
	 * A function that gets the current arduino status.
	 */
	public int getArduinoStatus() {
		return arduinoStatus;
	}

	/**
	 * A function that sets the current arduino status.
	 */
	public void setArduinoStatus( int arduinoStatus ) {
		this.arduinoStatus = arduinoStatus;
	}

	/**
	 * A private function that sends the arduinostatus to the client.
	 */
	private void statusToClient() {
		try{
			if( arduinoStatus == 1 ) {
				status = "unlocked";
			} else if( arduinoStatus == 2 ) {
				status = "locked";
			}else if( arduinoStatus == 3 ) {
				status = "open";
			} else if( arduinoStatus == 4 ){
				status = "not reachable";
			}
			clientOutput.writeUTF( status );
			clientOutput.flush();
		} catch(IOException e) {}
	}

	/**
	 * A function that listens to the clients choices.
	 */
	public void listenToArduinoChoices() {
		try{			
			while( connected ) {
				message = clientInput.readUTF();
				System.out.println(message);
				if( message.length() > 2 ) {
					splitInfo( message );
					if( message.equals( "changepassword" ) && table.get( id ).getPassword().equals( clientpassword ) ) {
						clientOutput.writeUTF( "sendnewpassword" );
						clientOutput.flush();
					} else if( message.equals( "newpassword" ) ) {
						setNewPassword();
					} else {
						clientOutput.writeUTF( "wrongpassword" );
						clientOutput.flush();
					} 
				} else {
					if( message.equals( "ok" ) ) {
						doorStatus.resetCounter();
					}else if( message.equals( "0" ) ) {
						connected = false;
						status = "disconnected";
						gui.showText( "Status: User " + table.get( id ).getName() + " " + status + "\n" );
					} else {
						talkToArduino( message );
						statusToClient();
						gui.showText( "Status: Door is " + status + " by " + table.get( id ).getName() + "\nTime: " + Time.getTime() + "\n" );
					} 
				} 
			}
			gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + clientSocket.getInetAddress().getHostAddress() + "\n" );
			clientSocket.close();
		} catch(IOException e) {
			try {
				gui.showText( "Connection lost: " + Time.getTime() + "\nIP-address: " + clientSocket.getInetAddress().getHostAddress() + "\n" );
				clientSocket.close();
			} catch (IOException e1) {}
		}
	}

	/**
	 * A private function that sets a new user password.
	 */
	private void setNewPassword() {
		try{
			table.get( id ).setPassword( clientpassword );
			MySQL.updateMySQLPassword( clientpassword, id );
			gui.showText( "Status: User " + table.get( id ).getName() + " changed password\n" );

			clientOutput.writeUTF( "passwordchanged" );
			clientOutput.flush();
		} catch(IOException e) {}
	}

	/**
	 * A private function that splits a string.
	 * 
	 * @param str A string that will be split.
	 */
	private void splitInfo( String str ) {
		String[] values;
		values = str.split( ";" );
		message = values[ 0 ];
		clientpassword = values[ 1 ];
	}

	/**
	 * A private function that communicates with the arduino.
	 * 
	 * @param message A string that contains a message for the arduino.
	 */
	private void talkToArduino( String message ) {
		num = Integer.parseInt( message );
		try{
			arduinoSocket = new Socket();
			arduinoSocket.connect(new InetSocketAddress( "10.228.0.123", 6666), 5000);

			arduinoOutput = new DataOutputStream( arduinoSocket.getOutputStream() );
			arduinoInput = new DataInputStream( arduinoSocket.getInputStream() );

			arduinoOutput.write( num ); // Message to Arduino
			arduinoOutput.flush();
			arduinoStatus = arduinoInput.read(); // Message from Arduino

			arduinoOutput.write( 0 );
			arduinoOutput.flush();
			arduinoSocket.close();
		} catch(IOException e) {
			try{
				arduinoStatus = 4;
				statusToClient();
				arduinoSocket.close();
			} catch(IOException e2) {}
		}
	}
}