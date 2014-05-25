package lock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Class that handles a login sequence.
 * 
 * @author Peter Johansson, Andree Höög, Jesper Hansen
 */
public class LoginToServer {
	private String message, arduinoChoice = "1";
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private ChoicesGUI choice;
	private ConnectGUI gui;
	private LoginGUI gui2;
	private LoginToServer loginToServer;
	private LockChoicesGUI lockGUI;

	/**
	 * Constructor for LoginToServer class.
	 * 
	 * @param password A client password.
	 * @param client A reference to the Client class.
	 * @param output The active OutputStream.
	 * @param input The active InputStream.
	 * @param gui A reference to the ConnectGUI class.
	 * @param gui2 A reference to the LoginGUI class.
	 */
	public LoginToServer( Socket socket, DataOutputStream output, DataInputStream input, ConnectGUI gui, LoginGUI gui2 ) {
		this.socket = socket;
		this.input = input;
		this.output = output;
		this.gui = gui;
		this.gui2 = gui2;
		this.loginToServer = this;
	}

	/**
	 * A function that sends the password to the server and starts the ChoicesGUI class.
	 * 
	 * @param password Client password.
	 */
	public void connected( String password ) {
		try {
			output.writeUTF( MD5.encryption( password ) );
			output.flush();

			message = input.readUTF();
			if( message.equals( "passwordtrue" ) ) {
				choice = new ChoicesGUI( loginToServer );
				Thread Status = new Thread( new Status( gui, choice, output, input, gui2 ) );
				Status.start();
			} else if( message.equals( "closed" ) ) {
				gui2.hideFrame();
				gui.setInfoDisplay( "Disconnected" );
				gui.showLogIn();
			} else {
				gui2.setInfoDisplay( "Wrong password" );
				gui2.showLogIn();
			} 
		} catch( Exception e1 ) {
			System.out.println( e1 );
		}
	}
	
	/**
	 * A function for sending changed lock to the server.
	 * 
	 * @param str Lock choice.
	 */
	public void changeArduino( String str ) {
		arduinoChoice = str;
		try {
			output.writeUTF( "arduino;" + arduinoChoice );
			output.flush();
			
			choice.setInfoDisplay( "" );
			choice.showChoices();
		} catch( Exception e1 ) {}
	}
	
	/**
	 * A function that sends the clients arduino choice to the server.
	 * 
	 * @param str Arduino choices.
	 */
	public void choices( String str ) {
		try {
			if( str.equals( "0" ) ) {
				output.writeUTF( str );
				output.flush();
				gui.setInfoDisplay( "Not connected" );
				gui.showLogIn();
				socket.close();
			} else if( str.equals( "3" ) ) {
				gui2.getChoiceGUI( choice );
				choice.hideFrame();
				gui2.setchangepassword( true );
				gui2.setnewpassword( false );
				gui2.setInfoDisplay( "Type old password" );
				gui2.showLogIn();
			} else if( str.equals( "4" ) ) {
				lockGUI = new LockChoicesGUI( loginToServer );
				if( arduinoChoice.equals( "2" ) ) {
					lockGUI.lock2();
				} else {
					lockGUI.lock1();
				}
			} else {
				output.writeUTF( str );
				output.flush();
			}
		} catch( Exception e1 ) {}
	}
	
	/**
	 * A function that sends the clients old password to the server.
	 * 
	 * @param oldpassword The old password.
	 */
	public void changePassword( String oldPassword ) {
		try {
			output.writeUTF( "changepassword;" + MD5.encryption( oldPassword ) );
			output.flush();	
		} catch(Exception e1 ) {}
	}
	
	/**
	 * A function that sends the clients new password to the server.
	 * 
	 * @param newPassword The new password.
	 */
	public void newPassword( String newPassword ) {
		try {
			output.writeUTF( "newpassword;" + MD5.encryption( newPassword ) );
			output.flush();
			choice.showChoices();
		} catch(Exception e1 ) {}
	}
}