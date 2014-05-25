package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;

/**
 *  A class that verifies the password if the client is a new user.
 *  
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class ListenToNewClient implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ServerGUI gui;
	private User user;
	private HashtableOH<String, User> table;
	private String loginInfo, username, password, id;
	private String[] temp;
	private ArrayList<Socket> list;

	/**
	 * A constructor that receives the current socket, current streams, a reference to the server GUI and a hashtable of users.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui The server GUI.
	 * @param table A hashtable that stores users.
	 */
	public ListenToNewClient( ArrayList<Socket> list, Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.table = table;
		this.list = list;
	}

	/**
	 * A function that verifies with a username and a password that the 
	 * user is authorised to be added to the server. After that the
	 * user selects a temp username and password for future login.
	 */
	public void run() {
		try {
			loginInfo = input.readUTF();
			splitInfo( loginInfo );
			if( MySQL.checkDatabase() ) {
				temp = MySQL.readTempMySQL();
				if( username.equals( temp[ 0 ] ) && password.equals( MD5.encryption( "alfa" ) ) ) {
					output.writeUTF( "temptrue" );
					output.flush();

					loginInfo = input.readUTF();
					splitInfo( loginInfo );

//					while( checkUsername( username ) ) { 	// Checks if username is occupied
//						output.writeUTF( "occupiedusername" );
//						output.flush();
//						loginInfo = input.readUTF();
//						splitInfo( loginInfo );
//					}
					
					id = getRandomID();
					while( table.containsKey( id ) ) {
						id = getRandomID();
					}
					output.writeUTF( id ); // Sends unique id to client
					output.flush();

					user = new User( id, username, password );
					table.put( id, user );

					MySQL.writeToMySQL( id, username, password );
					gui.showText( "Status: User " + username + " added to server\n" );
					MySQL.updateTempPWMySQL(); // Updates temp password to a new one

					Thread serverThread = new Thread( new ListenToClientPassword( list, socket, output, input, gui, table, id ) );
					serverThread.start();
				} else {
					output.writeUTF( "tempfalse" );
					output.flush();
					gui.showText( "Status: Wrong username or password\n" );
					gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + socket.getInetAddress().getHostAddress() + "\n" );
					socket.close();
					removeSocket();
				}
			} else {
				gui.showText( "Database unreachable - Unable to add users\n" + Time.getTime() + "\n" );
				output.writeUTF( "databasedown" );
				output.flush();
				socket.close();
				removeSocket();
			}
		} catch(IOException e) {
			try{
				gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
				removeSocket();
			} catch (IOException e1) {}
		} 
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
	
	/**
	 * A function that checks if a username is occupied.
	 * 
	 * @param username Client username.
	 * @return true or false.
	 */
	public boolean checkUsername( String username ) {
		Iterator<User> values = table.values();
	    while(values.hasNext()) {
	    	if( values.next().getName().toLowerCase().equals( username.toLowerCase() ) ) {
	    		return true;
	    	}
	    }
	    return false;
	}
}
