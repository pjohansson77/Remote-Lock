package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JOptionPane;

/**
 * Class that handles a login sequence.
 * 
 * @author Jesper Hansen, Peter Johansson, Andree Höög, Qasim Ahmad, Andreas Flink, Gustav Frigren
 */
public class LoginToServer implements Runnable {
	private String message, password, status, newPassword;
	private DataInputStream input;
	private DataOutputStream output;
	private Client client;
	private ChoicesGUI choice;
	private ConnectGUI gui;
	private LoginToServer loginToServer;

	/**
	 * Constructor for LoginToServer class.
	 * 
	 * @param password A client password.
	 * @param client A reference to the Client class.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the LoginGUI class.
	 */
	public LoginToServer( String password, Client client, DataOutputStream output, DataInputStream input, ConnectGUI gui ) {
		this.password = password;
		this.client = client;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.loginToServer = this;
	}

	/**
	 * A function that sends the password to the server and starts the ChoicesGUI class.
	 */
	public void run() {
		try {
			output.writeUTF( password );
			output.flush();

			message = input.readUTF();
			if( message.equals( "passwordtrue" ) ) {
				
				choice = new ChoicesGUI( loginToServer );
				Thread Status = new Thread( new Status( choice, output, input ) );
				Status.start();
			} else {
				gui.setInfoDisplay( "Wrong username or password" );
				client.disconnect();
			} 
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	/**
	 * A function that sends the clients Arduino choice to the server.
	 * @param str Arduino choices.
	 */
	public void choices( String str ) {
		String arduinoChoice;
		try {
			arduinoChoice = str;
			if( arduinoChoice.equals( "0" ) ) {
				output.writeUTF( arduinoChoice );
				output.flush();
				client.disconnect();
			} else if( arduinoChoice.equals( "3" ) ) {
				output.writeUTF( "changepassword;" + JOptionPane.showInputDialog( "Type old password:" ) );
				output.flush();					
			} else {
				output.writeUTF( arduinoChoice );
				output.flush();
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	
}