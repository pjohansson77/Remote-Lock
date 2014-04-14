package arduino;

import java.net.*;
import java.util.*;
import java.io.*;

/**
 * A server that listens for clients and verifies password and mac-addresses.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class Server {
	private int port;
	boolean connected = false;

	/**
	 * A constructor that starts a new connect thread.
	 * @param port The port that the server listens on.
	 */
	public Server( int port ) {
		this.port = port;
		Thread connectThread = new Thread( new Connect() ); 
		connectThread.start();
	}
	/**
	 * A private method that returns the date and time.
	 * @return date and time
	 */
	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * A private class that handles a connecting client and verifies the mac-address.
	 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
	 *
	 */
	private class Connect implements Runnable {
		ServerSocket serverSocket;
		Socket socket;
		String mac;
		DataOutputStream output;
		DataInputStream input;

		/**
		 * A method that listens for new clients.
		 */
		public void run() {
			try {
				serverSocket = new ServerSocket( port ); 
				while( true ) {
					socket = serverSocket.accept();
					connected = true;

					input = new DataInputStream( socket.getInputStream() );
					mac = input.readUTF();
					
					// If the mac-address is known the client only need to input the password. 
					if( mac.equals( "" ) ) {
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF("Connected");
						output.flush();
						Thread clientThread = new Thread( new ConnectAndListenToClient( socket, output, input ) );
						clientThread.start();
						
						// If the mac-address is not known the client also needs a username.
					} else {
						output = new DataOutputStream( socket.getOutputStream() );
						output.writeUTF("Unknown");
						output.flush();
						Thread clientThread = new Thread( new ConnectNewAndListenToClient( socket, output, input ) );
						clientThread.start();
					}
					System.out.println( "Connected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress()
							+ "\nMac-adress: " + mac + "\n" );
				}
			} catch( IOException e1 ) {
				System.out.println( e1 );
			}
			try {
				serverSocket.close();
			} catch( Exception e ) {}
		}
	}

	/**
	 * A class that verifies the password if the client is known.
	 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
	 *
	 */
	private class ConnectAndListenToClient implements Runnable { 
		Socket socket;
		DataInputStream input;
		DataOutputStream output;
		String password;
		
		/**
		 * The constructor receives the current socket and streams.
		 * @param socket The active socket.
		 * @param output The active OutputStream.
		 * @param input The active InputStream.
		 */
		public ConnectAndListenToClient( Socket socket, DataOutputStream output, DataInputStream input ) {
			this.socket = socket;
			this.input = input;
			this.output = output;
		}

		/**
		 * A method that verifies the password. If the password is not correct the user is disconnected.
		 */
		public void run() {
			try {
				password = input.readUTF();
				if( password.toLowerCase().equals( "alfa" ) ) {
					output.writeBoolean( true );
					output.flush();
					choices( socket, output, input );
				} else {
					output.writeBoolean( false );
					output.flush();
				}
			} catch(IOException e) {} 
			try {
				System.out.println( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
			} catch( Exception e ) {}
		} 
	}
	
	/**
	 *  A class that verifies the password if the client is unknown.
	 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
	 *
	 */
	private class ConnectNewAndListenToClient implements Runnable { 
		Socket socket;
		DataInputStream input;
		DataOutputStream output;
		String username, password;

		/**
		 * A method that verifies the password. If the password is not correct the user is disconnected.
		 * @param socket The active socket.
		 * @param output The active OutputStream.
		 * @param input The active InputStream.
		 */
		public ConnectNewAndListenToClient( Socket socket, DataOutputStream output, DataInputStream input ) {
			this.socket = socket;
			this.input = input;
			this.output = output;
		}

		/**
		 * A method that verifies the password and the "admin" user.
		 */
		public void run() {
			try {
				username = input.readUTF();
				password = input.readUTF();
				if( username.toLowerCase().equals( "admin" ) && password.toLowerCase().equals( "alfa" ) ) {
					output.writeBoolean( true );
					output.flush();
					choices( socket, output, input );
				} else {
					output.writeBoolean( false );
					output.flush();
				} 
			} catch(IOException e) {} 
			try {
				System.out.println( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
				socket.close();
			} catch( Exception e ) {}
		} 
	}
	
	/**
	 * A private method that print out the users choices and sends the choice to the ArduinoClient class.
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 */
	private void choices( Socket socket, DataOutputStream output, DataInputStream input ) {
		String message;
		int num;
		try{
			while( connected ) {
				message = input.readUTF();
				num = Integer.parseInt( message );
				ArduinoClient arduino = new ArduinoClient("169.254.146.12", 6666);
				arduino.TalkToArduino(num);
				
				if( num == 0 ) {
					connected = false;
				} else {
					System.out.println( "IP-adress: " + socket.getInetAddress().getHostAddress() + " sent: " + message + "\n" );
					if( num == 1 ) {
						output.writeUTF( "Lampa1" );
					//								System.out.println( "Lampa1" );
					}
					else if( num == 2 ) {
						output.writeUTF( "Lampa2" );
					//								System.out.println( "Lampa2" );
					}
					else if( num == 4 ) {
						output.writeUTF( "Disco" );
					//								System.out.println( "Disco" );
					} 
					else {
						output.writeUTF("Fel val");
					//								System.out.println( "Fel val" );
					}
					output.flush();
				} 
			} 
		} catch(IOException e) {}
	}

	public static void main( String[] args ) {
		new Server( 5555 );
	} 
}