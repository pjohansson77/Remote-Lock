package test;

import java.net.*;
import java.io.*;
/**
 * Test Test, Jesper
 * @author Andree
 *
 */
public class Client {
	private String serverIP;
	private int serverPort;
	private Socket socket;
	private ConnectGUI gui;
	boolean connected = true;
	private LoginGUI gui2;
	private LoginNewUserGUI gui3;
	private DataOutputStream output;
	private DataInputStream input;
	private Client client;
	private ClientID id;
	private ChoicesGUI choice;

	public Client( String serverIP, int serverPort, ConnectGUI gui, ClientID id ) { 
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.gui = gui;
		this.id = id;
		client = this;
		Thread thread = new Thread( new ConnectingToServer() ); 
		thread.start();
	}

	private class ConnectingToServer implements Runnable {
		String message;
		//		boolean connected = false;

		public void run() {
			try {
				socket = new Socket( InetAddress.getByName( serverIP ), serverPort );
				output = new DataOutputStream( socket.getOutputStream() );

				output.writeUTF( id.getID() );
				output.flush(); 

				input = new DataInputStream( socket.getInputStream() );
				message = input.readUTF();

				if( message.equals( "connected" ) ) {
					gui2 = new LoginGUI( client );
					gui2.setInfoDisplay( "Connected" );
				} else if( message.equals( "newuser" ) ) {
					gui3 = new LoginNewUserGUI( client );
					gui3.setInfoDisplay( "New user" );
				} else {
					try {
						socket.close();
					} catch( IOException e ) {
						System.out.println( e );
					}
				}

			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}

	public void startLogin( String password ) {
		Thread newThread = new Thread( new LoginToServer( password ) );
		newThread.start();
	}

	public void startNewUserLogin( String username, String password ) {
		Thread newThread = new Thread( new LoginNewUserToServer( username, password ) );
		newThread.start();
	}

	private class LoginToServer implements Runnable {
		String message, password;

		public LoginToServer( String password ) {
			this.password = password;
		}

		public void run() {
			try {
				output.writeUTF( password );
				output.flush();

				//				connected = input.readBoolean();
				message = input.readUTF();
				if( message.equals( "sant" ) ) {
					gui2.frameStatus( false );
					choice = new ChoicesGUI( client );
				} else {
					System.out.println( "Status: Fel användarnamn eller lösenord" );
					logOutWrong();
				} 
			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}

	public void choices( String str ) {
		String arduinoChoice, message;
		try {
			arduinoChoice = str;
			if( arduinoChoice.equals("0") ) {
				connected = false;
				logOut();
			} else {
				output.writeUTF( arduinoChoice );
				output.flush();
				message = input.readUTF();
				choice.setInfoDisplay( message );
			}
		} catch(Exception e1 ) {
			System.out.println( e1 );
		}
	}

	private class LoginNewUserToServer implements Runnable {
		String message, username, password;

		public LoginNewUserToServer( String username, String password ) {
			this.username = username;
			this.password = password;
		}

		public void run() {
			try {
				output.writeUTF( username );
				output.flush();

				output.writeUTF( password );
				output.flush();

				//				connected = input.readBoolean();
				message = input.readUTF();
				if( message.equals( "falskt" ) ) {
					System.out.println( "Status: Fel användarnamn eller lösenord" );
					logOutNewUser();
				} else {
					id.setID( message );
					System.out.println( "Status: Tillagd i server" );
					logOutNewUser();
				}
			} catch(Exception e1 ) {
				System.out.println( e1 );
			}
		}
	}

	public void logOutWrong() {
		try {
			gui2.frameStatus( false );
			gui.frameStatus( true );
			gui.setInfoDisplay( "Disconnected" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}

	public void logOutNewUser() {
		try {
			gui3.frameStatus( false );
			gui.frameStatus( true );
			gui.setInfoDisplay( "Disconnected" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
	
	public void logOut() {
		try {
			choice.frameStatus( false );
			gui.frameStatus( true );
			gui.setInfoDisplay( "Disconnected" );
			socket.close();
		} catch( IOException e ) {
			System.out.println( e );
		}
	}
}