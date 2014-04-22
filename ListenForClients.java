package lock;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A server that listens for clients and verifies a known client with an unique id.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class ListenForClients implements Runnable {
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private String id;
	private DataOutputStream output;
	private DataInputStream input;
	private ServerGUI gui;
	private HashtableOH<String, User> table;
	private User user;

	/**
	 * A constructor that gets a port number to listen on.
	 * @param port The port that the server listens on.
	 */
	public ListenForClients( int port, ServerGUI gui, String user ) {
		this.port = port;
		this.gui = gui;
		this.table = new HashtableOH<String, User>(10);
		readUsers( user );
	}

	/**
	 * A method that listens for new clients.
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket( port ); 
			while( true ) {
				try {
					socket = serverSocket.accept();

					gui.showText( "Connected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() );

					input = new DataInputStream( socket.getInputStream() );
					id = input.readUTF();
					gui.showText( "Inloggningsid: " + id + "\n");
					
					// If the unique id is known the client only needs to input the password. 
					if( table.containsKey( id ) ) {
						gui.showText( "Status: Användaren " + table.get( id ).getName() + " är betrodd\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "connected" );
						output.flush();
						Thread clientThread = new Thread( new ListenToClientPassword( socket, output, input, gui, table.get( id ).getPassword() ) );
						clientThread.start();

						// If the unique id is empty it's the first login and the client needs a username and password.
					} else if( id.equals( "" ) ) {
						gui.showText( "Status: Ny användare\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "newuser" );
						output.flush();
						Thread clientThread = new Thread( new ListenToNewClient( socket, output, input, gui, table ) );
						clientThread.start();
						
						// If the unique id is not empty the user is not allowed to log in.
					} else {
						gui.showText( "Status: Ej betrodd användare\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "unknown" );
						output.flush();
						gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
						socket.close();
					}
				} catch( IOException e1 ) {
					gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
				}
			}
		} catch( IOException e1 ) {}
		try {
			serverSocket.close();
		} catch( Exception e ) {}
	}
	
	/**
	 * Function that reads Library Member txt file with all members and puts
	 * them in Server class.
	 * 
	 * @param filename
	 *            Name of file that contains all Library Members.
	 */
	private void readUsers( String filename ) {
		String str;
		String[] values;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream( filename ), "ISO-8859-1"));
			while ( ( str = reader.readLine() ) != null ) {
				values = str.split( ";" );
				user = new User( values[ 0 ], values[ 1 ], values[ 2 ] );
				table.put( values[ 0 ], user );
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * A private method that returns the date and time.
	 * @return date and time
	 */
	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public void terminate() {
		try {
			socket.close();
		} catch( Exception e ) {}
	}
}
