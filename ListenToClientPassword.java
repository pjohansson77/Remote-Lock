package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

/**
 * A class that verifies the password if the client is trusted.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög
 */
public class ListenToClientPassword implements Runnable {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private String password;
	private ServerGUI gui;
	
	/**
	 * The constructor receives the current socket, current streams, a reference to the server GUI and the user password.
	 * 
	 * @param socket The active socket.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui The server GUI.
	 * @param password The user password.
	 */
	public ListenToClientPassword( Socket socket, DataOutputStream output, DataInputStream input, ServerGUI gui, String password ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.password = password;
	}

	/**
	 * A function that verifies the password. If the password is not correct the user is disconnected.
	 */
	public void run() {
		try {
			String clientPassword = input.readUTF();

			if( clientPassword.equals( password ) ) {
				output.writeUTF( "passwordtrue" );
				output.flush();
				gui.showText( "Status: User connected\n" );

				ArduinoChoices choice = new ArduinoChoices( socket, output, input, gui );
				choice.listenToArduinoChoices();
			} else {
				output.writeUTF( "passwordfalse" );
				output.flush();
				gui.showText( "Status: Wrong username or password\n" );
			}
		} catch(IOException e) {} 
		try {
			gui.showText( "Disconnected: " + getTime() + "\nIP-adress: " + socket.getInetAddress().getHostAddress() + "\n" );
			socket.close();
		} catch( Exception e ) {}
	} 
	
	/**
	 * A private function that returns the date and time.
	 * 
	 * @return date and time
	 */
	private Date getTime() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
}
