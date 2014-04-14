package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A class that verifies the password if the client is known.
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 *
 */
public class ConnectAndListenToClient implements Runnable {
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	String password;
	boolean connected = true;
	
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

				output.writeUTF( "sant" ); // Andreas fel ;-)
				output.flush();
//				output.writeBoolean( true );

				choices( socket, output, input );
			} else {
				output.writeUTF( "falskt" ); // Andreas fel ;-)
				output.flush();
//				output.writeBoolean( false );

			}
		} catch(IOException e) {} 
		try {
			System.out.println( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
			socket.close();
		} catch( Exception e ) {}
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
//				ArduinoClient arduino = new ArduinoClient("169.254.146.12", 6666);
//				arduino.TalkToArduino(num);
				
				if( num == 0 ) {
					connected = false;
				} else {
					System.out.println( "IP-adress: " + socket.getInetAddress().getHostAddress() + " sent: " + message + "\n" );
					if( num == 1 ) {
						output.writeUTF( "Lampa1" );
					}
					else if( num == 2 ) {
						output.writeUTF( "Lampa2" );
					}
					else if( num == 4 ) {
						output.writeUTF( "Disco" );
					} 
					else {
						output.writeUTF("Fel val");
					}
					output.flush();
				} 
			} 
		} catch(IOException e) {}
	}
}
