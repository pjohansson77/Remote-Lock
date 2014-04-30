package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class that sends the clients choices to the arduino and sends a confirmation back to the client.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class ArduinoChoices {
	private String message, id, clientpassword, newClientPassword;
	private int num, arduinoStatus;
	private boolean connected = true;
	private Socket arduinoSocket;
	private DataInputStream clientInput, arduinoInput;
	private DataOutputStream clientOutput, arduinoOutput;
	private ServerGUI gui;
	private HashtableOH<String, User> table;

	/**
	 * A class constructor that gets the current socket, current streams and a reference to the server gui.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ServerGUI class.
	 */
	public ArduinoChoices( DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String id ) {
		this.clientInput = input;
		this.clientOutput = output;
		this.gui = gui;
		this.table = table;
		this.id = id;
		connectToArduino();
	}

	private void connectToArduino() {

		// Avmarkera nedanstående javakod ifall Arduino inte är inkopplad
		try {
//			arduinoSocket = new Socket( InetAddress.getByName( "169.254.245.225" ), 6666 );
//
//			arduinoOutput = new DataOutputStream( arduinoSocket.getOutputStream() );
//			arduinoInput = new DataInputStream( arduinoSocket.getInputStream() );
			
//			arduinoOutput.write( 8 ); // Message to Arduino
//			arduinoOutput.flush();
			
			arduinoStatus = 1; // Tas bort sen!
			statusToClient();
			listenToArduinoChoices();
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	private void statusToClient() {
		try{
//			arduinoStatus = arduinoInput.read(); // Message from Arduino

			if( arduinoStatus == 1 ) {
				clientOutput.writeUTF( "unlocked" );
			} else if( arduinoStatus == 2 ) {
				clientOutput.writeUTF( "locked" );
			}else if( arduinoStatus == 3 ) {
				clientOutput.writeUTF( "open" );
			}

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

				if( message.equals( "0" ) ) {
					talkToArduino( message );
					connected = false;
				} else {
					if( message.equals( "1" ) ) {
						gui.showText( "Status: User " + table.get( id ).getName() + " sent: " + message + "\n" );
						talkToArduino( message );
						arduinoStatus = 1; // Tas bort sen!
						statusToClient();
					} else if( message.equals( "2" ) ) {
						gui.showText( "Status: User " + table.get( id ).getName() + " sent: " + message + "\n" );
						talkToArduino( message );
						arduinoStatus = 2; // Tas bort sen!
						statusToClient();
					} else if( message.length() > 2 ) {
						splitInfo( message );
						if( message.equals( "changepassword" ) && table.get( id ).getPassword().equals( clientpassword ) ) {
							clientOutput.writeUTF( "sendnewpassword" );
							clientOutput.flush();

							newClientPassword = clientInput.readUTF();
							table.get( id ).setPassword( newClientPassword );
							
//							writeMySQL();
							gui.showText( "Status: User " + table.get( id ).getName() + " changed password\n" );
							
							clientOutput.writeUTF( "Password changed" );
							clientOutput.flush();
						} else {
							clientOutput.writeUTF( "wrongpassword" );
							clientOutput.flush();
						}
					} 
				} 
			} 
			arduinoSocket.close();
			arduinoOutput.close();
			arduinoInput.close();
		} catch(IOException e) {}
	}

	private void writeMySQL() {
		try {
			Statement statement = MysqlDB.connect();

			String insert = "UPDATE ad1067.users SET password = '" + newClientPassword + "' WHERE id = '" + id + "'";
			statement.executeUpdate( insert );

			MysqlDB.disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}

	/**
	 * A function that sends the clients choices to the arduino.
	 * 
	 * @param message The message from the client to the arduino.
	 */
	private void talkToArduino( String message ) {
		num = Integer.parseInt( message );
		
		try {
//			arduinoOutput.write( num ); // Message to Arduino
//			arduinoOutput.flush();	
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	private void splitInfo( String txt ) {
		String[] values;
		values = txt.split( ";" );
		message = values[ 0 ];
		clientpassword = values[ 1 ];
	}
}
