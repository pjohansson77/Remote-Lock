package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A class that verifies the password if the client is trusted.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class ListenToClientPassword implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private HashtableOH<String, User> table;
	private ServerGUI gui;
	private String id;
	private ArrayList<Socket> list;

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
	public ListenToClientPassword( ArrayList<Socket> list, Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String id ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.table = table;
		this.id = id;
		this.list = list;
	}

	/**
	 * A function that verifies the password. If the password is not correct the user is disconnected.
	 */
	public void run() {
		try {
			String clientPassword = input.readUTF();

			while( !clientPassword.equals( table.get( id ).getPassword() ) ) {
				output.writeUTF( "passwordfalse" );
				output.flush();
				gui.showText( "Status: Wrong password from user " + table.get( id ).getName() + "\n" );
				clientPassword = input.readUTF();
			}
			output.writeUTF( "passwordtrue" );
			output.flush();
			gui.showText( "Status: User " + table.get( id ).getName() + " connected\n" );

			Thread serverThread = new Thread( new ArduinoChoices( list, socket, output, input, gui, table, id ) );
			serverThread.start();
		} catch(IOException e) {
			try{
				gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
				removeSocket();
			} catch (IOException e1) {}
		} 
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
