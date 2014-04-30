package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *  A class that verifies the password if the client is a new user.
 *  
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class ListenToNewClient implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ServerGUI gui;
	private User user;
	private HashtableOH<String, User> table;
	private String loginInfo, username, password, id, tempUsername = "admin", tempPassword = "alfa";

	/**
	 * A constructor that receives the current socket, current streams, a reference to the server GUI, a hashtable of users and the user textfile.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui The server GUI.
	 * @param table A hashtable that stores users.
	 */
	public ListenToNewClient( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.table = table;
	}

	/**
	 * A function that verifies with a username and a password that the 
	 * user is authorised to be added to the server. After that the
	 * user selects a username and password for future login.
	 */
	public void run() {
		try {
			loginInfo = input.readUTF();
			splitInfo( loginInfo );

			if( username.equals( tempUsername ) && password.equals( tempPassword ) ) {
				output.writeUTF( "temptrue" );
				output.flush();

				loginInfo = input.readUTF();
				splitInfo( loginInfo );

				id = getRandomID();
				while( table.containsKey( id ) ) {
					id = getRandomID();
				}
				output.writeUTF( id ); // Sends unique id to client
				output.flush();

				user = new User( id, username, password );
				table.put( id, user );

//				MySQL.writeToMySQL( id, username, password );
				gui.showText( "Status: User " + username + " added to server\n" );

				Thread clientThread = new Thread( new ListenToClientPassword( socket, output, input, gui, table, id ) );
				clientThread.start();
			} else {
				output.writeUTF( "tempfalse" );
				output.flush();
				gui.showText( "Status: Wrong username or password\n" );
				try {
					gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
					socket.close();
					output.close();
					input.close();
				} catch( Exception e ) {}
			} 
		} catch(IOException e) {} 
	} 

	/**
	 * A private function that creates a unique id.
	 * 
	 * @return unique id
	 */
	private String getRandomID() {
		String id = "DA";
		char character;
		Random rand = new Random();
		for( int i = 0; i < 8; i++ ) {
			character = ( char )( rand.nextInt( 75 ) + '0' );
			id += character;
		}
		return id;
	}

	/**
	 * A private method that returns the date and time.
	 * 
	 * @return date and time
	 */
	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * A private function that splits a string.
	 * 
	 * @param str The string that will be split.
	 */
	private void splitInfo( String str ) {
		String[] values;
		values = str.split( ";" );
		username = values[ 0 ];
		password = values[ 1 ];
	}
}
