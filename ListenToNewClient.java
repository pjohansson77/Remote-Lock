package lock;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *  A class that verifies the password if the client is a new user.
 *  
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class ListenToNewClient implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ServerGUI gui;
	private User user;
	private HashtableOH<String, User> table;
	private String userTextFile;

	/**
	 * A constructor that receives the current socket, current streams, a reference to the server GUI, a hashtable of users and the user textfile.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui The server GUI.
	 * @param table A hashtable that stores users.
	 * @param userTextFile The user textfile.
	 */
	public ListenToNewClient( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, HashtableOH<String, User> table, String userTextFile ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.table = table;
		this.userTextFile = userTextFile;
	}

	/**
	 * A function that verifies with a username and a password that the 
	 * user is authorised to be added to the server. After that the
	 * user selects an own name and password for future login.
	 */
	public void run() {
		try {
			String loginUsername = input.readUTF();
			String loginPassword = input.readUTF();
			String id = getRandomID();
			
			if( loginUsername.equals( "admin" ) && loginPassword.equals( "alfa" ) ) {
				output.writeUTF( id ); // Sends unique id to client
				output.flush();
				
				String clientUsername = input.readUTF();
				String clientPassword = input.readUTF();
	
				user = new User( id, clientUsername, clientPassword );
				table.put( id, user );

				writeUser( userTextFile );
				gui.showText( "Status: User " + clientUsername + " added to server\n" );
				
				Thread clientThread = new Thread( new ListenToClientPassword( socket, output, input, gui, clientPassword ) );
				clientThread.start();
			} else {
				output.writeUTF( "tempfalse" );
				output.flush();
				gui.showText( "Status: Wrong username or password\n" );
				try {
					gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
					socket.close();
				} catch( Exception e ) {}
			} 
		} catch(IOException e) {} 
	} 
	
	/**
	 * Function that writes users to a textfile.
	 * 
	 * @param filename Name of file that contains all users.
	 */
	private void writeUser( String filename ) {
		String str;
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter( filename, true ) );
			str = user.getID() + ";" + user.getName() + ";" + user.getPassword();
			
			writer.write( str );
			writer.newLine();

			writer.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
	
	/**
	 * A private method that creates a unique id.
	 * 
	 * @return unique id
	 */
	private String getRandomID() {
		Random rand = new Random();
		int nbr = rand.nextInt(100) + 1;
		String id = "DA211P1-" + nbr;
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
}
