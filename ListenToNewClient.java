package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;
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
	private String loginUsername, loginPassword, id, clientUsername, clientPassword;

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
			loginUsername = input.readUTF();
			loginPassword = input.readUTF();
			
			if( loginUsername.equals( "admin" ) && loginPassword.equals( "alfa" ) ) {
				output.writeUTF( "temptrue" );
				output.flush();
				
				clientUsername = input.readUTF();
				clientPassword = input.readUTF();
				
				id = getRandomID();
				output.writeUTF( id ); // Sends unique id to client
				output.flush();
	
				user = new User( id, clientUsername, clientPassword );
				table.put( id, user );

				writeMySQL();
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
	 * A private function that writes new users to a database.
	 * 
	 * @param id User id.
	 * @param clientUsername Username.
	 * @param clientPassword User password.
	 */
	private void writeMySQL() {
		try {
			Statement statement = MysqlDB.connect();

			String insert = "INSERT INTO ad1067.users VALUES ('" + id + "','" + clientUsername + "','" + clientPassword + "')";
			statement.executeUpdate( insert );
			
			MysqlDB.disconnect();
		} catch(SQLException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * A private function that creates a unique id.
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
