package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A class that sends the clients choices to the arduino and sends a confirmation back to the client.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class ArduinoChoices implements Runnable {
	private String message, id, clientMessage, status;
	private boolean connected = true;
	private Socket clientSocket;
	private DataInputStream clientInput;
	private DataOutputStream clientOutput;
	private ServerGUI gui;
	private HashtableOH<String, User> table;
	private DoorStatus doorStatus;
	private TalkToArduino talkToArduinoClient;
	private ArrayList<Socket> list;

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
	public ArduinoChoices( ArrayList<Socket> list, Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String id ) {
		talkToArduinoClient = new TalkToArduino();
		this.clientSocket = socket;
		this.clientInput = input;
		this.clientOutput = output;
		this.gui = gui;
		this.table = table;
		this.id = id;
		this.list = list;
	}

	/**
	 * A function that gets the current arduino status and then starts a new 
	 * thread that monitors the arduino status for changes and informs the client.
	 */
	public void run() {
		Thread Status = new Thread( doorStatus = new DoorStatus( clientSocket, clientOutput, talkToArduinoClient ) );
		Status.start();
		listenToArduinoChoices();
	}

	/**
	 * A function that listens to the clients choices.
	 */
	public void listenToArduinoChoices() {
		try{			
			while( connected ) {
				message = clientInput.readUTF();
				if( message.length() > 2 ) {
					splitInfo( message );
					if( message.equals( "changepassword" ) && table.get( id ).getPassword().equals( clientMessage ) ) {
						clientOutput.writeUTF( "sendnewpassword" );
						clientOutput.flush();
					} else if( message.equals( "newpassword" ) ) {
						setNewPassword();
					} else if( message.equals( "arduino" ) ) {
						talkToArduinoClient.setArduino( clientMessage );
						talkToArduinoClient.arduinoLock( "8" );
						talkToArduinoClient.statusToClient( clientOutput );
						gui.showText( "Status: User " + table.get( id ).getName() + " changed to lock " + clientMessage + "\n" );
					} else {
						clientOutput.writeUTF( "wrongpassword" );
						clientOutput.flush();
					} 
				} else {
					if( message.equals( "ok" ) ) {
						doorStatus.resetCounter();
					} else if( message.equals( "0" ) ) {
						connected = false;
						status = "disconnected";
						gui.showText( "Status: User " + table.get( id ).getName() + " " + status + "\n" );
					} else {
						if( !(talkToArduinoClient.getArduinoStatus() == 3) ) {
							talkToArduinoClient.arduinoLock( message );
							talkToArduinoClient.statusToClient( clientOutput );
							gui.showText( "Status: Door is " + talkToArduinoClient.getStatus() + " by " + table.get( id ).getName() + "\nTime: " + Time.getTime() + "\n" );
						} 
					}
				} 
			}
			gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + clientSocket.getInetAddress().getHostAddress() + "\n" );
			clientSocket.close();
			removeSocket();
		} catch(IOException e) {
			try {
				gui.showText( "Connection lost: " + Time.getTime() + "\nIP-address: " + clientSocket.getInetAddress().getHostAddress() + "\n" );
				clientSocket.close();
				removeSocket();
			} catch (IOException e1) {}
		}
	}

	/**
	 * A private function that sets a new user password.
	 */
	private void setNewPassword() {
		try{
			if( MySQL.checkDatabase() ) {
				table.get( id ).setPassword( clientMessage );
				MySQL.updateMySQLPassword( clientMessage, id );
				gui.showText( "Status: User " + table.get( id ).getName() + " changed password\n" );

				clientOutput.writeUTF( "passwordchanged" );
				clientOutput.flush();
			} else {
				gui.showText( "Database unreachable - Unable to change user password\n" + Time.getTime() + "\n" );
				clientOutput.writeUTF( "databasedown" );
				clientOutput.flush();
			}
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
		clientMessage = values[ 1 ];
	}
	
	/**
	 * A function that removes closed sockets from a list.
	 */
	public void removeSocket() {
		for( int i = 0; i < list.size(); i ++ ) {
			if( list.get( i ).isClosed() ) {
				list.remove( i );
			}
		}
	}
}