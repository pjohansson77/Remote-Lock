package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A class that verifies the password if the client is trusted.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class ListenToClientPassword implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private HashtableOH<String, User> table;
	private ServerGUI gui;
	private String id;
	private ListenForClients server;
	
	/**
	 * The constructor receives the current socket, current streams, a reference to the server GUI and the user password.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui The server GUI.
	 * @param table A hashtable that stores users.
	 * @param id A user id.
	 */
	public ListenToClientPassword( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String id ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.table = table;
		this.id = id;
	}

	/**
	 * A function that verifies the password. If the password is not correct the user is disconnected.
	 */
	public void run() {
		try {
			String clientPassword = input.readUTF();

			if( clientPassword.equals( table.get( id ).getPassword() ) ) {
				output.writeUTF( "passwordtrue" );
				output.flush();
				gui.showText( "Status: User " + table.get( id ).getName() + " connected\n" );

				Thread clientThread = new Thread( new ArduinoChoices( socket, output, input, gui, table, id ) );
				clientThread.start();
			} else {
				output.writeUTF( "passwordfalse" );
				output.flush();
				gui.showText( "Status: Wrong username or password\n" );
				gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
			}
		} catch(IOException e) {} 
	}
}
