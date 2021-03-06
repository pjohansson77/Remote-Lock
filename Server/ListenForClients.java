package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server class that listens for clients and verifies a trusted client with an unique id.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
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
	private boolean listen = true;
	private AdminGUI adminGUI;
	private ArrayList<Socket> list = new ArrayList<Socket>();

	/**
	 * A constructor that recieves a port number to listen on and a reference to the ServerGUI.
	 * 
	 * @param port The port that the server listens on.
	 * @param gui A reference to the ServerGUI class.
	 */
	public ListenForClients( int port, ServerGUI gui ) {
		this.port = port;
		this.table = new HashtableOH<String, User>( 10 );
		MySQL.readMySQL( table, user );
		this.gui = gui;
	}

	/**
	 * A function that listens for new clients and sends them the right way.
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket( port ); 
			while( listen ) {
				try {
					socket = serverSocket.accept();
					addSockets( socket );
					gui.showText( "Connected: " + Time.getTime() + "\nIP-address: " + socket.getInetAddress().getHostAddress() );

					input = new DataInputStream( socket.getInputStream() );
					id = input.readUTF();
					gui.showText( "Inloggningsid: " + id + "\n");

					// If the unique id is known the client only needs to input the password. 
					if( table.containsKey( id ) ) {
						gui.showText( "Status: User " + table.get( id ).getName() + " is trusted\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "connected" );
						output.flush();
						Thread serverThread = new Thread( new ListenToClientPassword( list, socket, output, input, gui, table, id ) );
						serverThread.start();

						// If the unique id is empty it's the first login and the client needs a username and password.
					} else if( id.equals( "" ) ) {
						gui.showText( "Status: New user\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "newuser" );
						output.flush();
						Thread serverThread = new Thread( new ListenToNewClient( list, socket, output, input, gui, table ) );
						serverThread.start();

						// If the unique id is not empty the user is not allowed to log in.
					} else {
						gui.showText( "Status: User not trusted\n" );
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF( "unknown" );
						output.flush();
						gui.showText( "Disconnected: " + Time.getTime() + "\nIP-address: " + socket.getInetAddress().getHostAddress() + "\n" );
						socket.close();
						removeSocket();
					}
				} catch( IOException e ) {
					if( list.size() != 0 ) {
						socket.close();
						removeSocket();
					}
				}
			}
		} catch( IOException e1 ) {}
	}

	/**
	 * A function that adds active sockets to a list.
	 * 
	 * @param socket Client socket
	 */
	private void addSockets( Socket socket ) {
		list.add( socket );
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
	 * A function that stops the server.
	 */
	public void terminate() {
		listen = false;
		try {
			output.writeUTF( "closed" );
			output.flush();
			for( int i = 0; i < list.size(); i ++ ) {
				list.get( i ).close();
			}
			list.clear();
			serverSocket.close();
		} catch( Exception e ) {}
	}

	/**
	 * A function that enables or disables the admin button in the server interface.
	 * @param b boolean true or false
	 */
	public void enableAdminButton( boolean bool ) {
		gui.enableAdminButton( bool );		
	}

	/**
	 * Returns a reference to this hashtable
	 * @return a hashtable
	 */
	public HashtableOH<String, User> getTable() {
		return this.table;
	}

	/**
	 * A function that creates a reference to an new AdminGUI with reference
	 * to this server.
	 */
	public void startAdminGUI() {
		adminGUI = new AdminGUI( this );
	}

	/**
	 * Sets the visibility to the adminGUI to true
	 */
	public void showAdminFrame() {
		adminGUI.showFrame( true );
	}
}
